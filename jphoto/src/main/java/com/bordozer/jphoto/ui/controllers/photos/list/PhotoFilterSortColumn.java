package com.bordozer.jphoto.ui.controllers.photos.list;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;

public enum PhotoFilterSortColumn implements IdentifiableNameable {

    POSTING_TIME(1, "PhotoFilterSortColumn: Posing time"), COMMENTS_COUNT(2, "PhotoFilterSortColumn: Count of comment"), VIEWS_COUNT(3, "PhotoFilterSortColumn: Count of views"), RATING(4, "PhotoFilterSortColumn: Rating");

    private final int id;
    private final String name;

    private PhotoFilterSortColumn(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public static PhotoFilterSortColumn getById(final int id) {
        for (final PhotoFilterSortColumn sortOrder : PhotoFilterSortColumn.values()) {
            if (sortOrder.getId() == id) {
                return sortOrder;
            }
        }

        throw new IllegalArgumentException(String.format("Illegal PhotoFilterSortColumn: %d", id));
    }
}
