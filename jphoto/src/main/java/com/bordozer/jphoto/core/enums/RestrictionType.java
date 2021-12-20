package com.bordozer.jphoto.core.enums;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public enum RestrictionType implements IdentifiableNameable {

    USER_LOGIN(1, "RestrictionType: User login in", "login.png"), USER_PHOTO_UPLOADING(2, "RestrictionType: User photo uploading", "photo_uploading.png"), USER_COMMENTING(3, "RestrictionType: User commenting", "commenting.png"), USER_MESSAGING(4, "RestrictionType: User messaging", "messaging.png"), USER_PHOTO_APPRAISAL(5, "RestrictionType: User photo appraisal", "photo_appraisal.png"), USER_VOTING_FOR_RANK_IN_GENRE(6, "RestrictionType: User voting fo rank in genre", "voting_fot_ranks_in_genres.png"), PHOTO_TO_BE_PHOTO_OF_THE_DAY(7, "RestrictionType: Photo: To be photo of the day", "photo_of_the_day.png"), PHOTO_SHOWING_IN_TOP_OF_GENRE(8, "RestrictionType: Photo: To show in the best photos of genre", "show_in_genre_top.png"), PHOTO_SHOWING_IN_PHOTO_GALLERY(11, "RestrictionType: Photo: To show in the photo gallery", "show_in_gallery.png"), PHOTO_COMMENTING(9, "RestrictionType: Photo: Commenting", "commenting.png"), PHOTO_APPRAISAL(10, "RestrictionType: Photo: appraisal", "photo_appraisal.png");

    public final static List<RestrictionType> FOR_USERS = newArrayList(USER_LOGIN, USER_PHOTO_UPLOADING, USER_COMMENTING, USER_MESSAGING, USER_PHOTO_APPRAISAL, USER_VOTING_FOR_RANK_IN_GENRE);
    public final static List<RestrictionType> FOR_PHOTOS = newArrayList(PHOTO_TO_BE_PHOTO_OF_THE_DAY, PHOTO_SHOWING_IN_TOP_OF_GENRE, PHOTO_SHOWING_IN_PHOTO_GALLERY, PHOTO_COMMENTING, PHOTO_APPRAISAL);

    private final int id;
    private final String name;
    private final String icon;

    private RestrictionType(final int id, final String name, final String icon) {
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
        return String.format("restrictions/%s", icon);
    }

    public static RestrictionType getById(final int id) {
        for (final RestrictionType restrictionType : RestrictionType.values()) {
            if (restrictionType.getId() == id) {
                return restrictionType;
            }
        }

        throw new IllegalArgumentException(String.format("Illegal RestrictionType id: %d", id));
    }

    public static RestrictionType getById(final String restrictionTypeId) {
        return getById(Integer.parseInt(restrictionTypeId));
    }
}
