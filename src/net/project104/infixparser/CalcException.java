package net.project104.infixparser;

public class CalcException extends ArithmeticException {
    private CalculatorError error;

    public CalcException(CalculatorError error){
        super(CalculatorError.getErrorString(error));
        this.error = error;
    }

    public CalculatorError getError() {
        return error;
    }

}
