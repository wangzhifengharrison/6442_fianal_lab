package lab3;

public class Add extends Expression {
	Expression left, right;
	
	public Add(Expression l, Expression r) {
		left = l;
		right = r;
	}
	@Override
	public String show() {
		
		return "("+ left.show() + " + " + right.show() + ")";
	}

	@Override
	public int evaluate() {
		
		return left.evaluate() + right.evaluate();
	}

}
