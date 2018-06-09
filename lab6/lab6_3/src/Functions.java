import java.util.HashMap;

/*
 * Functions 
 * Eric McCreath 2017
 */
public class Functions extends HashMap<String, Function> {

	public String show() {
		String res = "";
		for (String fname : this.keySet()) {
			Function f = this.get(fname);
			if (f != null)
			res += f.show() + "\n";
		}
		return res;
	}
	@Override
	public String toString() {
		
		return show();
	}

}
