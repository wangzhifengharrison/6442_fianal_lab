
/*
 * SelectExp
 * Eric McCreath 2017
 */

public class SelectExp extends Exp {

	Exp test, exp1, exp2;
	
	
	public SelectExp(Exp test, Exp exp1, Exp exp2) {
		super();
		this.test = test;
		this.exp1 = exp1;
		this.exp2 = exp2;
	}

	@Override
	public int evaluate(Subs subs, Functions funs) {
		
		return (test.evaluate(subs, funs) == 0? exp1.evaluate(subs, funs):exp2.evaluate(subs, funs) );
	}

	@Override
	public String show() {
		return "(" + test.show() + "?" + exp1.show() + ":" + exp2.show() + ")";
	}

}
 