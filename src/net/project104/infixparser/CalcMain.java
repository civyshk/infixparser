package net.project104.infixparser;
import java.util.Scanner;

/**
 * Accepts a mathematical expression from the user
 * and returns its numerical value
 * @author civyshk
 * @since 20180220
 * @version 20180617
 */
public class CalcMain {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Calculator calc = new Calculator();
		System.out.println("salir | exit | quit | q" );
		System.out.print("> ");
		while(in.hasNext()) {
			String s = in.nextLine();
			if(s.trim().toLowerCase().matches("^salir|exit|q(uit)?$")) {
				break;
			}
			RawText p = new RawText(s, calc);
			try {
				System.out.println(p.getValue());
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			System.out.print("> ");
		}
		System.out.println("(C) Civyshk - 2018; GPLv3; Live fast, love hard, never die");
		in.close();
	}
}
