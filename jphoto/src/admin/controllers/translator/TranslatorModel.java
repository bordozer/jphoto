package admin.controllers.translator;

import core.general.base.AbstractGeneralModel;
import core.services.translator.NerdKey;
import core.services.translator.TranslationData;

import java.util.List;
import java.util.Map;

public class TranslatorModel extends AbstractGeneralModel {

	private Map<NerdKey, TranslationData> translationsMap;
	private List<String> letters;

	private String urlPrefix;

	private TranslationMode translationMode;
	private String filterByLetter;

	public void setTranslationsMap( final Map<NerdKey,TranslationData> translationsMap ) {
		this.translationsMap = translationsMap;
	}

	public Map<NerdKey, TranslationData> getTranslationsMap() {
		return translationsMap;
	}

	public List<String> getLetters() {
		return letters;
	}

	public void setLetters( final List<String> letters ) {
		this.letters = letters;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix( final String urlPrefix ) {
		this.urlPrefix = urlPrefix;
	}

	public String getFilterByLetter() {
		return filterByLetter;
	}

	public void setFilterByLetter( final String filterByLetter ) {
		this.filterByLetter = filterByLetter;
	}

	public TranslationMode getTranslationMode() {
		return translationMode;
	}

	public void setTranslationMode( final TranslationMode translationMode ) {
		this.translationMode = translationMode;
	}
}
