package com.bordozer.jphoto.admin.services.services;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

public interface SqlUtilsService {

    public void execSQL(final String sql);

    public void renameTable(final String tableToRename, final String newName);

    public void renameColumn(final String table, final String columnToRename, final String columnType, final String newName);

    <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper);
}
