package admin.jobs;

import core.services.translator.Language;

public class JobRuntimeEnvironment {

	private final Language language;

	public JobRuntimeEnvironment( final Language language ) {
		this.language = language;
	}

	public Language getLanguage() {
		return language;
	}
}
