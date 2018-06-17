package net.project104.infixparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OperandsBundle {
    private BigDecimal[] operandsBD;
    private Operand[] operandsO;
    private ArrayList<BigDecimal> results = new ArrayList<>(1);
    private CalculatorError error = null;

    public OperandsBundle(BigDecimal[] operands) {
        operandsBD = operands.clone();
        operandsO = null;
    }

    public OperandsBundle(Operand[] operands) {
        operandsO = operands.clone();
        operandsBD = null;
    }

    public BigDecimal get(int i){
        try{
            return operandsBD[i];
        }catch(NullPointerException e){
            return operandsO[i].getValue();
        }
    }

    public BigDecimal[] getOperands(){
        if (operandsBD != null) {
            return operandsBD;
        }else{
            BigDecimal[] operands = new BigDecimal[operandsO.length];
            for(int i=0; i<operandsO.length; ++i){
                operands[i] = operandsO[i].getValue();
            }
            return operands;
        }
    }

    public List<BigDecimal> getResults(){
        return results;
    }

    public void add(BigDecimal number){
        results.add(number);
    }

    public void setError(CalculatorError error){
        this.error = error;
    }

    public CalculatorError getError() {
        return error;
    }

    public void setOperands(BigDecimal[] operands) {
        this.operandsBD = operands;
    }
}
