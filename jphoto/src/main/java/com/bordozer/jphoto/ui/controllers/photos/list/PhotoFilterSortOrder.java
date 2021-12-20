package com.bordozer.jphoto.ui.controllers.photos.list;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;
import com.bordozer.jphoto.sql.builder.SqlSortOrder;

public enum PhotoFilterSortOrder implements IdentifiableNameable {

    ASC(1, "PhotoFilterSortOrder: Ascending", SqlSortOrder.ASC), DESC(2, "PhotoFilterSortOrder: Descending", SqlSortOrder.DESC);

    private final int id;
    private final String name;
    private final SqlSortOrder sortOrder;

    private PhotoFilterSortOrder(final int id, final String name, final SqlSortOrder sortOrder) {
        this.id = id;
        this.name = name;
        this.sortOrder = sortOrder;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public SqlSortOrder getSortOrder() {
        return sortOrder;
    }

    public static PhotoFilterSortOrder getById(final int id) {
        for (final PhotoFilterSortOrder sortOrder : PhotoFilterSortOrder.values()) {
            if (sortOrder.getId() == id) {
                return sortOrder;
            }
        }

        throw new IllegalArgumentException(String.format("Illegal PhotoFilterSortOrder: %d", id));
    }
}
