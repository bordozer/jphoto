package com.bordozer.jphoto.ui.userRankIcons;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.utils.NumberUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class UserRankIconContainer {

    private final List<AbstractUserRankIcon> rankIcons = newArrayList();

    public UserRankIconContainer(final User user, final Genre genre, final int rank, final UserRankService userRankService, final ConfigurationService configurationService, final TranslatorService translatorService) {
        fillIcons(user, genre, rank, userRankService, configurationService, translatorService);
    }

    public UserRankIconContainer(final User user, final Photo photo, final UserRankService userRankService, final ConfigurationService configurationService, final GenreService genreService, final TranslatorService translatorService) {
        final Genre genre = genreService.load(photo.getGenreId());
        fillIcons(user, genre, photo.getUserGenreRank(), userRankService, configurationService, translatorService);
    }

    private void fillIcons(final User user, final Genre genre, final int rank, final UserRankService userRankService, final ConfigurationService configurationService, final TranslatorService translatorService) {
        final int userId = user.getId();
        final int genreId = genre.getId();

        final boolean userHasEnoughPhotos = userRankService.isUserHavingEnoughPhotosInGenre(userId, genreId);

        if (!userHasEnoughPhotos && rank == 0) {
            rankIcons.add(AbstractUserRankIcon.getNotEnoughPhotosInGenreUserRankIcon(user, genre, translatorService));
            return;
        }

        if (userHasEnoughPhotos && rank == 0) {
            rankIcons.add(AbstractUserRankIcon.getZeroUserRankIcon(user, genre, translatorService));
            return;
        }

        final int qtyToCollapse = configurationService.getInt(ConfigurationKey.RANK_VOTING_RANK_QTY_TO_COLLAPSE);
        final int absRank = rank > 0 ? rank : -rank;
        final int collapsedIconsQty = NumberUtils.floor(absRank / qtyToCollapse);
        final int iconsQty = absRank - (qtyToCollapse * collapsedIconsQty);

        for (int i = 1; i <= collapsedIconsQty; i++) {
            rankIcons.add(rank > 0 ? AbstractUserRankIcon.getCollapsedUserRankIcon(user, genre, i * qtyToCollapse, translatorService) : AbstractUserRankIcon.getCollapsedNegativeUserRankIcon(user, genre, i * qtyToCollapse, translatorService));
        }

        for (int i = 1; i <= iconsQty; i++) {
            final int shownRank = i + (collapsedIconsQty * qtyToCollapse);
            rankIcons.add(rank > 0 ? AbstractUserRankIcon.getUserRankIcon(user, genre, shownRank, translatorService) : AbstractUserRankIcon.getNegativeUserRankIcon(user, genre, shownRank, translatorService));
        }
    }

    public List<AbstractUserRankIcon> getRankIcons() {
        return rankIcons;
    }
}
