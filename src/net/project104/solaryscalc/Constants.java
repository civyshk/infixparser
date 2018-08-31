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

public abstract class Constants {
    public final static int ARITY_ZERO_ONE = -1;// takes one number if it's being written by user
    public final static int ARITY_ALL = -2;// takes all numbers in stack
    public final static int ARITY_N = -3;// takes N+1 numbers, let N be the first number in the stack
}
