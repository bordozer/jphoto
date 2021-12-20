package com.bordozer.jphoto.core.general.executiontasks;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;

public enum PeriodUnit implements IdentifiableNameable {

    SECOND(1, "PeriodUnit: second"), MINUTE(2, "PeriodUnit: minute"), HOUR(3, "PeriodUnit: hour")
    //	, DAY( 4, "PeriodUnit: day" )
    ;

    private final int id;
    private final String name;

    private PeriodUnit(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PeriodUnit getById(final int id) {
        for (final PeriodUnit configurationDataType : PeriodUnit.values()) {
            if (configurationDataType.getId() == id) {
                return configurationDataType;
            }
        }
        return null;
    }
}
