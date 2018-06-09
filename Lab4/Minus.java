public class Minus extends Expression{
    Expression left, right;

    public Minus(Expression l, Expression r) {
        left = l;
        right = r;
    }
    @Override
    public String show() {

        return "("+ left.show() + " - " + right.show() + ")";
    }

    @Override
    public int evaluate() {

        return left.evaluate() - right.evaluate();
    }
}
