package com.bordozer.jphoto.core.enums;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;

public enum UserGender implements IdentifiableNameable {

    MALE(1, "UserGender: male"), FEMALE(2, "UserGender: female");

    private final int id;
    private final String name;

    private UserGender(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static UserGender getById(final int id) {
        for (final UserGender upgradeTaskResult : UserGender.values()) {
            if (upgradeTaskResult.getId() == id) {
                return upgradeTaskResult;
            }
        }

        throw new IllegalArgumentException(String.format("Illegal UserGender id: %d", id));
    }
}
