
/*
 * DemoFunctions 
 * Eric McCreath 2017
 */

public class DemoFunctions {

	
	
	
	public static void main(String[] args) {
		Subs subs = new Subs();
		Functions funs = new Functions();
	    evaluateAndShow(subs, funs, new IncExp(new IncExp(new ZeroExp())));
	    funs.put("add", new Function("add", new Vars("A", "B"), new SelectExp(new VarExp("A"), new VarExp("B"), new FunCallExp("add", new DecExp(new VarExp("A")), new IncExp(new VarExp("B")))) ));
	    funs.put("sub", new Function("sub", new Vars("A", "B"), new SelectExp(new VarExp("B"), new VarExp("A"), new FunCallExp("sub", new DecExp(new VarExp("A")), new DecExp(new VarExp("B")))) ));
	    funs.put("fib", new Function("fib", new Vars("A"), new SelectExp(new VarExp("A"), new ZeroExp(),      
	    		new SelectExp( new FunCallExp("sub",new VarExp("A"),new IncExp(new ZeroExp())), new IncExp(new ZeroExp()),
	    		new FunCallExp("add", new FunCallExp("fib",new DecExp(new VarExp("A"))), new FunCallExp("fib",new DecExp(new DecExp(new VarExp("A")))))))));
	  
	    System.out.println(funs.show());
	    
	    evaluateAndShow(subs, funs, new FunCallExp("add", new IncExp(new IncExp(new ZeroExp())), new IncExp(new IncExp(new ZeroExp()))));
	    evaluateAndShow(subs, funs, new FunCallExp("fib", new IncExp(new IncExp(new ZeroExp()))));
	    evaluateAndShow(subs, funs, new FunCallExp("fib",  new IncExp( new IncExp(new IncExp(new ZeroExp())))));
	    evaluateAndShow(subs, funs, new FunCallExp("fib",   new IncExp(new IncExp( new IncExp(new IncExp(new ZeroExp()))))));
	    evaluateAndShow(subs, funs, new FunCallExp("fib",   new IncExp( new IncExp(new IncExp( new IncExp(new IncExp(new ZeroExp())))))));
	    evaluateAndShow(subs, funs, new FunCallExp("fib",    new IncExp( new IncExp( new IncExp(new IncExp( new IncExp(new IncExp(new ZeroExp()))))))));
	}

	private static void evaluateAndShow(Subs subs, Functions funs, Exp exp1) {
		System.out.println(exp1.show() + " => " + exp1.evaluate(subs, funs));
	}

}
