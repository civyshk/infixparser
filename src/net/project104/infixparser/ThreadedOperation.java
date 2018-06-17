package net.project104.infixparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static net.project104.infixparser.Constants.*;

public class ThreadedOperation implements Callable<OperandsBundle> {
    private final OperandsBundle bundle;
    private final Operator operator;
    private final int arity;
    private final ThreadPoolExecutor pool;
    private final Calculator calc;
    private final OperandStack stack;

    public ThreadedOperation(OperandsBundle bundle, Operator operator,
                             ThreadPoolExecutor pool, Calculator calc, OperandStack stack){
        this.bundle = bundle;
        this.operator = operator;
        this.arity = operator.getArity();
        this.pool = pool;
        this.calc = calc;
        this.stack = stack;
    }

    public Future<OperandsBundle> start(){
        return pool.submit(this);
    }

    @Override
    public OperandsBundle call() {
        try {
            switch(arity){
                case ARITY_ALL:         solveArityAll(); break;
                case ARITY_ZERO_ONE:    solveArityZeroOne(); break;
                case ARITY_N:           solveArityN(); break;
                case 0:                 solveArityZero(); break;
                case 1:                 solveArityOne(); break;
                case 2:                 solveArityTwo(); break;
                case 3:                 solveArityThree(); break;
                default:  bundle.setError(CalculatorError.NOT_IMPLEMENTED);
            }
        }catch(ArithmeticException e) {
            bundle.setError(CalculatorError.NOT_IMPLEMENTED);
        }

        return bundle;
    }

    private void solveArityAll() {
        switch (operator) {
            case SUMMATION:         calc.summation(bundle);break;
            case MEAN:              calc.mean(bundle);break;
            default:    bundle.setError(CalculatorError.NOT_IMPLEMENTED);break;
        }
    }

    private void solveArityZeroOne() {
        switch (operator){
            case CONSTANTPI:        calc.pi(bundle); break;
            case CONSTANTPHI:       calc.phi(bundle); break;
            case CONSTANTEULER:     calc.euler(bundle); break;
            default:    bundle.setError(CalculatorError.NOT_IMPLEMENTED); break;
        }
    }

    private void solveArityN() {
        if(stack == null){
            throw new RuntimeException("There is no stack from which pop more numbers");
        }

        try {
            long nLong = bundle.get(0).longValueExact();
            if (nLong <= Integer.MAX_VALUE) {
                int userNumberOperands = (int) nLong;
                if (stack.size() < userNumberOperands) {
                    bundle.setError(CalculatorError.NOT_ENOUGH_ARGS);
                } else {
                    //pop N more results.operands
                    ArrayList<BigDecimal> fullOperands = new ArrayList<>(userNumberOperands + 1);
                    fullOperands.add(bundle.get(0));
                    if(!Thread.currentThread().isInterrupted()){
                        //TODO this doesn't really protects from a weird race condition where the task can
                        //be cancelled in this right moment (after if()) and it'll still pop
                        //numbers from the stack. The main thread is assuming that task.cancel() ((search that .cancel() and set a flag))
                        //prevents the stack to be modified here, but here it's doing exactly that.
                        //
                        //The race condition can only happen if the code before this line is so slow
                        //that it can't complete before the task times out.
                        // That means one whole second when I wrote this comment.
                        //It's very improbable that:
                        // The code before this line takes >1 second
                        // The thread gets cancelled in this exact line
                        // We pop numbers from stack

                        //Why the main thread assumes that the stack won't be modified now?

                        fullOperands.addAll(Arrays.asList(stack.popNumbers(userNumberOperands)));
                        bundle.setOperands(fullOperands.toArray(new BigDecimal[userNumberOperands + 1]));
                        switch (operator) {
                            case SUMMATION_N:   calc.summationN(bundle);break;
                            case MEAN_N:        calc.meanN(bundle);break;
                            default:    bundle.setError(CalculatorError.NOT_IMPLEMENTED);break;
                        }
                    }else{
                        ;; //The result won't be used, there's no need to set the error
                    }
                }
            } else {
                bundle.setError(CalculatorError.TOO_MANY_ARGS);
            }
        }catch(ArithmeticException e) {
            bundle.setError(CalculatorError.LAST_NOT_INT);
        }
    }

    private void solveArityZero() {
        switch (operator) {
            case RANDOM: calc.random(bundle);break;
            default: bundle.setError(CalculatorError.NOT_IMPLEMENTED); break;
        }
    }

    private void solveArityOne() {
        switch (operator) {
            case INVERSION:             calc.inversion(bundle); break;
            case SQUARE:                calc.square(bundle); break;
            case NEGATIVE:              calc.negative(bundle); break;
            case SQUAREROOT:            calc.squareRoot(bundle); break;
            case SINE:                  calc.sine(bundle); break;
            case COSINE:                calc.cosine(bundle); break;
            case TANGENT:               calc.tangent(bundle); break;
            case ARCSINE:               calc.arcsine(bundle); break;
            case ARCCOSINE:             calc.arccosine(bundle); break;
            case ARCTANGENT:            calc.arctangent(bundle); break;
            case SINE_H:                calc.sineH(bundle); break;
            case COSINE_H:              calc.cosineH(bundle); break;
            case TANGENT_H:             calc.tangentH(bundle); break;
            case LOG10:                 calc.log10(bundle); break;
            case LOGN:                  calc.logN(bundle); break;
            case EXPONENTIAL:           calc.exponential(bundle); break;
            case FACTORIAL:             calc.factorial(bundle); break;
            case DEGTORAD:              calc.deg2rad(bundle); break;
            case RADTODEG:              calc.rad2deg(bundle); break;
            case FLOOR:                 calc.floor(bundle); break;
            case ROUND:                 calc.round(bundle); break;
            case CEIL:                  calc.ceil(bundle); break;
            case CIRCLE_SURFACE:        calc.circleSurface(bundle); break;
            default:    bundle.setError(CalculatorError.NOT_IMPLEMENTED); break;
        }
    }

    private void solveArityTwo() {
        switch (operator) {
            case ADDITION:              calc.addition(bundle);break;
            case SUBTRACTION:           calc.subtraction(bundle);break;
            case MULTIPLICATION:        calc.multiplication(bundle);break;
            case DIVISION:              calc.joyDivision(bundle);break;
            case MODULO:                calc.modulo(bundle);break;
            case ROOTYX:                calc.rootYX(bundle);break;
            case LOGYX:                 calc.logYX(bundle);break;
            case EXPONENTIATION:        calc.exponentiation(bundle);break;
            case HYPOTENUSE_PYTHAGORAS: calc.hypotenusePythagoras(bundle);break;
            case LEG_PYTHAGORAS:        calc.legPythagoras(bundle);break;
            default:    bundle.setError(CalculatorError.NOT_IMPLEMENTED);break;
        }
    }

    private void solveArityThree() {
        switch (operator) {
            case TRIANGLE_SURFACE:      calc.triangleSurface(bundle);break;
            case QUARATIC_EQUATION:     calc.quadraticEquation(bundle);break;
            default:    bundle.setError(CalculatorError.NOT_IMPLEMENTED);break;
        }
    }
}
