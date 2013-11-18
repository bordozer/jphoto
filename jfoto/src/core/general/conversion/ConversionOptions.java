package core.general.conversion;

import java.awt.*;

public class ConversionOptions {

	public static final int DEFAULT_DENSITY = 72;
	public static final int DEFAULT_PREVIEW_SIZE = 200;

	public static final ConversionOptions DEFAULT_PREVIEW_OPTION = new ConversionOptions() {
		@Override
		public int getDensity() {
			return DEFAULT_DENSITY;
		}

		@Override
		public Dimension getDimension() {
			return new Dimension( DEFAULT_PREVIEW_SIZE, DEFAULT_PREVIEW_SIZE );
		}
	};

	private int density;
	private Dimension dimension;

	public int getDensity() {
		return density;
	}

	public void setDensity( int density ) {
		this.density = density;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension( Dimension dimension ) {
		this.dimension = dimension;
	}
}
