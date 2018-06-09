public class SimpleTokenizer extends Tokenizer{
    String s;
    int pos = 0;
    Token current;

    public SimpleTokenizer(String s){
        this.s = s.replace(" ", "");
        next();
    }

    @Override
    boolean hasNext() {
        return this.current != null;
    }

    @Override
    Token current() {
        return this.current;
    }

    @Override
    void next() {
        if (finished()){
            current = null;
        }else if (isSymbol()){
            current = getSymbol(s.charAt(pos));
            pos++;
        }else if (isDigit()){
            for (int i = s.length(); i > pos; i--){
                String sub = s.substring(pos, i);
                if (isInteger(sub)){
                    current = new Token(TokenType.INTLIT, Integer.parseInt(sub));
                    pos = i;
                }
            }
        }
    }

    private Token getSymbol(char c) {
        switch (c){
            case '+':
                return new Token(TokenType.PLUS, null);
            case '-':
                return new Token(TokenType.MINUS, null);
            case '*':
                return new Token(TokenType.MULT, null);
            case '/':
                return new Token(TokenType.DIV, null);
            case '(':
                return new Token(TokenType.LBRA, null);
            case ')':
                return new Token(TokenType.RBRA, null);
        }
        return null;
    }

    private boolean isInteger(String sub) {
        try {
            Integer.parseInt(sub);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    private boolean isDigit() {
        return Character.isDigit(s.charAt(pos));
    }

    private boolean isSymbol() {
        return "+-*/()".indexOf(s.charAt(pos)) != -1;
    }

    private boolean finished() {
        return pos == s.length();
    }
}
