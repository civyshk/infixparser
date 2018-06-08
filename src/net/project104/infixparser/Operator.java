package net.project104.infixparser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Rules from https://docs.python.org/3/reference/expressions.html
 * Operators in the same box [precedence] group left to right 
 * (except for exponentiation, which groups from right to left)
 * @author civyshk
 * @cersion 20180223
 */
public enum Operator {
	ADD(2, 2), SUBTRACT(2, 2), 
	MULTIPLY(2, 1), DIVIDE(2, 1), MODULO(2, 1), 
	POW(2, 0),
	SQRT(1), LOG10(1);

	public static ArrayList<String> functionNames;
	static {
		functionNames = new ArrayList<>(Arrays.asList(
				"ADD", "SUBTRACT", 
				"MULTIPLY", "DIVIDE", "MODULO", 
				"POW",
				"SQRT", "LOG10"));
	}
	
	/** 
	 * Lower values of precedence mean a higher binding priority
	 * when grouping expressions
	 */
	private final int precedence;
	
	/**
	 * arity is 2 for binary operators but might hold different
	 * values for other operators
	 */
	private final int arity;
	
	/**
	 * @param arity The number of operands that this operator needs
	 */
	Operator(int arity){
		this(arity, -1);
	}
	
	/**
	 * @param arity The number of operands that this operator needs
	 * @param precedence The highest values mean the lowest binding power
	 */
	Operator(int arity, int precedence){
		this.arity = arity;
		this.precedence = precedence;
	}
	
	public int getPrecedence() {
		return precedence;
	}
	
	public int getArity() {
		return arity;
	}
	
	static public boolean anyStarts(String candidate) {
//		return functionNames.stream().anyMatch(s -> s.startsWith(candidate));
		for (String fun : functionNames) {
			if (fun.startsWith(candidate)) {
				return true;
			}
		}
		return false;
	}
	
	static public Operator fromName(String name) throws IllegalArgumentException {
		switch(name) {
			case "ADD": return ADD;
			case "SUBTRACT": return SUBTRACT;
			case "MULTIPLY": return MULTIPLY;
			case "DIVIDE": return DIVIDE;
			case "MODULO": return MODULO;
			case "POW": return POW;
			case "SQRT": return SQRT;
			case "LOG10": return LOG10;
			default: throw new IllegalArgumentException("There is no function with that name");				
		}
	}
}
