package com.bordozer.jphoto.core.enums;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;

public enum PhotoActionAllowance implements IdentifiableNameable {

    ACTIONS_DENIED(1, "PhotoActionAllowance: Not allowed"), CANDIDATES_AND_MEMBERS(2, "PhotoActionAllowance: Candidates and members"), MEMBERS_ONLY(3, "PhotoActionAllowance: Members only");

    private final int id;
    private final String name;

    private PhotoActionAllowance(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PhotoActionAllowance getById(final int id) {
        for (final PhotoActionAllowance commentsAllowance : PhotoActionAllowance.values()) {
            if (commentsAllowance.getId() == id) {
                return commentsAllowance;
            }
        }

        throw new IllegalArgumentException(String.format("Illegal PhotoCommentsAllowance id: %d", id));
    }
}
