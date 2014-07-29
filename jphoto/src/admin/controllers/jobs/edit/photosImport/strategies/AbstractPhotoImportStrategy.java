package admin.controllers.jobs.edit.photosImport.strategies;

import admin.controllers.jobs.edit.photosImport.ImportedImage;
import admin.controllers.jobs.edit.photosImport.ImageToImport;
import admin.controllers.jobs.edit.photosImport.RemotePhotoSiteSeries;
import admin.jobs.entries.AbstractJob;
import admin.services.jobs.JobHelperService;
import core.exceptions.BaseRuntimeException;
import core.exceptions.SaveToDBException;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.photoTeam.PhotoTeam;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import core.log.LogHelper;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import core.services.user.UserRankService;
import core.services.utils.DateUtilsService;
import core.services.utils.RandomUtilsService;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractPhotoImportStrategy {

	protected final AbstractJob job;
	protected final Services services;
	protected final LogHelper log;

	protected Language language;

	public abstract void doImport() throws IOException, SaveToDBException;

	public abstract int getTotalOperations( final int totalJopOperations ) throws IOException;

	public AbstractPhotoImportStrategy( final AbstractJob job, final Services services, final LogHelper log, final Language language ) {
		this.job = job;
		this.services = services;
		this.log = log;
		this.language = language;
	}

	protected void createPhotosDBEntries( final List<ImageToImport> imageToImports ) throws IOException, SaveToDBException {
		log.debug( "Creating photos" );

		int counter = 1;
		final int total = imageToImports.size();
		for ( final ImageToImport imageToImport : imageToImports ) {
			createPhotoDBEntry( imageToImport, counter, total );
			counter++;
		}
	}

	protected void createPhotoDBEntry( final ImageToImport photoToImport, final int counter, final int total ) throws IOException, SaveToDBException {

		final ImportedImage importedImage = photoToImport.getImportedImage();

		final User user = photoToImport.getUser();

		final Photo photo = new Photo();
		photo.setUserId( user.getId() );

		final Genre genre = createNecessaryGenre( importedImage.getGenreName() );

		photo.setGenreId( genre.getId() );

		photo.setName( photoToImport.getName() );

		final Date uploadTime = photoToImport.getUploadTime();

		photo.setUploadTime( uploadTime );
		photo.setDescription( photoToImport.getPhotoDescription() );
		photo.setKeywords( photoToImport.getPhotoKeywords() );

		final RandomUtilsService randomUtilsService = services.getRandomUtilsService();
		final JobHelperService jobHelperService = services.getJobHelperService();
		final UserRankService userRankService = services.getUserRankService();

		photo.setAnonymousPosting( jobHelperService.getAnonymousOption( user.getId(), genre.getId(), uploadTime ) );
		photo.setBgColor( randomUtilsService.getPhotoBackgroundRandomColor() );
		photo.setCommentsAllowance( randomUtilsService.getRandomPhotoAllowance() );
		photo.setVotingAllowance( randomUtilsService.getRandomPhotoAllowance() );
		jobHelperService.initPhotoNudeContentOption( photo );
		photo.setUserGenreRank( userRankService.getUserRankInGenre( user.getId(), genre.getId() ) );
		photo.setImportId( photoToImport.getImportId() );

		services.getPhotoService().uploadNewPhoto( photo, importedImage.getImageFile(), getPhotoTeam( photo, user ), getPhotoAlbumsAssignTo( photoToImport, user ) );

		services.getUsersSecurityService().saveLastUserActivityTime( user.getId(), uploadTime ); // TODO: set last activity only if previous one is less then this photo uploading

		photoToImport.setPhoto( photo );

		job.addJobRuntimeLogMessage( new TranslatableMessage( "$1 / $2: Created photo #$3 '$4' of $5, category: $6", services )
			.addIntegerParameter( counter )
			.addIntegerParameter( total )
			.addIntegerParameter( photo.getId() )
			.addPhotoCardLinkParameter( photo )
			.addUserCardLinkParameter( user )
			.addPhotosByGenreLinkParameter( genre )
		);

		log.debug( String.format( "Photo %s is generated for user %s", photo.getNameEscaped(), user.getNameEscaped() ) );
	}

	public Services getServices() {
		return services;
	}

	public Genre createNecessaryGenre( final String genreName ) {
		Genre genre = services.getGenreService().loadIdByName( genreName );
		if ( genre == null || genre.getId() == 0 ) {
			genre = new Genre();
			genre.setName( genreName );
			final DateUtilsService dateUtilsService = services.getDateUtilsService();
			genre.setDescription( String.format( "Created by FilesystemImportStrategy at %s", dateUtilsService.formatDateTime( dateUtilsService.getCurrentTime() ) ) );

			final List<PhotoVotingCategory> votingCategories = services.getVotingCategoryService().loadAll();
			if ( votingCategories.size() == 0 ) {
				throw new BaseRuntimeException( "There are at least three voting categories have to be configured in the system" );
			}

			genre.setPhotoVotingCategories( votingCategories );
			if ( !services.getGenreService().save( genre ) ) {
				throw new BaseRuntimeException( String.format( "Can not create genre by name: '%s'", genreName ) );
			}

			log.debug( String.format( "New genre '%s' has been created", genre.getName() ) );
		}

		return genre;
	}

	private PhotoTeam getPhotoTeam( final Photo photo, final User user ) {

		final UserTeam userTeam = services.getUserTeamService().loadUserTeam( user.getId() );
		final List<UserTeamMember> userTeamMembers = userTeam.getUserTeamMembers();

		final int size = userTeamMembers.size();
		if ( size == 0 ) {
			return new PhotoTeam( photo, Collections.<PhotoTeamMember>emptyList() );
		}

		final List<UserTeamMember> randomUserTeamMember = services.getRandomUtilsService().getRandomNUniqueListElements( userTeamMembers );
		final List<PhotoTeamMember> photoTeamMembers = newArrayList();
		for ( final UserTeamMember userTeamMember : randomUserTeamMember ) {
			final PhotoTeamMember photoTeamMember = new PhotoTeamMember();
			photoTeamMember.setUserTeamMember( userTeamMember );
			photoTeamMember.setDescription( "Generated automatically" );

			photoTeamMembers.add( photoTeamMember );
		}

		return new PhotoTeam( photo, photoTeamMembers );
	}

	private List<UserPhotoAlbum> getPhotoAlbumsAssignTo( final ImageToImport photoToImport, final User user ) {

		final RemotePhotoSiteSeries remotePhotoSiteSeries = photoToImport.getRemotePhotoSiteSeries();

		if ( remotePhotoSiteSeries == null ) {
			return newArrayList();
		}

		final UserPhotoAlbum existingPhotoAlbum = services.getUserPhotoAlbumService().loadPhotoAlbumByName( user, remotePhotoSiteSeries.getName() );
		if ( existingPhotoAlbum != null ) {
			log.debug( String.format( "The photo will be added to the existing album with name '%s'", existingPhotoAlbum.getName() ) );
			return newArrayList( existingPhotoAlbum );
		}

		final UserPhotoAlbum photoAlbum = new UserPhotoAlbum();
		photoAlbum.setName( remotePhotoSiteSeries.getName() );
		photoAlbum.setUser( user );
		photoAlbum.setDescription( String.format( "The album unites multiple images from photo card of remote site" ) );

		services.getUserPhotoAlbumService().save( photoAlbum );

		return newArrayList( photoAlbum );
	}

	/*private List<UserPhotoAlbum> getRandomPhotoAlbums( final User user ) {

		final List<UserPhotoAlbum> userPhotoAlbums = services.getUserPhotoAlbumService().loadAllForEntry( user.getId() );

		if ( userPhotoAlbums.isEmpty() ) {
			return Collections.<UserPhotoAlbum>emptyList();
		}

		return services.getRandomUtilsService().getRandomNUniqueListElements( userPhotoAlbums, 3 );
	}*/
}
