package ui.userRankIcons;

import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.user.User;
import core.services.system.ConfigurationService;
import core.services.user.UserRankService;
import utils.NumberUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class UserRankIconContainer {

	private final List<AbstractUserRankIcon> rankIcons = newArrayList();

	public UserRankIconContainer( final User user, final Genre genre, final UserRankService userRankService, final ConfigurationService configurationService ) {

		final int rank = userRankService.getUserRankInGenre( user.getId(), genre.getId() );

		final int userId = user.getId();
		final int genreId = genre.getId();

		final boolean userHasEnoughPhotos = userRankService.isUserHavingEnoughPhotosInGenre( userId, genreId );

		if ( ! userHasEnoughPhotos && rank == 0 ) {
			rankIcons.add( AbstractUserRankIcon.getNotEnoughPhotosInGenreUserRankIcon( user, genre ) );
			return;
		}

		if ( userHasEnoughPhotos && rank == 0 ) {
			rankIcons.add( AbstractUserRankIcon.getZeroUserRankIcon( user, genre ) );
			return;
		}

		final int qtyToCollapse = configurationService.getInt( ConfigurationKey.RANK_VOTING_RANK_QTY_TO_COLLAPSE );
		final int absRank = rank > 0 ? rank : -rank;
		final int collapsedIconsQty = NumberUtils.floor( absRank / qtyToCollapse );
		final int iconsQty = absRank - ( qtyToCollapse * collapsedIconsQty );

		for ( int i = iconsQty; i >= 1 ; i-- ) {
			final int shownRank = i + ( collapsedIconsQty * qtyToCollapse );
			rankIcons.add( rank > 0 ? AbstractUserRankIcon.getUserRankIcon( user, genre, shownRank ) : AbstractUserRankIcon.getNegativeUserRankIcon( user, genre, shownRank ) );
		}

		for ( int i = collapsedIconsQty; i >= 1; i-- ) {
			rankIcons.add( rank > 0 ? AbstractUserRankIcon.getCollapsedUserRankIcon( user, genre, i * qtyToCollapse ) : AbstractUserRankIcon.getCollapsedNegativeUserRankIcon( user, genre, i * qtyToCollapse ) );
		}
	}

	public List<AbstractUserRankIcon> getRankIcons() {
		return rankIcons;
	}
}
