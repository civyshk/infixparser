package net.project104.infixparser;

import java.math.BigDecimal;

public interface OperandStack {
    int size();
    BigDecimal[] popNumbers(int n);
}
