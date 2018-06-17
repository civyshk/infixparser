package net.project104.infixparser;

import static net.project104.infixparser.Constants.*;

import java.util.ArrayList;

/**
 * Rules from https://docs.python.org/3/reference/expressions.html
 * Operators in the same box [precedence] group left to right 
 * (except for exponentiation, which groups from right to left)
 * @author civyshk
 * @version 20180617
 */
public enum Operator {

	ADDITION("ADD", 2, 2), SUBTRACTION("SUBTRACT", 2, 2),
	MULTIPLICATION("MULTIPLY", 2, 1), DIVISION("DIVIDE", 2, 1), MODULO("MOD", 2, 1),
	EXPONENTIATION("POW", 2, 0),

	SQUARE("SQUARE", 1), SQUAREROOT("SQRT", 1), ROOTYX("ROOT", 2), NEGATIVE("NEG", 1), INVERSION("INVERSION", 1),

	LOG10("LOG10", 1), LOGYX("LOG", 2), LOGN("LOGN", 1), EXPONENTIAL("EXP", 1), FACTORIAL("FACT", 1),

	SINE("SIN", 1), COSINE("COS", 1), TANGENT("TAN", 1),
	ARCSINE("ASIN", 1), ARCCOSINE("ACOS", 1), ARCTANGENT("ATAN", 1),
	SINE_H("SINH", 1), COSINE_H("COSH", 1), TANGENT_H("TANH", 1),
	DEGTORAD("RAD", 1), RADTODEG("DEG", 1),

	FLOOR("FLOOR", 1), ROUND("ROUND", 1), CEIL("CEIL", 1),

	RANDOM("RAND", 0),

	SUMMATION("SUM", ARITY_ALL), SUMMATION_N(null, ARITY_N),
	MEAN("AVG", ARITY_ALL), MEAN_N(null, ARITY_N),

	CONSTANTPI("PI", ARITY_ZERO_ONE), CONSTANTEULER("E", ARITY_ZERO_ONE), CONSTANTPHI("PHI", ARITY_ZERO_ONE),

	CIRCLE_SURFACE("SFCCIRCLE", 1), TRIANGLE_SURFACE("SFCTRIANGLE", 3),
	HYPOTENUSE_PYTHAGORAS("PYTHAHYPO", 2), LEG_PYTHAGORAS("PYTHALEG", 2),
	QUARATIC_EQUATION(null, 3);

	public static final ArrayList<String> functionNames;
	static {
		functionNames = new ArrayList<>();
		for(Operator op : Operator.values()){
			String name = op.getFName();
			if(name != null) {
				functionNames.add(name);
			}
		}
	}

	/**
	 * Text representation of the operation, which the user can
	 * write to access it
	 * If null, the user can't directly use it in an expression
	 */
	private final String fName;

	/** 
	 * Lower values of precedence mean a higher binding priority
	 * when grouping expressions. It only applies to binary
	 * operators
	 * Non-binary operators have an unused negative precedence
	 */
	private final int precedence;
	
	/**
	 * Arity is 2 for binary operators but holds different
	 * values for other operators
	 */
	private final int arity;
	
	/**
	 * @param fName The text that the user can write to access this Operator
	 * @param arity The number of operands that this operator needs
	 */
	Operator(String fName, int arity){
		this(fName, arity, -1);
	}
	
	/**
	 * @param arity The number of operands that this operator needs
	 * @param precedence The highest values mean the lowest binding power
	 */
	Operator(String fName, int arity, int precedence){
		this.fName = fName;
		this.arity = arity;
		this.precedence = precedence;
	}

	public String getFName(){
		return fName;
	}
	
	public int getPrecedence() {
		return precedence;
	}
	
	public int getArity() {
		return arity;
	}
	
	public static boolean anyStarts(String candidate) {
//		return functionNames.stream().anyMatch(s -> s.startsWith(candidate));
		for (String fun : functionNames) {
			if (fun.startsWith(candidate)) {
				return true;
			}
		}
		return false;
	}
	
	public static Operator fromName(String name) throws IllegalArgumentException {
		if(name == null) return null;

		for(Operator op : Operator.values()){
			if(name.equals(op.getFName())){
				return op;
			}
		}
		throw new IllegalArgumentException("There is no function with that name");
	}
}
