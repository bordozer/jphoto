package admin.controllers.translator;

import core.general.base.AbstractGeneralModel;
import core.services.translator.NerdKey;
import core.services.translator.TranslationData;

import java.util.Map;
import java.util.Set;

public class TranslatorModel extends AbstractGeneralModel {

	private Map<NerdKey, TranslationData> translationsMap;
	private Set<String> letters;

	private String urlPrefix;

	public void setTranslationsMap( final Map<NerdKey,TranslationData> translationsMap ) {
		this.translationsMap = translationsMap;
	}

	public Map<NerdKey, TranslationData> getTranslationsMap() {
		return translationsMap;
	}

	public Set<String> getLetters() {
		return letters;
	}

	public void setLetters( final Set<String> letters ) {
		this.letters = letters;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix( final String urlPrefix ) {
		this.urlPrefix = urlPrefix;
	}
}
