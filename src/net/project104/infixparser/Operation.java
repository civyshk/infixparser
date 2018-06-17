package net.project104.infixparser;

import java.math.BigDecimal;
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
	private Operand[] operands;
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
		this.operands = new Operand[2];
		this.operands[0] = left;
		this.operands[1] = right;
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
		this.operands = new Operand[this.operator.getArity()];
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
			
			if(parsedOperands != operator.getArity()) {
				throw new IllegalStateException("The number of arguments don't match the function arity");
			}
		}

		//Build a bundle with operands and operator
        OperandsBundle bundle = new OperandsBundle(operands);

		// Warning: idea taken from 104RPNCalc, where this code actually makes sense
		// Warning: use null as ThreadPoolExecutor, so don't even try to call task.start()
        ThreadedOperation operationTask = new ThreadedOperation(bundle, operator, null, calc, null);
        bundle = operationTask.call();

		String error = null;
		if(bundle.getError() != null) {
			error = getErrorString(bundle.getError());
			System.out.println(error);
		}

		List<BigDecimal> results = bundle.getResults();
		if (error == null) {
			value = results.get(0);
			//Warning: No support for Operators which return more than one result
		}
	}

	public static String getErrorString(CalculatorError error) {
		switch(error){
			case NOT_IMPLEMENTED:
				return "Not implemented";
			case DIV_BY_ZERO:
				return "Division by zero";
			case TOO_BIG:
				return "That number is too big";
			case NEGATIVE_SQRT:
				return "Negative square root";
			case TAN_OUT_DOMAIN:
				return "Tangent out of domain";
			case ARCSIN_OUT_RANGE:
				return "Arcsin out of range";
			case ARCCOS_OUT_RANGE:
				return "Arccos out of range";
			case LOG_OUT_RANGE:
				return "Log out of range";
			case WRONG_FACTORIAL:
				return "Wrong factorial";
			case NEGATIVE_RADIUS:
				return "Negative radius";
			case NEGATIVE_BASE_EXPONENTIATION:
				return "Negative base for exponential";
			case ROOT_INDEX_ZERO:
				return "Index of root is zero";
			case NEGATIVE_RADICAND:
				return "Radicand can't be negative";
			case LOG_BASE_ONE:
				return "Base of log can't be one";
			case SIDE_NEGATIVE:
				return "A side is negative";
			case NOT_TRIANGLE:
				return "That is not a triangle";
			case COMPLEX_NUMBER:
				return "That yields a complex number";
			case TOO_MANY_ARGS:
				return "There are too many arguments";
			case NOT_ENOUGH_ARGS:
				return "There are not enough arguments";
			case LAST_NOT_INT:
				return "Last argument is not an integer";
			default:
				return "Unknown error";
		}
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
		if(currentOperandStr.length() == 0) {
			throw new IllegalStateException("There is an empty argument");
		}
		operands[parsedOperands++] = new RawText(currentOperandStr.toString(), calc);
		currentOperandStr.setLength(0);
	}

	@Override
	public BigDecimal getValue() {
		if(value == null) {
			parse();
		}
		return value;
	}
}
