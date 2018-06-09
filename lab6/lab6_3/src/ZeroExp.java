
/*
 * ZeroExp
 */

public class ZeroExp extends Exp {

	@Override
	public int evaluate(Subs subs, Functions funs) {
		return 0;
	}

	@Override
	public String show() {
		return "0";
	}

}
