
public abstract class Tokenizer {

    abstract boolean hasNext();

    abstract Token current();

    abstract void next();

//    public void parse(Object o) throws ParseException {
////        if (current() == null || !current().equals(o))
////            throw new ParseException();
//        next();
//    }
}

