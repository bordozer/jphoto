package com.bordozer.jphoto.core.general.photo;

public enum PhotoImageLocationType {

    FILE(1, "PhotoImageSourceType: file"), WEB(2, "PhotoImageSourceType: web");

    private final int id;
    private final String description;

    PhotoImageLocationType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static PhotoImageLocationType getById(int id) {
        for (PhotoImageLocationType importSource : PhotoImageLocationType.values()) {
            if (importSource.getId() == id) {
                return importSource;
            }
        }

        throw new IllegalArgumentException(String.format("Illegal PhotoImageSourceType: %d", id));
    }
}
