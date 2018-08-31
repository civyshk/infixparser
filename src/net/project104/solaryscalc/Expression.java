/*	Solarys Calc - Console & graphical calculator written in Java
 * 	Copyright 2018 Yeshe Santos García <civyshk@gmail.com>
 *	
 *	This file is part of Solarys Calc
 *	
 *	Solarys Calc is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.project104.solaryscalc;

import java.math.BigDecimal;
import java.util.List;

/**
 * This operand stores its data as a list of operands
 * and a matching number (n-1) of binary operators
 * 
 * @author civyshk
 * @version 20180617
 */
public class Expression implements Operand {
	private List<Operand> operands;
	private List<Operator> operators;
	private final Calculator calc;

	/**
	 * Constructor for a number of operands and binary operators
	 * @param operands List of operands
	 * @param operators List of binary operators
	 * @param calc
	 * @throws IllegalArgumentException If the size of operands is not the size of operators + 1
	 */
	public Expression(List<Operand> operands, List<Operator> operators, Calculator calc) {
		if(operands.size() != operators.size() + 1) {
			throw new IllegalArgumentException("Lengths don't match operands = operators + 1");
		}
		this.operands = operands;
		this.operators = operators;
		this.calc = calc;
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
				Expression left = new Expression(
						operands.subList(0, i + 1),
						operators.subList(0, i),
						calc);
				
				Expression right = new Expression(
						operands.subList(i+1, operands.size()),
						operators.subList(i+1, operators.size()),
						calc);
				
				return new Operation(operators.get(i), left, right, calc);
			}
		}
		throw new RuntimeException("Wrongly coded. It should't reach here");
	}
}
