

public class TestParser {
    static String t1 = "(2*(4+2))";
    static String t2 = "((2+44)*2)";

    public static void main(String[] args) {
        Tokenizer test = new SimpleTokenizer(t1);
        Expression pe = Expression.parseExp(test);
//        while (test.hasNext()){
//            System.out.println(pe.show()+"="+pe.evaluate());
//        }
        System.out.println(pe.show()+" = " + pe.evaluate());
    }
}
