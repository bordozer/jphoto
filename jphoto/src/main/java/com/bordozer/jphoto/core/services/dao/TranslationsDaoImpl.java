package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.admin.controllers.translator.custom.TranslationEntryType;
import com.bordozer.jphoto.core.services.translator.Language;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class TranslationsDaoImpl extends BaseDaoImpl implements TranslationsDao {

    public final static String TABLE_TRANSLATIONS = "translations";

    public final static String TABLE_TRANSLATIONS_COL_ENTRY_TYPE_ID = "entryTypeId";
    public final static String TABLE_TRANSLATIONS_COL_ENTRY_ID = "entryId";
    public final static String TABLE_TRANSLATIONS_COL_LANGUAGE_ID = "languageId";
    public final static String TABLE_TRANSLATIONS_COL_TRANSLATION = "translation";

    @Override
    public String translateCustom(final TranslationEntryType entryType, final int entryId, final Language language) {
        final String sql = String.format("SELECT %s FROM %s WHERE %s=:entryTypeId AND %s=:entryId AND %s=:languageId"
                , TABLE_TRANSLATIONS_COL_TRANSLATION
                , TABLE_TRANSLATIONS
                , TABLE_TRANSLATIONS_COL_ENTRY_TYPE_ID
                , TABLE_TRANSLATIONS_COL_ENTRY_ID
                , TABLE_TRANSLATIONS_COL_LANGUAGE_ID
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("entryTypeId", entryType.getId());
        paramSource.addValue("entryId", entryId);
        paramSource.addValue("languageId", language.getId());

        final String translation = getEntryOrNull(sql, paramSource, new SingleColumnRowMapper<String>());

        if (StringUtils.isEmpty(translation)) {
            //			return String.format( "%s %d %s - no custom translation", entryType, entryId, language );
            return "";
        }

        return translation;
    }

    @Override
    public boolean save(final TranslationEntryType entryType, final int entryId, final Language language, final String translation) {
        final String existSql = String.format("SELECT 1 FROM %s WHERE %s=:entryTypeId AND %s=:entryId AND %s=:languageId;"
                , TABLE_TRANSLATIONS
                , TABLE_TRANSLATIONS_COL_ENTRY_TYPE_ID
                , TABLE_TRANSLATIONS_COL_ENTRY_ID
                , TABLE_TRANSLATIONS_COL_LANGUAGE_ID
        );

        final MapSqlParameterSource existsParamSource = new MapSqlParameterSource();
        existsParamSource.addValue("entryTypeId", entryType.getId());
        existsParamSource.addValue("entryId", entryId);
        existsParamSource.addValue("languageId", language.getId());

        final String sql;
        if (existsInt(existSql, existsParamSource)) {
            sql = String.format("UPDATE %s SET %s=:translation WHERE %s=:entryTypeId AND %s=:entryId AND %s=:languageId;"
                    , TABLE_TRANSLATIONS
                    , TABLE_TRANSLATIONS_COL_TRANSLATION
                    , TABLE_TRANSLATIONS_COL_ENTRY_TYPE_ID
                    , TABLE_TRANSLATIONS_COL_ENTRY_ID
                    , TABLE_TRANSLATIONS_COL_LANGUAGE_ID
            );
        } else {
            sql = String.format("INSERT INTO %s ( %s, %s, %s, %s ) VALUES( :entryTypeId, :entryId, :languageId, :translation );"
                    , TABLE_TRANSLATIONS
                    , TABLE_TRANSLATIONS_COL_ENTRY_TYPE_ID
                    , TABLE_TRANSLATIONS_COL_ENTRY_ID
                    , TABLE_TRANSLATIONS_COL_LANGUAGE_ID
                    , TABLE_TRANSLATIONS_COL_TRANSLATION
            );
        }

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("entryTypeId", entryType.getId());
        paramSource.addValue("entryId", entryId);
        paramSource.addValue("languageId", language.getId());
        paramSource.addValue("translation", translation);

        return jdbcTemplate.update(sql, paramSource) > 0;
    }
}

