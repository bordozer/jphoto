package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.filesystem;

public enum PhotographStrategyType {

    NATURE_ANIMALS_UNDERWATER(10), NUDE_GLAMOUR_PORTRAIT(10), SPORT_REPORTING_GENRE(4), STILL(9), HDR_DIGITAL_ART(2), ADVERTISING(4), CHILDREN(1), CITY_TRAVELLING(5), MACRO(8), WEDDING(2), HUMOR_WALLPAPER_OTHER(2);


    private final int priority;

    private PhotographStrategyType(final int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
