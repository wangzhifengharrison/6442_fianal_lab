import java.util.ArrayList;
import java.util.HashMap;

/*
 * Exp 
 * Eric McCreath 2017
 */

public abstract class Exp {
	public abstract int evaluate(Subs subs, Functions funs);

	public abstract String show();


	static public Exp parseExp(Tokenizer tok) {
		if(tok.current() instanceof Integer && (int)tok.current() == 0){
			tok.next();
			return new ZeroExp();
		}
		else if(tok.current() instanceof String && tok.current().equals("inc")){
			tok.next();
			tok.parse("(");
			Exp exp = parseExp(tok);
			tok.parse(")");
			return new IncExp(exp);
		}
		else if(tok.current() instanceof String && tok.current().equals("dec")){
			tok.next();
			tok.parse("(");
			Exp exp = parseExp(tok);
			tok.parse(")");
			return new DecExp(exp);
		}
		else if(tok.current() instanceof String && String.valueOf(tok.current()).length() == 1 &&
				String.valueOf(tok.current()).charAt(0) >= 'A' && String.valueOf(tok.current()).charAt(0) <= 'Z'){
			String v = String.valueOf(tok.current());
			tok.next();
			return new VarExp(v);
		}
		else if(tok.current() instanceof String && String.valueOf(tok.current()).length() > 1){
			String name = String.valueOf(tok.current());
			tok.next();
			tok.parse("(");
			ArrayList<Exp> exps = parseExps(tok);
			tok.parse(")");
			return new FunCallExp(name, exps);
		}else if(tok.current() instanceof String && String.valueOf(tok.current()).equals("(")){
			tok.next();
			Exp exp = parseExp(tok);
			tok.parse("?");
			Exp exp1 = parseExp(tok);
			tok.parse(":");
			Exp exp2 = parseExp(tok);
			tok.parse(")");
			return new SelectExp(exp, exp1, exp2);
		}




	
		// add your code here
		return null;
	}

	private static ArrayList<Exp> parseExps(Tokenizer tok) {
		Exp exp = parseExp(tok);
		if(String.valueOf(tok.current()).equals(",")){
			tok.next();
			ArrayList<Exp> exps = parseExps(tok);
			exps.add(0, exp);
			return exps;
		}else{
			ArrayList<Exp> exps = new ArrayList<Exp>();
			exps.add(exp);
			return exps;
		}
		// add your code here
	}

	private static Function parseFunction(Tokenizer tok) {
		String name = String.valueOf(tok.current());
		tok.next();
		tok.parse("(");
		Vars vars = parseVars(tok);
		tok.parse(")");
		tok.parse("=");
		Exp exp = parseExp(tok);
		// add your code here
		return new Function(name, vars, exp);
	}

	private static Vars parseVars(Tokenizer tok) {
		String v = String.valueOf(tok.current());
		tok.next();
		if(String.valueOf(tok.current()).equals(",")){
			tok.parse(",");
			Vars vars = parseVars(tok);
			vars.add(0, v);
			return vars;
		}else{
			Vars vars = new Vars();
			vars.add(0, v);
			return vars;
		}

		// add your code here

	}
	
	
	public static Functions parseFunctions(Tokenizer tok) {
		// add your code here
		Function f = parseFunction(tok);
		if(tok.hasNext()){
			Functions fs = parseFunctions(tok);
			fs.put(f.name, f);
			return fs;
		}else{
			Functions map = new Functions();
			map.put(f.name, f);
			return map;
		}
	}

}
