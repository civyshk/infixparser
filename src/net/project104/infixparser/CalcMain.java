package net.project104.infixparser;
import java.util.Scanner;

/**
 * Accepts a mathematical expression from the user
 * and returns its numerical value
 * @author civyshk
 * @created 20180220
 * @version 20180223
 */
public class CalcMain {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("salir | exit | quit | q" );
		System.out.print("> ");
		while(in.hasNext()) {
			String s = in.nextLine();
			if(s.trim().toLowerCase().matches("^salir|exit|q(uit)?$")) {
				break;
			}
			RawText p = new RawText(s);
			try {
				System.out.println(p.getValue());
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			System.out.print("> ");
		}
		System.out.println("Sponsored by Civyshk; GPLv3");
		in.close();
	}
}
