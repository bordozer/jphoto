package com.bordozer.jphoto.admin.controllers.restriction.entry;

public enum RestrictionEntryType {

    USER(1, "RestrictionEntryType: user"), PHOTO(2, "RestrictionEntryType: photo");

    private final int id;
    private final String name;

    RestrictionEntryType(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
