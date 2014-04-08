package admin.jobs.entries.resources.photos;

import java.util.List;

public class CaseSensitiveData {

	private String caseName;
	private List<String> values;

	public CaseSensitiveData( final String caseName, final List<String> values ) {

		this.caseName = caseName;
		this.values = values;
	}

	public String getCaseName() {
		return caseName;
	}

	public List<String> getValues() {
		return values;
	}

	@Override
	public String toString() {
		return String.format( "%s: %d", caseName, values.size() );
	}
}
