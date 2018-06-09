import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

/**
 * Tokenizer - this uses the StreamTokenizer class to make a simpler tokenizer
 * which provides a stream of tokens which are either Integer, Double, or
 * String.
 * 
 * @author Eric McCreath
 * 
 */

public abstract class Tokenizer {

	abstract boolean hasNext();

	abstract Object current();

	abstract void next();

	public void parse(Object o)  {
		if (current() == null || !current().equals(o))
			throw new Error(); // normally one would use exceptions in such cases, however, 
		                       // it just makes code simpler as you don't need to deal with the exception 
		                       // in calling code
		next();
	}
}
