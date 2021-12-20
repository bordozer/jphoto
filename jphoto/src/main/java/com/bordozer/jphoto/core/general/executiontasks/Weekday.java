package com.bordozer.jphoto.core.general.executiontasks;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;

import java.util.Calendar;

public enum Weekday implements IdentifiableNameable {

    MONDAY(Calendar.MONDAY, "Weekday: monday", "MON"), TUESDAY(Calendar.TUESDAY, "Weekday: tuesday", "TUE"), WEDNESDAY(Calendar.WEDNESDAY, "Weekday: wednesday", "WED"), THURSDAY(Calendar.THURSDAY, "Weekday: thursday", "THU"), FRIDAY(Calendar.FRIDAY, "Weekday: friday", "FRI"), SATURDAY(Calendar.SATURDAY, "Weekday: saturday", "SAT"), SUNDAY(Calendar.SUNDAY, "Weekday: sunday", "SUN");

    private final int id;
    private final String name;
    private final String croneSchedulerName;

    private Weekday(final int id, final String name, final String croneSchedulerName) {
        this.id = id;
        this.name = name;
        this.croneSchedulerName = croneSchedulerName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCroneSchedulerName() {
        return croneSchedulerName;
    }

    public static Weekday getById(final int id) {
        for (final Weekday weekday : Weekday.values()) {
            if (weekday.getId() == id) {
                return weekday;
            }
        }

        throw new IllegalArgumentException(String.format("Invalid index %s", id));
    }
}
