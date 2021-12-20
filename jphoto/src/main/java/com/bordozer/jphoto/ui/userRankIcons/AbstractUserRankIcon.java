package com.bordozer.jphoto.ui.userRankIcons;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;

public abstract class AbstractUserRankIcon {

    public static final String USER_RANK_ICON_NOT_ENOUGH_PHOTOS = "user_rank_icon_disabled16x16.png";
    public static final String USER_RANK_ICON_ZERO = "user_rank_icon_zero_16x16.png";
    public static final String USER_RANK_ICON = "user_rank_icon_16x16.png";
    public static final String USER_RANK_ICON_NEGATIVE = "user_negative_rank_icon_16x16.png";
    public static final String USER_RANK_ICON_COLLAPSED = "user_rank_collapsed_icon_16x16.png";
    public static final String USER_RANK_ICON_NEGATIVE_COLLAPSED = "user_negative_rank_collapsed_icon_16x16.png";

    protected TranslatorService translatorService;

    public abstract String getIcon();

    public abstract String getTitle();

    protected AbstractUserRankIcon(final TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    public static AbstractUserRankIcon getNotEnoughPhotosInGenreUserRankIcon(final User user, final Genre genre, final TranslatorService translatorService) {

        return new AbstractUserRankIcon(translatorService) {
            @Override
            public String getIcon() {
                return USER_RANK_ICON_NOT_ENOUGH_PHOTOS;
            }

            @Override
            public String getTitle() {
                return translatorService.translate("UserRankIcon: There is not enough photos in category $1 to have a rank", getLanguage(), getGenreName(genre, translatorService));
            }
        };
    }

    public static AbstractUserRankIcon getZeroUserRankIcon(final User user, final Genre genre, final TranslatorService translatorService) {

        return new AbstractUserRankIcon(translatorService) {
            @Override
            public String getIcon() {
                return USER_RANK_ICON_ZERO;
            }

            @Override
            public String getTitle() {
                return translatorService.translate("UserRankIcon: Zero rank in category $1", getLanguage(), getGenreName(genre, translatorService));
            }
        };
    }

    public static AbstractUserRankIcon getUserRankIcon(final User user, final Genre genre, final int rank, final TranslatorService translatorService) {

        return new AbstractUserRankIcon(translatorService) {
            @Override
            public String getIcon() {
                return USER_RANK_ICON;
            }

            @Override
            public String getTitle() {
                return translatorService.translate("UserRankIcon: Rank in category $1 : $2", getLanguage(), getGenreName(genre, translatorService), String.valueOf(rank));
            }
        };
    }

    public static AbstractUserRankIcon getNegativeUserRankIcon(final User user, final Genre genre, final int rank, final TranslatorService translatorService) {
        return new AbstractUserRankIcon(translatorService) {
            @Override
            public String getIcon() {
                return USER_RANK_ICON_NEGATIVE;
            }

            @Override
            public String getTitle() {
                return translatorService.translate("UserRankIcon: Negative rank in category $1 : $2", getLanguage(), getGenreName(genre, translatorService), String.valueOf(rank));
            }
        };
    }

    public static AbstractUserRankIcon getCollapsedUserRankIcon(final User user, final Genre genre, final int rank, final TranslatorService translatorService) {
        return new AbstractUserRankIcon(translatorService) {
            @Override
            public String getIcon() {
                return USER_RANK_ICON_COLLAPSED;
            }

            @Override
            public String getTitle() {
                return translatorService.translate("UserRankIcon: Rank in category $1 : $2", getLanguage(), getGenreName(genre, translatorService), String.valueOf(rank));
            }
        };
    }

    public static AbstractUserRankIcon getCollapsedNegativeUserRankIcon(final User user, final Genre genre, final int rank, final TranslatorService services) {
        return new AbstractUserRankIcon(services) {
            @Override
            public String getIcon() {
                return USER_RANK_ICON_NEGATIVE_COLLAPSED;
            }

            @Override
            public String getTitle() {
                return translatorService.translate("UserRankIcon: Negative rank in category $1 : $2", getLanguage(), getGenreName(genre, translatorService), String.valueOf(rank));
            }
        };
    }

    private static String getGenreName(final Genre genre, final TranslatorService translatorService) {
        return translatorService.translateGenre(genre, getLanguage());
    }

    private static Language getLanguage() {
        return EnvironmentContext.getLanguage(); // TODO: pass the language of logged user
    }
}
