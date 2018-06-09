public abstract class Expression {
    public abstract String show();
    public abstract int evaluate();
//    public abstract Expression simplify();

    public static Expression mult(Expression e1,Expression e2){
        return new Mult(e1,e2);
    }
    public static Expression plus(Expression e1,Expression e2){
        return new Plus(e1,e2);
    }
    public static Expression minus(Expression e1,Expression e2){
        return new Minus(e1,e2);
    }
    public static Expression div(Expression e1,Expression e2){
        return new Div(e1,e2);
    }
    public static Expression lit(int value){
        return new Lit(value);
    }
////    public static Expression mult(Expression e1,Expression e2){
////        return new Mult(e1,e2);
//    }
//<exp> ::= <integer lit> | ( <exp> + <exp> ) | ( <exp> - <exp> ) | ( <exp> / <exp> ) | ( <exp> * <exp> ) | - <exp>

    public static  Expression parseExp(Tokenizer tokenizer){
        if (tokenizer.current().type == TokenType.INTLIT){
            Token t = tokenizer.current();
            tokenizer.next();
            return lit(t.value);
        }else if(tokenizer.current().type == TokenType.MINUS){
            tokenizer.next();
            Token t = tokenizer.current();
            tokenizer.next();
            return lit(-1*t.value);
        } else if(tokenizer.current().type == TokenType.LBRA){
            tokenizer.next();
            Expression left = parseExp(tokenizer);
            Token symbol = tokenizer.current();
            tokenizer.next();
            Expression right = parseExp(tokenizer);
            tokenizer.next();
            switch (symbol.type){
                case PLUS:
                    return plus(left, right);
                case MINUS:
                    return minus(left, right);
                case MULT:
                    return mult(left, right);
                case DIV:
                    return div(left, right);
            }
        }
        return null;
    }
}
