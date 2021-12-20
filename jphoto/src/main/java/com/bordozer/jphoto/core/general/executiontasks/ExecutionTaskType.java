package com.bordozer.jphoto.core.general.executiontasks;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;

public enum ExecutionTaskType implements IdentifiableNameable {

    ONCE(1, "ExecutionTaskType: One time", "schedulerTaskOnce.png"), PERIODICAL(2, "ExecutionTaskType: Periodically", "schedulerTaskPeriodical.png"), DAILY(3, "ExecutionTaskType: Daily", "schedulerTaskDaily.png"), MONTHLY(4, "ExecutionTaskType: Monthly", "schedulerTaskMonthly.png");

    private final int id;
    private final String name;
    private final String icon;

    private ExecutionTaskType(final int id, final String name, final String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public static ExecutionTaskType getById(final int id) {
        for (final ExecutionTaskType executionTaskType : ExecutionTaskType.values()) {
            if (executionTaskType.getId() == id) {
                return executionTaskType;
            }
        }

        throw new IllegalArgumentException(String.format("Invalid index %s", id));
    }
}
