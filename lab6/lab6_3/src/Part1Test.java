/*
 * Part1Test
 * Eric McCreath 2017
 */

public class Part1Test {

	
	public static void main(String[] args) {
	   Tokenizer tok = new SimpleTokenizer("   (0 ? inc(inc(0)):  dec(0)  ) ");
	   checktok(tok, "(");
	   checktok(tok, new Integer(0));
	   checktok(tok, "?");
	   checktok(tok, "inc");
	   checktok(tok, "(");
	   checktok(tok, "inc");
	   checktok(tok, "(");
	   checktok(tok, new Integer(0));
	   checktok(tok, ")");
	   checktok(tok, ")");
	   checktok(tok, ":");
	   checktok(tok, "dec");
	   checktok(tok, "(");
	   checktok(tok, new Integer(0));
	   checktok(tok, ")");
	   checktok(tok, ")");
	   System.out.println(tok.hasNext()? "Problem!!! should be at end.": "okay");  
	   
	   
	}

	private static void checktok(Tokenizer tok, Object expected) {
		if (tok.current() != null && tok.current().equals(expected)) {
			System.out.println("ok " + expected + " as expected." );
		} else {
			System.out.println("Problem!!!!! Expected " + expected + " but was " + tok.current());
		}
		tok.next();
	}

}
