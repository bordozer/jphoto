package ui.userRankIcons;

import core.general.genre.Genre;
import core.general.user.User;
import utils.TranslatorUtils;

public abstract class AbstractUserRankIcon {

	private final User user;
	private final Genre genre;

	public abstract String getIcon();

	public abstract String getTitle();

	protected AbstractUserRankIcon( final User user, final Genre genre ) {
		this.user = user;
		this.genre = genre;
	}

	public static AbstractUserRankIcon getNotEnoughPhotosInGenreUserRankIcon( final User user, final Genre genre ) {

		return new AbstractUserRankIcon( user, genre ) {
			@Override
			public String getIcon() {
				return "user_rank_icon_disabled16x16.png";
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "There is not enough photos in category '$1' to have a rank", genre.getName() );
			}
		};
	}

	public static AbstractUserRankIcon getZeroUserRankIcon( final User user, final Genre genre ) {

		return new AbstractUserRankIcon( user, genre ) {
			@Override
			public String getIcon() {
				return "user_rank_icon_zero_16x16.png";
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "Zero rank in category '$1'", genre.getName() );
			}
		};
	}

	public static AbstractUserRankIcon getUserRankIcon( final User user, final Genre genre, final int rank ) {

		return new AbstractUserRankIcon( user, genre ) {
			@Override
			public String getIcon() {
				return "user_rank_icon_16x16.png";
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "Rank in category '$1' : $2", genre.getName(), String.valueOf( rank ) );
			}
		};
	}

	public static AbstractUserRankIcon getNegativeUserRankIcon( final User user, final Genre genre, final int rank ) {
		return new AbstractUserRankIcon( user, genre ) {
			@Override
			public String getIcon() {
				return "user_negative_rank_icon_16x16.png";
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "Negative rank in category '$1' : $2", genre.getName(), String.valueOf( rank ) );
			}
		};
	}

	public static AbstractUserRankIcon getCollapsedUserRankIcon( final User user, final Genre genre, final int rank ) {
		return new AbstractUserRankIcon( user, genre ) {
			@Override
			public String getIcon() {
				return "user_rank_collapsed_icon_16x16.png";
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "Rank in category '$1' : $2", genre.getName(), String.valueOf( rank ) );
			}
		};
	}

	public static AbstractUserRankIcon getCollapsedNegativeUserRankIcon( final User user, final Genre genre, final int rank ) {
		return new AbstractUserRankIcon( user, genre ) {
			@Override
			public String getIcon() {
				return "user_negative_rank_collapsed_icon_16x16.png";
			}

			@Override
			public String getTitle() {
				return TranslatorUtils.translate( "Negative rank in category '$1' : $2", genre.getName(), String.valueOf( rank ) );
			}
		};
	}
}
