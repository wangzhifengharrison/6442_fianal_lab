
/*
 * VarExp
 * Eric McCreath 2017
 */

public class VarExp extends Exp {

	String var;
	
	@Override
	public int evaluate(Subs subs, Functions funs) {
		Integer res = subs.get(var);
		return  (res == null? 0:res) ;
	}

	@Override
	public String show() {
		return var;
	}

	public VarExp(String var) {
		super();
		this.var = var;
	}

}
