
/*
 * Part2Test
 * Eric McCreath 2017
 */

public class Part3Test {

	
	public static void main(String[] args) {
		String str2 = "add(X,Y) = (X ? Y : add(dec(X),inc(Y)))\n" +
				"sub(X,Y) = (Y ? X : sub(dec(X),dec(Y)))\n" +
				"mult(X,Y) = (X ? 0 : add(Y,mult(dec(X),Y)))\n" +
				"fib(A) = (A?0:(sub(A,inc(0))?inc(0):add(fib(dec(A)),fib(dec(dec(A))))))\n";
		Tokenizer tok = new SimpleTokenizer(str2);
		Functions funs = Exp.parseFunctions(tok);
		
		System.out.println(funs.show());
		
		showandeval(funs, "add(inc(0), inc(0))");
		showandeval(funs, "fib(inc(inc(0)))");
		showandeval(funs, "fib(inc(inc(inc(0))))");
		showandeval(funs, "fib(inc(inc(inc(inc(0)))))");
		showandeval(funs, "fib(inc(inc(inc(inc(inc(0))))))");	
	}

	private static void showandeval(Functions funs, String estr) {
		Subs subs = new Subs();
		Tokenizer tok2 = new SimpleTokenizer(estr);
		Exp e = Exp.parseExp(tok2);
		System.out.println(e.show() + " => " + e.evaluate(subs, funs));
	}
}
