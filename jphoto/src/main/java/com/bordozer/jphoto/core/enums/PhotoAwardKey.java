package com.bordozer.jphoto.core.enums;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;

public enum PhotoAwardKey implements IdentifiableNameable {

    PHOTO_OF_THE_DAY(1, "PhotoAwardKey: The photo of the day"), TOP_3_OF_THE_DAY(2, "PhotoAwardKey: Top 3 of the day"), TOP_10_OF_THE_DAY(3, "PhotoAwardKey: Top 10 of the day"), TOP_50_OF_THE_DAY(4, "PhotoAwardKey: Top 50 of the day")
    //	, TOP_200_WEEK( 5, "Top 200 of the week" )
    ;

    private final int id;
    private final String name;

    private PhotoAwardKey(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PhotoAwardKey getById(final int id) {
        for (final PhotoAwardKey upgradeTaskResult : PhotoAwardKey.values()) {
            if (upgradeTaskResult.getId() == id) {
                return upgradeTaskResult;
            }
        }

        throw new IllegalArgumentException(String.format("Illegal PhotoAwardKey id: %d", id));
    }
}
