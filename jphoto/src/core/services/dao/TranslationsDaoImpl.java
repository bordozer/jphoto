package core.services.dao;

import core.services.translator.Language;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class TranslationsDaoImpl extends BaseDaoImpl implements TranslationsDao {

	public final static String TABLE_TRANSLATIONS = "translations";

	public final static String TABLE_TRANSLATIONS_COL_NERD = "nerd";
	public final static String TABLE_TRANSLATIONS_COL_LANGUAGE_ID = "languageId";
	public final static String TABLE_TRANSLATIONS_COL_TRANSLATION = "translations";

	@Override
	public String translate( final String nerd, final Language language ) {
		final String sql = String.format( "SELECT %s FROM %s WHERE %s=:nerd AND %s=:languageId"
			, TABLE_TRANSLATIONS_COL_TRANSLATION
			, TABLE_TRANSLATIONS
			, TABLE_TRANSLATIONS_COL_NERD
			, TABLE_TRANSLATIONS_COL_LANGUAGE_ID
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "nerd", nerd );
		paramSource.addValue( "languageId", language.getId() );

		return getEntryOrNull( sql, paramSource, new SingleColumnRowMapper<String>() );
	}
}
