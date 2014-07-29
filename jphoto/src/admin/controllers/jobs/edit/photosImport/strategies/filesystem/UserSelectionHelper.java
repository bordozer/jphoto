package admin.controllers.jobs.edit.photosImport.strategies.filesystem;

import admin.controllers.jobs.edit.photosImport.LocalCategory;
import core.general.genre.Genre;
import core.general.user.UserMembershipType;
import core.services.system.Services;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class UserSelectionHelper {

	protected final Services services;

	public UserSelectionHelper( final Services services ) {
		this.services = services;
	}

	public AbstractPhotographStrategy getRandomStrategy() {
		return getStrategy( getRandomTypeConsideringPriority() );
	}

	private PhotographStrategyType getRandomTypeConsideringPriority() {
		final List<PhotographStrategyType> strategyTypes = newArrayList();

		final PhotographStrategyType[] values = PhotographStrategyType.values();
		for ( final PhotographStrategyType strategyType : values ) {
			for ( int i = 0; i < strategyType.getPriority(); i++ ) {
				strategyTypes.add( strategyType );
			}
		}
		return services.getRandomUtilsService().getRandomGenericListElement( strategyTypes );
	}

	public boolean isGenresSimilar( final Genre genre1, final Genre genre2 ) {
		final PhotographStrategyType[] strategyTypes = PhotographStrategyType.values();

		for ( final PhotographStrategyType strategyType : strategyTypes ) {
			final AbstractPhotographStrategy strategy = getStrategy( strategyType );

			final boolean strategySupportBothGenres = strategy.doesSupportGenre( genre1 ) && strategy.doesSupportGenre( genre2 );
			if ( strategySupportBothGenres ) {
				return strategySupportBothGenres; // at least one strategy support both genres
			}
		}

		return false;
	}

	private AbstractPhotographStrategy getStrategy( final PhotographStrategyType strategyType ) {
		switch ( strategyType ) {
			case NATURE_ANIMALS_UNDERWATER:
				return new AbstractPhotographStrategy() {
					@Override
					public List<LocalCategory> getSupportedGenres() {
						return newArrayList( LocalCategory.LANDSCAPE, LocalCategory.ANIMALS, LocalCategory.UNDERWATER );
					}

					@Override
					public List<UserMembershipType> getSupportedMembershipType() {
						return newArrayList( UserMembershipType.AUTHOR );
					}
				};
			case NUDE_GLAMOUR_PORTRAIT:
				return new AbstractPhotographStrategy() {
					@Override
					public List<LocalCategory> getSupportedGenres() {
						return newArrayList( LocalCategory.NUDE, LocalCategory.GLAMOUR, LocalCategory.PORTRAIT );
					}

					@Override
					public List<UserMembershipType> getSupportedMembershipType() {
						return newArrayList( UserMembershipType.AUTHOR, UserMembershipType.MODEL, UserMembershipType.MAKEUP_MASTER );
					}
				};
			case SPORT_REPORTING_GENRE:
				return new AbstractPhotographStrategy() {
					@Override
					public List<LocalCategory> getSupportedGenres() {
						return newArrayList( LocalCategory.SPORT, LocalCategory.REPORTING, LocalCategory.GENRE );
					}

					@Override
					public List<UserMembershipType> getSupportedMembershipType() {
						return newArrayList( UserMembershipType.AUTHOR );
					}
				};
			case STILL:
				return new AbstractPhotographStrategy() {
					@Override
					public List<LocalCategory> getSupportedGenres() {
						return newArrayList( LocalCategory.STILL );
					}

					@Override
					public List<UserMembershipType> getSupportedMembershipType() {
						return newArrayList( UserMembershipType.AUTHOR );
					}
				};
			case HDR_DIGITAL_ART:
				return new AbstractPhotographStrategy() {
					@Override
					public List<LocalCategory> getSupportedGenres() {
						return newArrayList( LocalCategory.HDR, LocalCategory.DIGITAL_ART );
					}

					@Override
					public List<UserMembershipType> getSupportedMembershipType() {
						return newArrayList( UserMembershipType.AUTHOR );
					}
				};
			case ADVERTISING:
				return new AbstractPhotographStrategy() {
					@Override
					public List<LocalCategory> getSupportedGenres() {
						return newArrayList( LocalCategory.ADVERTISING );
					}

					@Override
					public List<UserMembershipType> getSupportedMembershipType() {
						return newArrayList( UserMembershipType.AUTHOR, UserMembershipType.MODEL, UserMembershipType.MAKEUP_MASTER );
					}
				};
			case CHILDREN:
				return new AbstractPhotographStrategy() {
					@Override
					public List<LocalCategory> getSupportedGenres() {
						return newArrayList( LocalCategory.CHILDREN );
					}

					@Override
					public List<UserMembershipType> getSupportedMembershipType() {
						return newArrayList( UserMembershipType.AUTHOR );
					}
				};
			case CITY_TRAVELLING:
				return new AbstractPhotographStrategy() {
					@Override
					public List<LocalCategory> getSupportedGenres() {
						return newArrayList( LocalCategory.CITY, LocalCategory.TRAVELLING );
					}

					@Override
					public List<UserMembershipType> getSupportedMembershipType() {
						return newArrayList( UserMembershipType.AUTHOR );
					}
				};
			case HUMOR_WALLPAPER_OTHER:
				return new AbstractPhotographStrategy() {
					@Override
					public List<LocalCategory> getSupportedGenres() {
						return newArrayList( LocalCategory.HUMOR, LocalCategory.WALLPAPERS, LocalCategory.OTHER );
					}

					@Override
					public List<UserMembershipType> getSupportedMembershipType() {
						return newArrayList( UserMembershipType.AUTHOR );
					}
				};
			case MACRO:
				return new AbstractPhotographStrategy() {
					@Override
					public List<LocalCategory> getSupportedGenres() {
						return newArrayList( LocalCategory.MACRO );
					}

					@Override
					public List<UserMembershipType> getSupportedMembershipType() {
						return newArrayList( UserMembershipType.AUTHOR );
					}
				};
			case WEDDING:
				return new AbstractPhotographStrategy() {
					@Override
					public List<LocalCategory> getSupportedGenres() {
						return newArrayList( LocalCategory.WEDDING );
					}

					@Override
					public List<UserMembershipType> getSupportedMembershipType() {
						return newArrayList( UserMembershipType.AUTHOR, UserMembershipType.MODEL, UserMembershipType.MAKEUP_MASTER );
					}
				};
		}

		throw new IllegalArgumentException( String.format( "Illegal PhotographStrategyType: %s", strategyType ) );
	}
}
