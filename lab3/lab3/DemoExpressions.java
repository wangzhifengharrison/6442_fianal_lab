package lab3;

public class DemoExpressions {

	public static void main(String[] args) {
			Expression exp1 = new Lit(5);
			System.out.println(exp1.show() + " evaluates to " + exp1.evaluate());
			Expression exp2 = new Add(new Lit(1), new Lit(1));
			System.out.println(exp2.show() + " evaluates to " + exp2.evaluate());
			Expression exp3 = new Mult(new Lit(-1), new Sub(new Lit(3), new Lit(-2)));
			System.out.println(exp3.show() + " evaluates to " + exp3.evaluate());
			Expression exp4 = new Div(new Lit(7), new Minus(new Lit(3)));
			System.out.println(exp4.show() + " evaluates to " + exp4.evaluate());

	}

}
