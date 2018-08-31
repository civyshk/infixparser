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
