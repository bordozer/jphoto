package admin.controllers.jobs.edit.photosImport.importParameters;

import core.services.translator.Language;

public abstract class AbstractImportParameters {

	protected final Language language;

	public AbstractImportParameters( final Language language ) {
		this.language = language;
	}

	public Language getLanguage() {
		return language;
	}
}
