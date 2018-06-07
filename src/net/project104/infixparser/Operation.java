package net.project104.infixparser;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Operation represents a function/operation acting on
 * one or multiple operands
 * 
 * @author civyshk
 * @version 20180223
 */
public class Operation implements Operand {
	private Operator type;
	private Operand[] operands;
	private int parsedOperands;
	private String argStr;
	private BigDecimal value;
	private int openParenthesis;
	private StringBuilder currentOperandStr;
	
	/**
	 * Constructor for a binary operation. It is most useful when creating
	 * an operation from a binary operator like + - * / etc
	 * @param type Binary {@link Operator}
	 * @param left Left {@link Operand}
	 * @param right Right {@link Operand}
	 */
	public Operation(Operator type, Operand left, Operand right) {
		if(type.getArity() != 2) {
			throw new IllegalArgumentException("Wrong operand. It doesn't accept exactly 2 operands");
		}
		this.type = type;
		this.operands = new Operand[2];
		this.operands[0] = left;
		this.operands[1] = right;
		this.value = null;
	}
	
	/**
	 * Constructor for an operation from a String holding its name. 
	 * The function parameters need to be added using {@link #setArguments}
	 * @param functionName The canonical name of the function
	 */
	public Operation(String functionName) {
		type = Operator.fromName(functionName);
		operands = new Operand[type.getArity()];
		value = null;
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
			
			if(parsedOperands != type.getArity()) {
				throw new IllegalStateException("The number of arguments don't match the function arity");
			}
		}	
		
		switch(type) {//TODO copy code from RPNCalc104k
		case ADD:
			value = operands[0].getValue().add(operands[1].getValue());
			break;
		case SUBTRACT:
			value = operands[0].getValue().subtract(operands[1].getValue()); 
			break;
		case MULTIPLY:
			value = operands[0].getValue().multiply(operands[1].getValue()); 
			break;
		case DIVIDE:
			value = operands[0].getValue().divide(operands[1].getValue(), new MathContext(10, RoundingMode.HALF_UP)); 
			break;
		case MODULO:
			value = new BigDecimal(operands[0].getValue().doubleValue() % operands[1].getValue().doubleValue()); 
			break;
		case POW:
			value = new BigDecimal(Math.pow(operands[0].getValue().doubleValue(), operands[1].getValue().doubleValue())); 
			break;
		case SQRT:
			value = new BigDecimal(Math.pow(operands[0].getValue().doubleValue(), 0.5)); 
			break;
		case LOG10:
			value = new BigDecimal(Math.log10(operands[0].getValue().doubleValue())); 
			break;			
		default:
			throw new RuntimeException("Wrongly coded. It shouldn't reach here");
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
		operands[parsedOperands++] = new RawText(currentOperandStr.toString());
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
