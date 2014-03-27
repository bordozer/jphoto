package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.enums.FavoriteEntryType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.photo.Photo;
import core.general.user.User;
import core.log.LogHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class FavoritesJob extends AbstractJob {

	private List<FavoriteEntryType> favoriteEntries;

	private final static Integer MAX_ITERATIONS = 1000;

	public FavoritesJob( final JobRuntimeEnvironment jobEnvironment) {
		super( new LogHelper( FavoritesJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {
		final FavoriteGeneratorFactory factory = new FavoriteGeneratorFactory();

		int emptyIterations = 0;
		while ( ! isFinished() ) {

			final FavoriteEntryType favoriteType = services.getRandomUtilsService().getRandomFavoriteType( favoriteEntries );
			final User userWhoIsAddingToFavorites = services.getRandomUtilsService().getRandomUser( beingProcessedUsers );

			final AbstractFavoriteEntryGenerator entryGenerator = factory.getInstance( favoriteType, userWhoIsAddingToFavorites );

			if ( entryGenerator.generateFavoriteEntry() ) {
				increment();
				emptyIterations = 0;

			} else {
				emptyIterations++;
			}

			if ( hasJobFinishedWithAnyResult() ) {
				break;
			}

			if ( emptyIterations > MAX_ITERATIONS ) {
				getLog().info( String.format( "%s empty iterations. Stopped.", MAX_ITERATIONS ) );
				break;
			}
		}
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.FAVORITES_GENERATION;
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {

		final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

		parametersMap.put( SavedJobParameterKey.PARAM_ACTIONS_QTY, new CommonProperty( SavedJobParameterKey.PARAM_ACTIONS_QTY.getId(), totalJopOperations ) );
		parametersMap.put( SavedJobParameterKey.PARAM_PHOTOS_QTY, new CommonProperty( SavedJobParameterKey.PARAM_PHOTOS_QTY.getId(), photoQtyLimit ) );
		parametersMap.put( SavedJobParameterKey.PARAM_FAVORITE_ENTRIES, CommonProperty.createFromIdentifiable( SavedJobParameterKey.PARAM_FAVORITE_ENTRIES.getId(), favoriteEntries ) );

		return parametersMap;
	}

	@Override
	public String getJobParametersDescription() {
		final StringBuilder builder = new StringBuilder();

		builder.append( services.getTranslatorService().translate( "Total job's steps", getLanguage() ) ).append( ": " ).append( totalJopOperations );
		for ( final FavoriteEntryType favoriteEntry : favoriteEntries ) {
			builder.append( "<br />- " ).append( favoriteEntry.getName() );
		}

		return builder.toString();
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
		totalJopOperations = jobParameters.get( SavedJobParameterKey.PARAM_ACTIONS_QTY ).getValueInt();
		photoQtyLimit = jobParameters.get( SavedJobParameterKey.PARAM_PHOTOS_QTY ).getValueInt();

		final List<Integer> favoriteEntriesIds = jobParameters.get( SavedJobParameterKey.PARAM_FAVORITE_ENTRIES ).getValueListInt();
		favoriteEntries = newArrayList();
		for ( final Integer favoriteEntriesId : favoriteEntriesIds ) {
			favoriteEntries.add( FavoriteEntryType.getById( favoriteEntriesId ) );
		}
	}

	public void setFavoriteEntries( final List<FavoriteEntryType> favoriteEntries ) {
		this.favoriteEntries = favoriteEntries;
	}

	private class FavoriteGeneratorFactory {
		public AbstractFavoriteEntryGenerator getInstance( final FavoriteEntryType favoriteEntryType, final User user ) {
			switch ( favoriteEntryType ) {
				case PHOTO:
					return new FavoritePhotoEntryGenerator( user );
				case USER:
					return new FavoriteUserEntryGenerator( user );
				case FRIEND:
					return new FriendEntryGenerator( user );
				case BLACKLIST:
					return new BlacklistEntryGenerator( user );
				case BOOKMARK:
					return new BookmarkEntryGenerator( user );
				case NEW_COMMENTS_NOTIFICATION:
					return new NewCommentNotificationEntryGenerator( user );
				case NEW_PHOTO_NOTIFICATION:
					return new NewPhotoNotificationEntryGenerator( user );
			}
			throw new IllegalArgumentException( String.format( "Illegal favorite entry: %s", favoriteEntryType ) );
		}
	}

	private abstract class AbstractFavoriteEntryGenerator {
		protected final User user;

		abstract boolean generateFavoriteEntry();

		protected boolean addFavoriteUser( final FavoriteEntryType favoriteEntryType ) {
			final User randomUser = services.getRandomUtilsService().getRandomUserButNotThisOne( user, FavoritesJob.this.beingProcessedUsers );

			if ( randomUser == null ) {
				return false;
			}

			if ( services.getFavoritesService().isEntryInFavorites( user.getId(), randomUser.getId(), favoriteEntryType.getId() ) ) {
				return false;
			}

			final Date currentTime = services.getDateUtilsService().getCurrentTime();

			services.getFavoritesService().addEntryToFavorites( user.getId(), randomUser.getId(), currentTime, favoriteEntryType );
			getLog().info( String.format( "Member %d has added member %d to %s", user.getId(), randomUser.getId(), favoriteEntryType ) );

			services.getUsersSecurityService().saveLastUserActivityTime( user.getId(), currentTime );

			return true;
		}

		protected boolean addFavoritePhoto( final FavoriteEntryType favoriteEntryType ) {
			final Photo randomPhoto = services.getRandomUtilsService().getRandomPhotoButNotOfUser( user, FavoritesJob.this.beingProcessedPhotosIds );

			if ( randomPhoto == null ) {
				return false;
			}

			if ( services.getFavoritesService().isEntryInFavorites( user.getId(), randomPhoto.getId(), favoriteEntryType.getId() ) ) {
				return false;
			}

			final Date currentTime = services.getDateUtilsService().getCurrentTime();

			services.getFavoritesService().addEntryToFavorites( user.getId(), randomPhoto.getId(), currentTime, favoriteEntryType );
			getLog().info( String.format( "User %d has added photo %d to %s", user.getId(), randomPhoto.getId(), favoriteEntryType ) );

			services.getUsersSecurityService().saveLastUserActivityTime( user.getId(), currentTime );

			return true;
		}

		protected AbstractFavoriteEntryGenerator( final User user ) {
			this.user = user;
		}
	}

	private class FavoriteUserEntryGenerator extends AbstractFavoriteEntryGenerator {

		protected FavoriteUserEntryGenerator( final User user ) {
			super( user );
		}

		@Override
		public boolean generateFavoriteEntry() {
			return addFavoriteUser( FavoriteEntryType.USER );
		}
	}

	private class FriendEntryGenerator extends AbstractFavoriteEntryGenerator {

		protected FriendEntryGenerator( final User user ) {
			super( user );
		}

		@Override
		public boolean generateFavoriteEntry() {
			return addFavoriteUser( FavoriteEntryType.FRIEND );
		}
	}

	private class BlacklistEntryGenerator extends AbstractFavoriteEntryGenerator {

		protected BlacklistEntryGenerator( final User user ) {
			super( user );
		}

		@Override
		public boolean generateFavoriteEntry() {
			return addFavoriteUser( FavoriteEntryType.BLACKLIST );
		}
	}

	private class NewPhotoNotificationEntryGenerator extends AbstractFavoriteEntryGenerator {

		protected NewPhotoNotificationEntryGenerator( final User user ) {
			super( user );
		}

		@Override
		public boolean generateFavoriteEntry() {
			return addFavoriteUser( FavoriteEntryType.NEW_PHOTO_NOTIFICATION );
		}
	}

	private class FavoritePhotoEntryGenerator extends AbstractFavoriteEntryGenerator {

		public FavoritePhotoEntryGenerator( final User user ) {
			super( user );
		}

		@Override
		public boolean generateFavoriteEntry() {
			return addFavoritePhoto( FavoriteEntryType.PHOTO );
		}
	}

	private class BookmarkEntryGenerator extends AbstractFavoriteEntryGenerator {

		protected BookmarkEntryGenerator( final User user ) {
			super( user );
		}

		@Override
		public boolean generateFavoriteEntry() {
			return addFavoritePhoto( FavoriteEntryType.BOOKMARK );
		}
	}

	private class NewCommentNotificationEntryGenerator extends AbstractFavoriteEntryGenerator {

		protected NewCommentNotificationEntryGenerator( final User user ) {
			super( user );
		}

		@Override
		public boolean generateFavoriteEntry() {
			return addFavoritePhoto( FavoriteEntryType.NEW_COMMENTS_NOTIFICATION );
		}
	}
}
