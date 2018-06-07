package net.project104.infixparser;

import java.math.BigDecimal;

/**
 * Number wraps a numeric value to act as an operand
 * @author besokare
 *
 */
public class Number implements Operand{
	private BigDecimal value;

	public Number(BigDecimal bd) {
		value = bd;
	}
	
	public Number(String s) {
		value = new BigDecimal(s);
	}

	@Override
	public BigDecimal getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

}
