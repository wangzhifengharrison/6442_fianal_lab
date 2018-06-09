public class TestTokenizer {
    static String t1 = "2+2 *-3";

    public static void main(String[] args) {


        Tokenizer test = new SimpleTokenizer(t1);
        while (test.hasNext()) {
            System.out.println(test.current());
            test.next();
        }
    }
}
