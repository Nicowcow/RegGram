package misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import processing.Matcher;
import processing.soups.NiceSoup;
import processing.soups.UglySoup;
import core.Strand;
import core.Symbol;

public class RegGram {

	public static void main(String[] args) throws IOException,
			InstantiationException, IllegalAccessException {

		niceSoup();

	}

	public static Symbol[] delimiters() {
		Symbol[] delimiters = { new Symbol("<", true), new Symbol("<", false),
				new Symbol(">", true), new Symbol(">", false) };

		return delimiters;
	}

	public static Matcher matcher() {
		Matcher matcher = new Matcher().useCovers().lift(
				Matcher.LiftRegion.THREE_AND_FIVE, delimiters());
		return matcher;
	}

	public static List<String> strands() {

		int n = 9, sqrt = 3;
		
		System.out.println("Running SQRT with n = " + n + ", sqrt = " + sqrt);

		List<String> strands = new ArrayList<String>();
		String input = "(<b[" + n + "]a>)";
		System.out.println("Input : ");
		System.out.println(new Strand(input));
		System.out.println("Rules : ");
		
		strands.add(input);


		for (int k = 1; k < sqrt; k++) {
			String r1 = "(<([" + (k + 1) + "]b)-([" + (2 * k) + "]a[" + (k)
					+ "]b<))";
			String r2 = "(-(>[" + (k + 1) + "]a[" + (k + 1) + "]b<)(<" + (k + 1)
					+ ">))";
			strands.add(r1);
			strands.add(r2);
			System.out.println(new Strand(r1));
			System.out.println(new Strand(r2));
		}

		return strands;
	}

	public static void niceSoup() {
		int n_iter = 20;
		
		System.out.println("Running NiceSoup with n_iter = " + n_iter );

		NiceSoup m = new NiceSoup(matcher(), n_iter).verbose();

		for (String str : strands()) {
			m.add(str);
		}

		while (!m.isDone()) {
			try {
				m.step();
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}

		System.out.println("\n\n\n ----- RESULTS ----- \n\n\n");
		for (Strand s : m.getCurrentStrands()) {
			System.out.println(" ---- " + s);
		}
	}

	public static void uglySoup() {
		int n_strands = 1;
		System.out.println("Running UglySoup" + ", n strands =  " + n_strands);
		UglySoup m = new UglySoup(matcher(), 0);

		for (String str : strands()) {
			m.add(str, n_strands);
		}

		while (!m.isDone()) {
			try {
				m.step();
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}

		System.out.println(m.toString());

	}
}
