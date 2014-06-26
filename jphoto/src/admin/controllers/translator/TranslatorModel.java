package admin.controllers.translator;

import core.general.base.AbstractGeneralModel;
import core.services.translator.Language;
import core.services.translator.NerdKey;
import core.services.translator.TranslationData;

import java.util.List;
import java.util.Map;

public class TranslatorModel extends AbstractGeneralModel {

	private Map<NerdKey, TranslationData> translationsMap;
	private List<String> letters;

	private TranslationMode translationMode;
	private String filterByLetter;
	private Language language;

	private Map<TranslationMode, Integer> modeRecordsCount;

	public TranslatorModel filter( final Language language ) {
		this.language = language;
		return this;
	}

	public TranslatorModel filter( final String letter ) {
		this.filterByLetter = letter;
		return this;
	}

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

	public Language getLanguage() {
		return language;
	}

	public void setLanguage( final Language language ) {
		this.language = language;
	}

	public Map<TranslationMode, Integer> getModeRecordsCount() {
		return modeRecordsCount;
	}

	public void setModeRecordsCount( final Map<TranslationMode, Integer> modeRecordsCount ) {
		this.modeRecordsCount = modeRecordsCount;
	}
}
