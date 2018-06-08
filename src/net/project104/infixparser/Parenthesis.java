package net.project104.infixparser;

import java.math.BigDecimal;
import java.util.List;

/**
 * This operand stores its data as a list of operands
 * and a matching number (n-1) of binary operators
 * 
 * @author civyshk
 * @version 20180223
 */
public class Parenthesis implements Operand {
	private List<Operand> operands;
	private List<Operator> operators;

	/**
	 * Constructor for a number of operands and binary operators
	 * @param operands List of operands
	 * @param operators List of binary operators
	 * @throws IllegalArgumentException If the size of operands is not the size of operators + 1
	 */
	public Parenthesis(List<Operand> operands, List<Operator> operators) {
		if(operands.size() != operators.size() + 1) {
			throw new IllegalArgumentException("Lengths don't match operands = operators + 1");
		}
		this.operands = operands;
		this.operators = operators;
	}

	@Override
	public BigDecimal getValue() {
		if(operators.size() > 0) {
			return getOperation().getValue();
		}else {
			return operands.get(0).getValue();
		}		
	}
	
	private Operation getOperation() {
		//Iterate through every operand and choose the one with the least binding priority
//		int maxPrecedence = operators.stream()
//				.mapToInt(o -> o.getPrecedence())
//				.max()
//				.orElse(-1);

		int precedence,
			maxPrecedence = -1;
		for(Operator op : operators){
			precedence = op.getPrecedence();
			if(precedence > maxPrecedence){
				maxPrecedence = precedence;
			}
		}

		//default looping values to choose the leftmost POW
		int start = 0;
		int end = operators.size();
		int step = 1;
		
		//if the operator is not POW, take the rightmost operator first
		if(maxPrecedence > 0) {
			start = end - 1;
			end = -1;
			step = -1;
		}
		
		for(int i = start; i != end; i += step) {
			if(operators.get(i).getPrecedence() == maxPrecedence) {
				Parenthesis left = new Parenthesis(
						operands.subList(0, i + 1),
						operators.subList(0, i));
				
				Parenthesis right = new Parenthesis(
						operands.subList(i+1, operands.size()),
						operators.subList(i+1, operators.size()));
				
				return new Operation(operators.get(i),left, right);
			}
		}
		throw new RuntimeException("Wrongly coded. It should't reach here");
	}

}
