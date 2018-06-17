package net.project104.infixparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Operation represents a function/operation acting on
 * one or multiple operands
 * 
 * @author civyshk
 * @version 20180617
 */
public class Operation implements Operand {
	private Operator operator;
	private ArrayList<Operand> operands;
	private int parsedOperands;
	private String argStr;
	private BigDecimal value;
	private int openParenthesis;
	private StringBuilder currentOperandStr;

	private final Calculator calc;
	
	/**
	 * Constructor for a binary operation. It is most useful when creating
	 * an operation from a binary operator like + - * / etc
	 * @param operator Binary {@link Operator}
	 * @param left Left {@link Operand}
	 * @param right Right {@link Operand}
	 * @param calc
	 */
	public Operation(Operator operator, Operand left, Operand right, Calculator calc) {
		if(operator.getArity() != 2) {
			throw new IllegalArgumentException("Wrong operand. It doesn't accept exactly 2 operands");
		}
		this.operator = operator;
		this.operands = new ArrayList<>(2);
		this.operands.add(left);
		this.operands.add(right);
		this.calc = calc;
		this.value = null;
		this.argStr = null;
	}
	
	/**
	 * Constructor for an operation from a String holding its name. 
	 * The function parameters need to be added using {@link #setArguments}
	 * @param functionName The canonical name of the function
	 * @param calc
	 */
	public Operation(String functionName, Calculator calc) {
		this.operator = Operator.fromName(functionName);
		this.operands = new ArrayList<>(1);
		this.calc = calc;
		this.value = null;
		this.argStr = null;
	}
	
	/**
	 * Set the function arguments
	 * @param argStr String with a text representation of them like "arg1, arg2, ... argn"
	 */
	public void setArguments(String argStr) {
		this.argStr = argStr;
	}
	
	private void parse() {
		openParenthesis = 0;
		currentOperandStr = new StringBuilder();
		
		if(argStr != null) {
			for(int i = 0; i < argStr.length(); i++) {
				char c = argStr.charAt(i);
				this.interpret(c, i);
			}
			saveOperand();

			if(openParenthesis != 0) {
				throw new IllegalArgumentException("Opening and closing parenthesis don't match:\n" + argStr);
			}
			
			if(operator.getArity()>= 0 && parsedOperands != operator.getArity()) {
				throw new IllegalStateException("The number of arguments don't match the function arity");
			}
		}

		//Build a bundle with operands and operator
        OperandsBundle bundle = new OperandsBundle(operands.toArray(new Operand[operands.size()]));

		// Warning: idea taken from 104RPNCalc, where this code actually makes sense
		// Warning: use null as ThreadPoolExecutor, so don't even try to call task.start()
        ThreadedOperation operationTask = new ThreadedOperation(bundle, operator, null, calc, null);
        bundle = operationTask.call();

		if(bundle.getError() != null) {
			throw new CalcException(bundle.getError());
		}

		List<BigDecimal> results = bundle.getResults();
		value = results.get(0);
		//Warning: No support for Operators which return more than one result
	}

	private void interpret(char c, int i) {
		if(openParenthesis > 0) {
			if(c == '(') {
				openParenthesis++;
			}else if(c == ')') {
				openParenthesis--;
			}
			addChar(c);
		}else if(c == ',') {
			saveOperand();
		}else {
			addChar(c);
		}
	}
	
	private void addChar(char c) {
		currentOperandStr.append(c);
	}
	
	private void saveOperand() {
		if(currentOperandStr.length() != 0) {
			++parsedOperands;
			operands.add(new RawText(currentOperandStr.toString(), calc));
			currentOperandStr.setLength(0);
		}
	}

	@Override
	public BigDecimal getValue() {
		if(value == null) {
			parse();
		}
		return value;
	}
}
