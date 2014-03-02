package admin.controllers.jobs.edit.photosImport.strategies;

import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageToImport;
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
import core.services.photo.PhotoService;
import core.services.security.Services;
import core.services.user.UserRankService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.RandomUtilsService;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractPhotoImportStrategy {

	protected final AbstractJob job;
	protected final Services services;
	protected final LogHelper log;

	public abstract void doImport() throws IOException, SaveToDBException;

	public abstract int getTotalOperations( final int totalJopOperations ) throws IOException;

	public AbstractPhotoImportStrategy( final AbstractJob job, final Services services, final LogHelper log ) {
		this.job = job;
		this.services = services;
		this.log = log;
	}

	protected void createPhotosDBEntries( final List<ImageToImport> imageToImports ) throws IOException, SaveToDBException {
		log.debug( "Creating photos" );

		for ( final ImageToImport imageToImport : imageToImports ) {
			createPhotoDBEntry( imageToImport );
		}
	}

	protected void createPhotoDBEntry( final ImageToImport photoToImport ) throws IOException, SaveToDBException {
		final ImageDiscEntry imageDiscEntry = photoToImport.getImageDiscEntry();

		final User user = photoToImport.getUser();

		final Photo photo = new Photo();
		photo.setUserId( user.getId() );

		final String genreName = imageDiscEntry.getGenreDiscEntry().getName();
		final Genre genre = createNecessaryGenre( genreName );

		photo.setGenreId( genre.getId() );

		photo.setName( photoToImport.getName() );

		final Date uploadTime = photoToImport.getUploadTime();

		photo.setUploadTime( uploadTime );
		photo.setDescription( photoToImport.getPhotoDescription() );
		photo.setKeywords( photoToImport.getPhotoKeywords() );

		final RandomUtilsService randomUtilsService = services.getRandomUtilsService();
		final JobHelperService jobHelperService = services.getJobHelperService();
		final UserRankService userRankService = services.getUserRankService();

		photo.setAnonymousPosting( jobHelperService.getAnonymousOption( uploadTime ) );
		photo.setBgColor( randomUtilsService.getPhotoBackgroundRandomColor() );
		photo.setCommentsAllowance( randomUtilsService.getRandomPhotoAllowance() );
		photo.setVotingAllowance( randomUtilsService.getRandomPhotoAllowance() );
		jobHelperService.initPhotoNudeContentOption( photo );
		photo.setUserGenreRank( userRankService.getUserRankInGenre( user.getId(), genre.getId() ) );
		photo.setImportId( photoToImport.getImportId() );

		final PhotoService photoService = services.getPhotoService();

		final PhotoTeam photoTeam = getPhotoTeam( photo, user );
		final List<UserPhotoAlbum> albums = getPhotoAlbums( user );

		photoService.savePhotoWithTeamAndAlbums( photo, photoTeam, albums ); // SAVE PHOTO OR THROW EXCEPTION

		final File imageFile = imageDiscEntry.getImageFile();
		final File photoFile = services.getUserPhotoFilePathUtilsService().copyFileToUserFolder( imageFile, photo, user );

		if ( photoService.updatePhotoFileData( photo.getId(), photoFile ) ) {
			try {
				services.getPreviewGenerationService().generatePreview( photo.getId() );
			} catch ( final InterruptedException e ) {
				final String message = String.format( "Error creating preview: %s ( %s )", photoFile.getCanonicalPath(), e.getMessage() );

				log.error( message );

				photo.setDescription( message );
				photoService.save( photo ); // SAVE ERROR TO DESCRIPTION
				// TODO: delete photo from DB because is has no file
			}
		}

		services.getUsersSecurityService().saveLastUserActivityTime( user.getId(), uploadTime );

		photoToImport.setPhoto( photo );

		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();
		final String message = String.format( "Created photo #%d '%s' of %s, category: %s"
			, photo.getId()
			, entityLinkUtilsService.getPhotoCardLink( photo )
			, entityLinkUtilsService.getUserCardLink( user )
			, services.getEntityLinkUtilsService().getPhotosByGenreLink( services.getGenreService().loadIdByName( genre.getName() ) )
		);
		job.addJobExecutionFinalMessage( message );

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

	private List<UserPhotoAlbum> getPhotoAlbums( final User user ) {
		final List<UserPhotoAlbum> userPhotoAlbums = services.getUserPhotoAlbumService().loadAllForEntry( user.getId() );

		if ( userPhotoAlbums.isEmpty() ) {
			return Collections.<UserPhotoAlbum>emptyList();
		}

		return services.getRandomUtilsService().getRandomNUniqueListElements( userPhotoAlbums );
	}
}
