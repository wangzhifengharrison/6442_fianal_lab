import java.util.ArrayList;

/*
 * FunCallExp 
 * Eric McCreath 2017
 */
public class FunCallExp extends Exp {

	String funname;
	ArrayList<Exp> exps;
	
	public FunCallExp(String name, ArrayList<Exp> es) {
		funname = name;
		exps = es;
		
	}
	
	public FunCallExp(String name, Exp e1) {
		funname = name;
		exps = new ArrayList<Exp>();
		exps.add(e1);
		
	}
	
	public FunCallExp(String name, Exp e1, Exp e2) {
		funname = name;
		exps = new ArrayList<Exp>();
		exps.add(e1);
		exps.add(e2);
	}

	@Override
	public int evaluate(Subs subs, Functions funs) {
		int res[];
		res = new int[exps.size()];
		Function f = funs.get(funname);
		Subs nsubs = new Subs();
		for (int i = 0;i< exps.size();i++) {
			nsubs.put(f.vars.get(i), exps.get(i).evaluate(subs, funs));
		}
		return f.exp.evaluate(nsubs,funs);
	}

	@Override
	public String show() {
		String res =  funname + "(";
		for (int i=0;i< exps.size();i++) {
			res += exps.get(i).show();
			if (i < exps.size()-1) res += ",";
		}
		return res + ")";
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return show();
	}

}
