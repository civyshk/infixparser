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

public enum CalculatorError {
    NOT_IMPLEMENTED,
    DIV_BY_ZERO, TOO_BIG, NEGATIVE_SQRT,
    TAN_OUT_DOMAIN, ARCSIN_OUT_RANGE, ARCCOS_OUT_RANGE,
    LOG_OUT_RANGE, WRONG_FACTORIAL,
    NEGATIVE_RADIUS, NEGATIVE_BASE_EXPONENTIATION,
    ROOT_INDEX_ZERO, NEGATIVE_RADICAND, LOG_BASE_ONE,
    SIDE_NEGATIVE, NOT_TRIANGLE, COMPLEX_NUMBER,
    TOO_MANY_ARGS, NOT_ENOUGH_ARGS, LAST_NOT_INT,
    ;

    public static String getErrorString(CalculatorError error) {
        switch(error){
            case NOT_IMPLEMENTED:
                return "Not implemented";
            case DIV_BY_ZERO:
                return "Division by zero";
            case TOO_BIG:
                return "That number is too big";
            case NEGATIVE_SQRT:
                return "Negative square root";
            case TAN_OUT_DOMAIN:
                return "Tangent out of domain";
            case ARCSIN_OUT_RANGE:
                return "Arcsin out of range";
            case ARCCOS_OUT_RANGE:
                return "Arccos out of range";
            case LOG_OUT_RANGE:
                return "Log out of range";
            case WRONG_FACTORIAL:
                return "Wrong factorial";
            case NEGATIVE_RADIUS:
                return "Negative radius";
            case NEGATIVE_BASE_EXPONENTIATION:
                return "Negative base for exponential";
            case ROOT_INDEX_ZERO:
                return "Index of root is zero";
            case NEGATIVE_RADICAND:
                return "Radicand can't be negative";
            case LOG_BASE_ONE:
                return "Base of log can't be one";
            case SIDE_NEGATIVE:
                return "A side is negative";
            case NOT_TRIANGLE:
                return "That is not a triangle";
            case COMPLEX_NUMBER:
                return "That yields a complex number";
            case TOO_MANY_ARGS:
                return "There are too many arguments";
            case NOT_ENOUGH_ARGS:
                return "There are not enough arguments";
            case LAST_NOT_INT:
                return "Last argument is not an integer";
            default:
                return "Unknown error";
        }
    }
}
