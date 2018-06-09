class Token {
    TokenType type;
    Integer value;

    public Token(TokenType type, Integer v) {
        this.type = type;
        this.value = v;
    }

    @Override
    public String toString() {
        return type.toString() + " " + (value!=null?value:"");
    }
}