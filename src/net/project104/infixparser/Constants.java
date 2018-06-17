package net.project104.infixparser;

public abstract class Constants {
    public final static int ARITY_ZERO_ONE = -1;// takes one number if it's being written by user
    public final static int ARITY_ALL = -2;// takes all numbers in stack
    public final static int ARITY_N = -3;// takes N+1 numbers, let N be the first number in the stack
}
