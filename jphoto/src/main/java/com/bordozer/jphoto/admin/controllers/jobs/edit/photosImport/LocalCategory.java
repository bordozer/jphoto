package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import com.bordozer.jphoto.utils.NumberUtils;

public enum LocalCategory implements RemotePhotoSiteCategory {

    ADVERTISING(0, "Advertising"),
    ANIMALS(0, "Animals"),
    CHILDREN(0, "Children"),
    CITY(0, "City"),
    DIGITAL_ART(0, "Digital art"),
    GENRE(0, "Genre"),
    GLAMOUR(0, "Glamour"),
    HDR(0, "HDR"),
    HUMOR(0, "Humor"),
    LANDSCAPE(0, "Landscape"),
    MACRO(0, "Macro"),
    MODELS(0, "Models"),
    NUDE(0, "Nude"),
    OTHER(0, "Other"),
    PORTRAIT(0, "Portrait"),
    REPORTING(0, "Reporting"),
    SPORT(0, "Sport"),
    STILL(0, "Still"),
    TRAVELLING(0, "Travelling"),
    UNDERWATER(0, "Underwater"),
    WEDDING(0, "Wedding"),
    WALLPAPERS(0, "Wallpapers");

    private final int id;
    private final String name;
    private final String key;

    LocalCategory(final int id, final String name) {
        this.id = id;
        this.name = name;
        this.key = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getKey() {
        return key;
    }

    public static LocalCategory getByName(final String genreName) {
        for (final LocalCategory localCategory : LocalCategory.values()) {
            if (localCategory.getName().equals(genreName)) {
                return localCategory;
            }
        }

        throw new IllegalArgumentException(String.format("LocalCategory does not exist for name %s", genreName));
    }

    public static LocalCategory getById(final int id) {
        for (final LocalCategory category : values()) {
            if (category.getId() == id) {
                return category;
            }
        }

        return null;
    }

    public static LocalCategory getById(final String id) {
        return getById(NumberUtils.convertToInt(id));
    }
}
