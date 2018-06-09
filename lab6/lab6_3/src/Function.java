
/*
 * Function 
 * Eric McCreath 2017
 */

public class Function {
	String name;
	Vars vars;
	Exp exp;
	
	public Function(String name, Vars vars, Exp exp) {
		super();
		this.name = name;
		this.vars = vars;
		this.exp = exp;
	}

	public String show() {
		return name + "(" + vars.show() + ") = " + exp.show() ;
	}
	
	@Override
	public String toString() {
		return show();
	}
}
