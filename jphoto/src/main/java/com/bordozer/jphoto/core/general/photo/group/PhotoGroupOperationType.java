package com.bordozer.jphoto.core.general.photo.group;

public enum PhotoGroupOperationType {
    ARRANGE_PHOTO_ALBUMS(1, "PhotoGroupOperationType: Arrange photo albums"), ARRANGE_TEAM_MEMBERS(4, "PhotoGroupOperationType: Arrange team members"), DELETE_PHOTOS(3, "PhotoGroupOperationType: Delete photos"), ARRANGE_NUDE_CONTENT(5, "PhotoGroupOperationType: Set nude content"), MOVE_TO_GENRE(6, "PhotoGroupOperationType: Move to category"), SEPARATOR(-1, "- - - - - - - - - - - - - - - - - - - - - - -");

    private final int id;
    private final String name;

    private PhotoGroupOperationType(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PhotoGroupOperationType getById(final int id) {
        for (final PhotoGroupOperationType groupOperationType : PhotoGroupOperationType.values()) {
            if (id != -1 && groupOperationType.getId() == id) {
                return groupOperationType;
            }
        }

        throw new IllegalArgumentException(String.format("Illegal PhotoGroupOperation id: %d", id));
    }
}
