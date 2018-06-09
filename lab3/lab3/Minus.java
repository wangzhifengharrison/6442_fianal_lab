package lab3;

public class Minus extends Expression {

	Expression value;
	
	public Minus(Expression v) {
		value = v;
	}
	@Override
	public String show() {
		return "(" + "-" + "(" + value.show() + ")" + ")";
	}

	@Override
	public int evaluate() {
		return -value.evaluate();
	}

}
