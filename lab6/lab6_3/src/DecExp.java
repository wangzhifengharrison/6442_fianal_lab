
/*
 * DecExp 
 * Eric McCreath 2017
 */


public class DecExp extends Exp {
	Exp exp;
	
	@Override
	public int evaluate(Subs subs, Functions funs) {
		return exp.evaluate(subs,funs) - 1;
	}

	@Override
	public String show() {
		return "dec(" + exp.show() + ")";
	}

	public DecExp(Exp exp) {
		super();
		this.exp = exp;
	}
}
