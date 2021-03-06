package com.bordozer.jphoto.core.enums;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;

public enum RestrictionStatus implements IdentifiableNameable {

    PROGRESS(1, "RestrictionStatus: Progress"), POSTPONED(2, "RestrictionStatus: Postponed"), CANCELLED(3, "RestrictionStatus: Cancelled"), PASSED(4, "RestrictionStatus: Passed");

    private final int id;
    private final String name;

    RestrictionStatus(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static RestrictionStatus getById(final int id) {
        for (final RestrictionStatus restrictionStatus : RestrictionStatus.values()) {
            if (restrictionStatus.getId() == id) {
                return restrictionStatus;
            }
        }

        throw new IllegalArgumentException(String.format("Illegal RestrictionStatus id: %d", id));
    }

    public static RestrictionStatus getById(final String restrictionStatusId) {
        return getById(Integer.parseInt(restrictionStatusId));
    }
}
