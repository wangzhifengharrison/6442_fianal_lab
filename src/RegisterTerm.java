
public class RegisterTerm implements Register {

	private int value;
	
	public RegisterTerm() {
		value = 0;
	}
	
	@Override
	public void set(int v) {
		value = v;
	}

	@Override
	public void reset() {
		value = 0;
	}

	@Override
	public int get() {
		return value;
	}

	@Override
	public void updateWord1(int style){

	};

}
