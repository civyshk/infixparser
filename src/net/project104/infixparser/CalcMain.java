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
		System.out.println("(C) Civ Ysh K - 2018; GPLv3; Live fast, love hard, never die");
		in.close();
	}

	/*
	Cuanto más la veía más seguro estaba de que era una máquina fabricada por la Cultura.
	No se trataba de nada que pudiera definir con precisión, pero cada instante quepasaba
	contemplando la máquina hacía que estuviera más seguro de ello. Supuso que debía contar
	con cuarenta o cincuenta asientos; el tamaño justo para transportar a todas las personas
	que había visto en la isla. No parecía especialmente nueva o rápida, y no daba la impresión
	de tener ninguna clase de armamento, pero algo en la forma de diseñar y construir aquella
	silueta tan sencilla y utilitaria hablaba de la Cultura. Si la Cultura diseñase un carro de
	bueyes o un automóvil, aquellos artefactos seguirían compartiendo algo con la máquina que
	había al final de la playa, pese a todo el abismo de tiempo existente entre las épocas
	representadas por cada objeto. El enigma habría sido más fácil de resolver si la Cultura
	usase algún emblema o logotipo, pero su negativa a depositar ninguna fe en los símbolos era
	otro más de los muchos aspectos en que la Cultura mostraba su falta de realismo y su
	inexplicable orgullo. La Cultura afirmaba ser justamente lo que era y decía no necesitar
	ese tipo de representaciones exteriores. La Cultura estaba compuesta por todos y cada uno
	de los seres humanos y máquinas que vivían en ella, no por una sola cosa o faceta
	determinada. Al igual que no podía aprisionarse a sí misma con leyes, empobrecerse con el
	uso del dinero o engañarse confiando en los líderes, no estaba dispuesta a
	autorrepresentarse de forma engañosa mediante signos.
	 */

}
