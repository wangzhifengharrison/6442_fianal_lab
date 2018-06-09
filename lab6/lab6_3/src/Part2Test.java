

/*
 * Part2Test
 * Eric McCreath 2017
 */

public class Part2Test {

	
	public static void main(String[] args) {
		testexp("0");
		testexp("  inc(inc(inc(0)))");
		testexp(" dec (dec(0))");
		testexp("  (0 ? inc(inc(0)):  dec(0)  ) ");
		testexp("  (dec(0) ? inc(inc(0)):  dec(0)  ) ");
	}

	private static void testexp(String str) {
		Tokenizer tok = new SimpleTokenizer(str);
		Exp exp = Exp.parseExp(tok);
		showandeval(str, exp, new Functions());
	}

	private static void showandeval(String raw, Exp exp, Functions funs) {
		Subs subs = new Subs();
		System.out.println("parsing " + raw + " is " + exp.show() + " evaluates to " + exp.evaluate(subs, funs));
	}

}
