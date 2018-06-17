package net.project104.infixparser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import static net.project104.infixparser.CalculatorError.*;

public class Calculator {

    public final static BigDecimal BIG_PI = new BigDecimal(Math.PI);
    public final static BigDecimal BIG_EULER = new BigDecimal(Math.E);
    public final static BigDecimal BIG_PHI = new BigDecimal("1.618033988749894");
    public static final BigDecimal RAD_TO_DEG_FACTOR = new BigDecimal("57.2957795130823208768");
    public static final BigDecimal DEG_TO_RAD_FACTOR = new BigDecimal("0.01745329251994329577");

    public final static int GOOD_PRECISION = 10;
    public final static RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    public final static MathContext DEFAULT_MATH_CONTEXT = new MathContext(GOOD_PRECISION, ROUNDING_MODE);

    private AngleMode angleMode;

    public Calculator(){
        angleMode = AngleMode.DEGREE;
    }

    public void setAngleMode(AngleMode mode) {
        angleMode = mode;
    }

    public AngleMode getAngleMode(){
        return angleMode;
    }

    public static MathContext getGoodContext(BigDecimal... operands) {
        int precision = GOOD_PRECISION;
        for (BigDecimal op : operands) {
            precision = Math.max(precision, op.precision());
        }
        return new MathContext(precision, ROUNDING_MODE);
    }

    public static boolean doubleIsInfinite(BigDecimal number) {
        return number.doubleValue() == Double.NEGATIVE_INFINITY ||
                number.doubleValue() == Double.POSITIVE_INFINITY;
    }

    public static BigDecimal getHardcodedCosine(BigDecimal angle, AngleMode angleMode)
            throws IllegalArgumentException
    {
        BigDecimal degrees = angle;
        if (angleMode == AngleMode.RADIAN) {
            degrees = toDegrees(angle);
        }

        BigDecimal remainder = degrees.abs().remainder(new BigDecimal(360), getGoodContext(angle));
        if(remainder.compareTo(new BigDecimal(90)) == 0 || remainder.compareTo(new BigDecimal(270)) == 0){
            return BigDecimal.ZERO;
        }else{
            throw new IllegalArgumentException();
        }
    }

    public static BigDecimal getHardcodedTangent(BigDecimal angle, AngleMode angleMode)
            throws IllegalArgumentException
    {
        BigDecimal degrees = angle;
        if (angleMode == AngleMode.RADIAN) {
            degrees = toDegrees(angle);
        }

        BigDecimal remainder = degrees.abs().remainder(new BigDecimal(360), getGoodContext(angle)).setScale(9, RoundingMode.HALF_UP);
        if(remainder.compareTo(new BigDecimal(90)) == 0 || remainder.compareTo(new BigDecimal(270)) == 0){
            return null;
        }else{
            throw new IllegalArgumentException();
        }
    }

    public static BigInteger factorial(BigInteger operand) {
        if (operand.compareTo(BigInteger.ZERO) < 0) {
            throw new ArithmeticException("Negative factorial");
        } else if (operand.compareTo(BigInteger.ZERO) == 0) {
            return BigInteger.ONE;
        } else {
            BigInteger result = BigInteger.ONE;
            for (BigInteger i = operand; i.compareTo(BigInteger.ONE) > 0; i = i.subtract(BigInteger.ONE)) {
                result = result.multiply(i);
            }
            return result;
        }
    }

    public static BigDecimal toDegrees(BigDecimal radians) {
        return radians.multiply(RAD_TO_DEG_FACTOR, getGoodContext(radians));
    }

    public static BigDecimal toRadians(BigDecimal degrees) {
        return degrees.multiply(DEG_TO_RAD_FACTOR, getGoodContext(degrees));
    }

    /* 0 */

    public void random(OperandsBundle bundle){
		bundle.add(new BigDecimal(Math.random()));
    }

    /* 1 */

    public void inversion(OperandsBundle bundle){
        BigDecimal[] operands = bundle.getOperands();
        if (operands[0].compareTo(BigDecimal.ZERO) == 0) {
            bundle.setError(DIV_BY_ZERO);
        } else {
            bundle.add(BigDecimal.ONE.divide(operands[0], DEFAULT_MATH_CONTEXT));
        }
    }

    public void square(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        try {
            bundle.add(operands[0].pow(2, getGoodContext(operands[0])));
        }catch(ArithmeticException e) {
            bundle.setError(TOO_BIG);
        }
    }

    public void negative(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        bundle.add(operands[0].negate());
    }

    public void squareRoot(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (operands[0].compareTo(BigDecimal.ZERO) < 0) {
            bundle.setError(NEGATIVE_SQRT);
        } else if (doubleIsInfinite(operands[0])) {
            bundle.setError(TOO_BIG);
        } else {
            bundle.add(new BigDecimal(Math.sqrt(operands[0].doubleValue()), Calculator.getGoodContext(operands[0])));
        }
    }

    public void sine(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        BigDecimal radians = operands[0];
        if (angleMode == AngleMode.DEGREE) {
            radians = toRadians(operands[0]);
        }

        if (Calculator.doubleIsInfinite(radians)) {
            bundle.setError(TOO_BIG);
        } else {
            bundle.add(new BigDecimal(Math.sin(radians.doubleValue()),
                    Calculator.getGoodContext(operands[0])));
        }
    }

    public void cosine(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        try {
            bundle.add(Calculator.getHardcodedCosine(operands[0], angleMode));
        }catch(IllegalArgumentException e) {
            BigDecimal radians = operands[0];
            if (angleMode == AngleMode.DEGREE) {
                radians = Calculator.toRadians(operands[0]);
            }

            if (Calculator.doubleIsInfinite(radians)) {
                bundle.setError(TOO_BIG);
            } else {
                bundle.add(new BigDecimal(Math.cos(radians.doubleValue()), Calculator.getGoodContext(operands[0])));
            }
        }
    }

    public void tangent(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        try {
            BigDecimal result = Calculator.getHardcodedTangent(operands[0], angleMode);
            if (result != null) {
                bundle.add(result);
            } else {
                bundle.setError(TAN_OUT_DOMAIN);
            }
        }catch(IllegalArgumentException e) {
            BigDecimal radians = operands[0];
            if (angleMode == AngleMode.DEGREE) {
                radians = Calculator.toRadians(operands[0]);
            }

            if (Calculator.doubleIsInfinite(radians)) {
                bundle.setError(TOO_BIG);
            } else {
                bundle.add(new BigDecimal(Math.tan(radians.doubleValue()), Calculator.getGoodContext(operands[0])).setScale(9, RoundingMode.HALF_UP));
            }
        }
    }

    public void arcsine(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (operands[0].compareTo(BigDecimal.ONE.negate()) < 0
                || operands[0].compareTo(BigDecimal.ONE) > 0) {
            bundle.setError(ARCSIN_OUT_RANGE);
        } else if (Calculator.doubleIsInfinite(operands[0])) {
            bundle.setError(TOO_BIG);
        } else {
            BigDecimal result = new BigDecimal(Math.asin(operands[0].doubleValue()), Calculator.getGoodContext(operands[0]));
            if (angleMode == AngleMode.DEGREE) {
                result = Calculator.toDegrees(result);
            }
            bundle.add(result);
        }
    }

    public void arccosine(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (operands[0].compareTo(new BigDecimal("-1.0")) < 0
                || operands[0].compareTo(new BigDecimal("1.0")) > 0) {
            bundle.setError(ARCCOS_OUT_RANGE);
        } else if (Calculator.doubleIsInfinite(operands[0])) {
            bundle.setError(TOO_BIG);
        } else {
            BigDecimal result = new BigDecimal(Math.acos(operands[0].doubleValue()), Calculator.getGoodContext(operands[0]));
            if (angleMode == AngleMode.DEGREE) {
                result = Calculator.toDegrees(result);
            }
            bundle.add(result);
        }
    }

    public void arctangent(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (Calculator.doubleIsInfinite(operands[0])) {
            bundle.setError(TOO_BIG);
        } else {
            BigDecimal result = new BigDecimal(Math.atan(operands[0].doubleValue()), Calculator.getGoodContext(operands[0]));
            if (angleMode == AngleMode.DEGREE) {
                result = Calculator.toDegrees(result);
            }
            bundle.add(result);
        }
    }

    public void sineH(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (Calculator.doubleIsInfinite(operands[0])) {
            bundle.setError(TOO_BIG);
        } else {
            try {
                bundle.add(new BigDecimal(Math.sinh(operands[0].doubleValue()),
                        Calculator.getGoodContext(operands[0])));
            }catch(NumberFormatException e) {
                bundle.setError(TOO_BIG);
            }
        }
    }

    public void cosineH(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (Calculator.doubleIsInfinite(operands[0])) {
            bundle.setError(TOO_BIG);
        } else {
            try {
                bundle.add(new BigDecimal(Math.cosh(operands[0].doubleValue()), Calculator.getGoodContext(operands[0])));
            }catch(NumberFormatException e) {
                bundle.setError(TOO_BIG);
            }
        }
    }

    public void tangentH(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (Calculator.doubleIsInfinite(operands[0])) {
            bundle.setError(TOO_BIG);
        } else {
            try {
                bundle.add(new BigDecimal(Math.tanh(operands[0].doubleValue()), Calculator.getGoodContext(operands[0])));
            }catch(NumberFormatException e) {
                bundle.setError(TOO_BIG);
            }
        }
    }

    public void log10(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (operands[0].compareTo(BigDecimal.ZERO) <= 0) {
            bundle.setError(LOG_OUT_RANGE);
        } else if (Calculator.doubleIsInfinite(operands[0])) {
            bundle.setError(TOO_BIG);
        } else {
            bundle.add(new BigDecimal(Math.log10(operands[0].doubleValue()), Calculator.getGoodContext(operands[0])));
        }
    }

    public void logN(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (operands[0].compareTo(BigDecimal.ZERO) <= 0) {
            bundle.setError(LOG_OUT_RANGE);
        } else if (Calculator.doubleIsInfinite(operands[0])) {
            bundle.setError(TOO_BIG);
        } else {
            bundle.add(new BigDecimal(Math.log(operands[0].doubleValue()), Calculator.getGoodContext(operands[0])));
        }
    }

    public void exponential(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (Calculator.doubleIsInfinite(operands[0])) {
            bundle.setError(TOO_BIG);
        } else {
            try {
                bundle.add(new BigDecimal(Math.exp(operands[0].doubleValue()), Calculator.getGoodContext(operands[0])));
            }catch(NumberFormatException e) {
                bundle.setError(TOO_BIG);
            }
        }
    }

    public void factorial(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        try {
            if (operands[0].compareTo(new BigDecimal(1000)) > 0) {
                bundle.setError(TOO_BIG);
            } else {
                BigInteger operand = operands[0].toBigIntegerExact();
                bundle.add(new BigDecimal(Calculator.factorial(operand)));
            }
        }catch(ArithmeticException e) {
            bundle.setError(WRONG_FACTORIAL);
        }
    }

    public void deg2rad(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        bundle.add(Calculator.toRadians(operands[0]));
    }

    public void rad2deg(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        bundle.add(Calculator.toDegrees(operands[0]));
    }

    public void floor(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        bundle.add(operands[0].setScale(0, RoundingMode.FLOOR));
    }

    public void round(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        bundle.add(operands[0].setScale(0, RoundingMode.HALF_UP));
    }

    public void ceil(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        bundle.add(operands[0].setScale(0, RoundingMode.CEILING));
    }

    public void circleSurface(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (operands[0].compareTo(BigDecimal.ZERO) < 0) {
            bundle.setError(NEGATIVE_RADIUS);
        } else {
            try {
                bundle.add(operands[0].pow(2).multiply(BIG_PI, Calculator.getGoodContext(operands[0])));
            }catch(ArithmeticException e) {
                bundle.setError(TOO_BIG);
            }
        }
    }

    /* 2 */

    public void addition(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        bundle.add(operands[0].add(operands[1]));
    }

    public void multiplication(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        bundle.add(operands[0].multiply(operands[1]));
    }

    public void exponentiation(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        //y^x; x is results.operands[1]; y is results.operands[0]
        if (Calculator.doubleIsInfinite(operands[0]) ||
                Calculator.doubleIsInfinite(operands[1]))
        {
            bundle.setError(TOO_BIG);
        } else if (operands[0].compareTo(BigDecimal.ZERO) > 0) {
            try {
                bundle.add(new BigDecimal(Math.pow(operands[0].doubleValue(), operands[1].doubleValue()), Calculator.getGoodContext(operands)));
            }catch(NumberFormatException e) {
                bundle.setError(TOO_BIG);
            }
        } else {
            try {
                BigInteger exponent = operands[1].toBigIntegerExact();
                bundle.add(new BigDecimal(Math.pow(operands[0].doubleValue(), exponent.doubleValue())));
            }catch(ArithmeticException e) {
                bundle.setError(NEGATIVE_BASE_EXPONENTIATION);
            }catch(NumberFormatException e) {
                bundle.setError(TOO_BIG);
            }
        }
    }

    public void subtraction(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        bundle.add(operands[0].subtract(operands[1]));
    }

    public void joyDivision(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (operands[1].compareTo(BigDecimal.ZERO) == 0) {
            bundle.setError(DIV_BY_ZERO);
        } else {
            bundle.add(operands[0].divide(operands[1], Calculator.getGoodContext(operands)));
        }
    }

    public void modulo(OperandsBundle bundle) {
        BigDecimal[] operands = bundle.getOperands();
        if (operands[1].compareTo(BigDecimal.ZERO) == 0) {
            bundle.setError(DIV_BY_ZERO);
        } else {
            bundle.add(operands[0].remainder(operands[1], Calculator.getGoodContext(operands)));
        }
    }

    public void rootYX(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        //x^(1/y); x is results.operands[1]; y is results.operands[0]
        if (Calculator.doubleIsInfinite(operands[1])) {
            bundle.setError(TOO_BIG);
        } else if (operands[0].compareTo(BigDecimal.ZERO) == 0) {
            bundle.setError(ROOT_INDEX_ZERO);
        } else if (operands[1].compareTo(BigDecimal.ZERO) > 0) {
            bundle.add(new BigDecimal(
                    Math.pow(operands[1].doubleValue(), BigDecimal.ONE.divide(operands[0], Calculator.DEFAULT_MATH_CONTEXT).doubleValue()),
                    Calculator.getGoodContext(operands)));
        } else {
            bundle.setError(NEGATIVE_RADICAND);
        }
    }

    public void logYX(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        //log(x) in base y; x is results.operands[1]; y is results.operands[0]
        if (operands[0].compareTo(BigDecimal.ZERO) <= 0
                || operands[1].compareTo(new BigDecimal("0.0")) <= 0)
        {
            bundle.setError(LOG_OUT_RANGE);
        } else if (Calculator.doubleIsInfinite(operands[0]) || Calculator.doubleIsInfinite(operands[1])) {
            bundle.setError(TOO_BIG);
        } else {
            BigDecimal divisor = new BigDecimal(Math.log(operands[0].doubleValue()));
            if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                bundle.setError(LOG_BASE_ONE);
            } else {
                bundle.add(new BigDecimal(Math.log(operands[1].doubleValue()))
                            .divide(divisor, Calculator.getGoodContext(operands)));
            }
        }
    }

    public void hypotenusePythagoras(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (Calculator.doubleIsInfinite(operands[0]) || Calculator.doubleIsInfinite(operands[1])) {
            bundle.setError(TOO_BIG);
        } else if (operands[0].compareTo(BigDecimal.ZERO) < 0 || operands[1].compareTo(BigDecimal.ZERO) < 0) {
            bundle.setError(SIDE_NEGATIVE);
        } else {
            bundle.add(operands[0]);
            bundle.add(operands[1]);
            bundle.add(new BigDecimal(
                    Math.hypot(operands[0].doubleValue(), operands[1].doubleValue()),
                    Calculator.getGoodContext(operands)));
            //TODO don't use Math.hypot and avoid .doubleValue()
        }
    }

    public void legPythagoras(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        if (operands[0].compareTo(BigDecimal.ZERO) < 0 || operands[1].compareTo(BigDecimal.ZERO) < 0) {
            bundle.setError(SIDE_NEGATIVE);
        } else {
            BigDecimal hyp = operands[0].max(operands[1]);
            BigDecimal leg = operands[0].min(operands[1]);
            BigDecimal subtract = hyp.pow(2).subtract(leg.pow(2));
            if (Calculator.doubleIsInfinite(subtract)) {
                bundle.setError(TOO_BIG);
            } else {
                bundle.add(operands[0]);
                bundle.add(operands[1]);
                bundle.add(new BigDecimal(Math.sqrt(subtract.doubleValue()), Calculator.getGoodContext(operands)));
            }
        }
    }

    public void triangleSurface(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        BigDecimal p = operands[0]
                        .add(operands[1])
                        .add(operands[2])
                        .divide(new BigDecimal(2), Calculator.DEFAULT_MATH_CONTEXT);
        BigDecimal q = p.multiply(p.subtract(operands[0]))
                        .multiply(p.subtract(operands[1]))
                        .multiply(p.subtract(operands[2]));
        if (q.compareTo(BigDecimal.ZERO) < 0) {
            bundle.setError(NOT_TRIANGLE);
        } else if (Calculator.doubleIsInfinite(q)) {
            bundle.setError(TOO_BIG);
        } else {
            bundle.add(new BigDecimal(Math.sqrt(q.doubleValue()), Calculator.getGoodContext(operands)));
        }
    }

    public void quadraticEquation(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        //0=ax2+bx+c, a==z==op[0]; b==y==op[1]; c==x==op[2]
        BigDecimal a = operands[0];
        BigDecimal b = operands[1];
        BigDecimal c = operands[2];
        BigDecimal radicand = b.pow(2).subtract(a.multiply(c).multiply(new BigDecimal(4)));
        if (Calculator.doubleIsInfinite(radicand)) {
            bundle.setError(TOO_BIG);
        } else if (radicand.compareTo(BigDecimal.ZERO) < 0) {
            bundle.setError(COMPLEX_NUMBER);
        } else {
            BigDecimal root = new BigDecimal(Math.sqrt(radicand.doubleValue()));
            bundle.add(root.subtract(b).divide(a.multiply(new BigDecimal(2)), Calculator.DEFAULT_MATH_CONTEXT));
            bundle.add(root.negate().subtract(b).divide(a.multiply(new BigDecimal(2)), Calculator.DEFAULT_MATH_CONTEXT));
        }
    }

    /* ALL */

    public void summation(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal op : operands) {
            result = result.add(op);
        }
        bundle.add(result);
    }

    public void mean(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        BigDecimal result = BigDecimal.ZERO;
        int precision = 1;
        for (BigDecimal op  :operands) {
            precision = Math.max(precision, op.precision());
            result = result.add(op);
        }
        precision = Math.max(precision, Calculator.GOOD_PRECISION);
        MathContext mathContext = new MathContext(precision, Calculator.ROUNDING_MODE);
        bundle.add(result.divide(new BigDecimal(operands.length), mathContext));
    }

    public void pi(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        BigDecimal result = BIG_PI.round(Calculator.DEFAULT_MATH_CONTEXT);
        if (operands.length == 1) {
            result = result.multiply(operands[0], Calculator.getGoodContext(operands[0]));
        }
        bundle.add(result);
    }

    public void phi(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        BigDecimal result = BIG_PHI.round(Calculator.DEFAULT_MATH_CONTEXT);
        if (operands.length == 1) {
            result = result.multiply(operands[0], Calculator.getGoodContext(operands[0]));
        }
        bundle.add(result);
    }

    public void euler(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        BigDecimal result = BIG_EULER.round(Calculator.DEFAULT_MATH_CONTEXT);
        if (operands.length == 1) {
            result = result.multiply(operands[0], Calculator.getGoodContext(operands[0]));
        }
        bundle.add(result);
    }

    /* N */

    public void summationN(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 1; i < operands.length; i++) {
            result = result.add(operands[i]);
        }
        bundle.add(result);
    }

    public void meanN(OperandsBundle bundle){
		BigDecimal[] operands = bundle.getOperands();
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 1; i < operands.length; i++) {
            result = result.add(operands[i]);
        }
        result = result.divide(operands[0], Calculator.getGoodContext(operands));
        bundle.add(result);
    }
}
