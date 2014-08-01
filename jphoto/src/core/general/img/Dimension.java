package core.general.img;

public class Dimension {

	private final int width;
	private final int height;

	public Dimension( final int width, final int height ) {
		this.height = height;
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public int hashCode() {
		return width * height;
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( ! ( obj instanceof Dimension ) ) {
			return false;
		}

		final Dimension dimension = ( Dimension ) obj;
		return dimension.getWidth() == width && dimension.getHeight() == height;
	}

	@Override
	public String toString() {
		return String.format( "%d x %d", width, height );
	}
}
