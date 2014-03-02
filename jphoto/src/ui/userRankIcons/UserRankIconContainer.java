package ui.userRankIcons;

import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import utils.NumberUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class UserRankIconContainer {

	private final List<AbstractUserRankIcon> rankIcons = newArrayList();

	public UserRankIconContainer( final User user, final Genre genre, final int rank, final Services services ) {
		fillIcons( user, genre, rank, services );
	}

	public UserRankIconContainer( final User user, final Photo photo, final Services services ) {
		final Genre genre = services.getGenreService().load( photo.getGenreId() );
		fillIcons( user, genre, photo.getUserGenreRank(), services );
	}

	private void fillIcons( final User user, final Genre genre, final int rank, final Services services ) {
		final int userId = user.getId();
		final int genreId = genre.getId();

		final boolean userHasEnoughPhotos = services.getUserRankService().isUserHavingEnoughPhotosInGenre( userId, genreId );

		if ( ! userHasEnoughPhotos && rank == 0 ) {
			rankIcons.add( AbstractUserRankIcon.getNotEnoughPhotosInGenreUserRankIcon( user, genre, services ) );
			return;
		}

		if ( userHasEnoughPhotos && rank == 0 ) {
			rankIcons.add( AbstractUserRankIcon.getZeroUserRankIcon( user, genre, services ) );
			return;
		}

		final int qtyToCollapse = services.getConfigurationService().getInt( ConfigurationKey.RANK_VOTING_RANK_QTY_TO_COLLAPSE );
		final int absRank = rank > 0 ? rank : -rank;
		final int collapsedIconsQty = NumberUtils.floor( absRank / qtyToCollapse );
		final int iconsQty = absRank - ( qtyToCollapse * collapsedIconsQty );

		for ( int i = 1; i <= collapsedIconsQty; i++ ) {
			rankIcons.add( rank > 0 ? AbstractUserRankIcon.getCollapsedUserRankIcon( user, genre, i * qtyToCollapse, services ) : AbstractUserRankIcon.getCollapsedNegativeUserRankIcon( user, genre, i * qtyToCollapse, services ) );
		}

		for ( int i = 1; i <= iconsQty ; i++ ) {
			final int shownRank = i + ( collapsedIconsQty * qtyToCollapse );
			rankIcons.add( rank > 0 ? AbstractUserRankIcon.getUserRankIcon( user, genre, shownRank, services ) : AbstractUserRankIcon.getNegativeUserRankIcon( user, genre, shownRank, services ) );
		}
	}

	public List<AbstractUserRankIcon> getRankIcons() {
		return rankIcons;
	}
}
