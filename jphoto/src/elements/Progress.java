package elements;

public class Progress {

	private int current;
	private int total;

	public Progress( int current, int total ) {
		this.current = current;
		this.total = total;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal( int total ) {
		this.total = total;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent( int current ) {
		this.current = current;
	}
}
