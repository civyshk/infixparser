package net.project104.infixparser;

import java.math.BigDecimal;

/**
 * This interface represents any element that can be applied
 * an operation like + - * / %
 *   
 * @author civyshk
 * @version 20180617
 */
public interface Operand {
	BigDecimal getValue();
}
