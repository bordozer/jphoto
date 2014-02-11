package ui.userRankIcons;

import core.general.genre.Genre;
import core.general.user.User;
import utils.TranslatorUtils;

public abstract class AbstractUserRankIcon {

	public static final String USER_RANK_ICON_NOT_ENOUGH_PHOTOS = "user_rank_icon_disabled16x16.png";
	public static final String USER_RANK_ICON_ZERO = "user_rank_icon_zero_16x16.png";
	public static final String USER_RANK_ICON = "user_rank_icon_16x16.png";
	public static final String USER_RANK_ICON_NEGATIVE = "user_negative_rank_icon_16x16.png";
	public static final String USER_RANK_ICON_COLLAPSED = "user_rank_collapsed_icon_16x16.png";
	public static final String USER_RANK_ICON_NEGATIVE_COLLAPSED = "user_negative_rank_collapsed_icon_16x16.png";

	public abstract String getIcon();

	public abstract String getTitle();

	protected AbstractUserRankIcon() {
	}

	public static AbstractUserRankIcon getNotEnoughPhotosInGenreUserRankIcon( final User user, final Genre genre ) {

		return new AbstractUserRankIcon() {
			@Override
			public String getIcon() {
				return USER_RANK_ICON_NOT_ENOUGH_PHOTOS;
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "There is not enough photos in category '$1' to have a rank", genre.getName() );
			}
		};
	}

	public static AbstractUserRankIcon getZeroUserRankIcon( final User user, final Genre genre ) {

		return new AbstractUserRankIcon() {
			@Override
			public String getIcon() {
				return USER_RANK_ICON_ZERO;
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "Zero rank in category '$1'", genre.getName() );
			}
		};
	}

	public static AbstractUserRankIcon getUserRankIcon( final User user, final Genre genre, final int rank ) {

		return new AbstractUserRankIcon() {
			@Override
			public String getIcon() {
				return USER_RANK_ICON;
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "Rank in category '$1' : $2", genre.getName(), String.valueOf( rank ) );
			}
		};
	}

	public static AbstractUserRankIcon getNegativeUserRankIcon( final User user, final Genre genre, final int rank ) {
		return new AbstractUserRankIcon() {
			@Override
			public String getIcon() {
				return USER_RANK_ICON_NEGATIVE;
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "Negative rank in category '$1' : $2", genre.getName(), String.valueOf( rank ) );
			}
		};
	}

	public static AbstractUserRankIcon getCollapsedUserRankIcon( final User user, final Genre genre, final int rank ) {
		return new AbstractUserRankIcon() {
			@Override
			public String getIcon() {
				return USER_RANK_ICON_COLLAPSED;
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "Rank in category '$1' : $2", genre.getName(), String.valueOf( rank ) );
			}
		};
	}

	public static AbstractUserRankIcon getCollapsedNegativeUserRankIcon( final User user, final Genre genre, final int rank ) {
		return new AbstractUserRankIcon() {
			@Override
			public String getIcon() {
				return USER_RANK_ICON_NEGATIVE_COLLAPSED;
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "Negative rank in category '$1' : $2", genre.getName(), String.valueOf( rank ) );
			}
		};
	}
}
