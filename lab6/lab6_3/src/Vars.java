import java.util.ArrayList;

/*
 * Vars
 */

public class Vars extends ArrayList<String> {

	public Vars() {
		
	}
	
	public Vars(String v1) {
		add(v1);
	}

	
	public Vars(String v1, String v2) {
		add(v1);
		add(v2);
	}


	public String show() {
		String res = "";
		for (int i= 0;i<size();i++) {
			res += this.get(i) + (i < size()-1? "," :"");
		}
	 return res;
	}

}
