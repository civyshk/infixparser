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

/**
 * Number wraps a numeric value to act as an operand
 * @author civyshk
 *
 */
public class Number implements Operand{
	private BigDecimal value;

	public Number(BigDecimal bd) {
		value = bd;
	}
	
	public Number(String s) {
		value = new BigDecimal(s);
	}

	@Override
	public BigDecimal getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

}
