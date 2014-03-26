package admin.controllers.genres.translations;

import core.general.base.AbstractGeneralModel;
import core.general.genre.Genre;
import core.services.translator.Language;
import core.services.translator.TranslationEntry;

import java.util.List;
import java.util.Map;

public class GenresTranslationsModel extends AbstractGeneralModel {

	private int selectedLanguageId;
	private int selectedLanguageIdOld;

	private Map<Integer, GenreTranslationEntry> translationEntriesMap;
	private Map<IdLanguageKey, GenreTranslationEntry> allTranslationEntriesMap;

	private Map<Integer, Genre> genreMap;
	private List<Language> languages;

	public int getSelectedLanguageId() {
		return selectedLanguageId;
	}

	public void setSelectedLanguageId( final int selectedLanguageId ) {
		this.selectedLanguageId = selectedLanguageId;
	}

	public int getSelectedLanguageIdOld() {
		return selectedLanguageIdOld;
	}

	public void setSelectedLanguageIdOld( final int selectedLanguageIdOld ) {
		this.selectedLanguageIdOld = selectedLanguageIdOld;
	}

	public Map<Integer, GenreTranslationEntry> getTranslationEntriesMap() {
		return translationEntriesMap;
	}

	public void setTranslationEntriesMap( final Map<Integer, GenreTranslationEntry> translationEntriesMap ) {
		this.translationEntriesMap = translationEntriesMap;
	}

	public Map<IdLanguageKey, GenreTranslationEntry> getAllTranslationEntriesMap() {
		return allTranslationEntriesMap;
	}

	public void setAllTranslationEntriesMap( final Map<IdLanguageKey, GenreTranslationEntry> allTranslationEntriesMap ) {
		this.allTranslationEntriesMap = allTranslationEntriesMap;
	}

	public List<Language> getLanguages() {
		return languages;
	}

	public void setLanguages( final List<Language> languages ) {
		this.languages = languages;
	}

	public Map<Integer, Genre> getGenreMap() {
		return genreMap;
	}

	public void setGenreMap( final Map<Integer, Genre> genreMap ) {
		this.genreMap = genreMap;
	}
}
