package core.services.dao;

import core.services.translator.Language;

public interface TranslationsDao {

	String translate( String nerd, Language language );
}
