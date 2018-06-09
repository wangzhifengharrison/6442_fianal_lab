
/*
 * IncExp
 * Eric McCreath 2017
 */

public class IncExp extends Exp {
	Exp exp;
	
	public IncExp(Exp exp) {
		super();
		this.exp = exp;
	}

	@Override
	public int evaluate(Subs subs, Functions funs) {
		return exp.evaluate(subs,funs) + 1;
	}

	@Override
	public String show() {
		return "inc(" + exp.show() + ")";
	}
}
