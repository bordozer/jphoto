package com.bordozer.jphoto.ui.controllers.photos.groupoperations;

public enum GroupOperationResultType {
    SUCCESSFUL("Successful", "success.png"), WARNING("Information", "warning.png"), ERROR("Error", "error.png"), SKIPPED("Skipped", "skipped.png");

    private final String name;
    private final String icon;

    private GroupOperationResultType(final String name, final String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
}
