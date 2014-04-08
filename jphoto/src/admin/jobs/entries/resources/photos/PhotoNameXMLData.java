package admin.jobs.entries.resources.photos;

import java.util.List;

public class PhotoNameXMLData {

	private String key;
	private List<String> values;

	public PhotoNameXMLData( final String key, final List<String> values ) {

		this.key = key;
		this.values = values;
	}

	public String getKey() {
		return key;
	}

	public List<String> getValues() {
		return values;
	}

	@Override
	public String toString() {
		return String.format( "%s: %d", key, values.size() );
	}
}
