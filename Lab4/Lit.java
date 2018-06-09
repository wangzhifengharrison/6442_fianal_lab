public class Lit extends Expression {
    int value;

    public Lit(int v) {
        value = v;
    }
    @Override
    public String show() {
        return "" + value;
    }

    @Override
    public int evaluate() {
        return value;
    }

}

