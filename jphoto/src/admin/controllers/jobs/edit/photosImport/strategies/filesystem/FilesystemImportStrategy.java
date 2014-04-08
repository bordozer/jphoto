package admin.controllers.jobs.edit.photosImport.strategies.filesystem;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageToImport;
import admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.FileSystemImportParameters;
import admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import admin.jobs.entries.AbstractJob;
import admin.jobs.entries.resources.photos.FakePhotoNameGenerator;
import admin.jobs.general.JobDateRange;
import core.exceptions.BaseRuntimeException;
import core.exceptions.SaveToDBException;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.user.User;
import core.log.LogHelper;
import core.services.system.Services;
import core.services.utils.PredicateUtilsService;
import org.apache.commons.io.FileUtils;
import utils.PhotoUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class FilesystemImportStrategy extends AbstractPhotoImportStrategy {

	private final List<ImageToImport> imageToImports;

	protected FileSystemImportParameters importParameters;

	public FilesystemImportStrategy( final AbstractJob job, final AbstractImportParameters parameters, final Services services ) {
		super( job, services, new LogHelper( FilesystemImportStrategy.class ), parameters.getLanguage() );

		importParameters = (FileSystemImportParameters) parameters;

		imageToImports = collectFileSystemImages( new File( importParameters.getPictureDir() ) );

		createNecessaryGenres();
	}

	@Override
	public void doImport() throws IOException, SaveToDBException {
		final Set<Genre> genres = getGenres();

		final UserGeneratorFactory factory = new UserGeneratorFactory();
		final AbstractUserGenerator userGenerator = factory.getInstance( importParameters.getAssignAllGeneratedPhotosToUserId(), genres, services, job.getBeingProcessedUsers() );

		final Iterator<ImageToImport> pictureIterator = imageToImports.iterator();

		log.debug( "Processing images" );

		while( pictureIterator.hasNext() ) {
			final ImageToImport imageToImport = pictureIterator.next();

			final ImageDiscEntry imageDiscEntry = imageToImport.getImageDiscEntry();
			final Genre genre = getGenreByName( genres, imageDiscEntry.getGenreDiscEntry().getName() );

			final User user = userGenerator.getUser( genre );
			imageToImport.setUser( user );

			imageToImport.setName( getRandomPhotoName( genre ) );

			final JobDateRange jobDateRange = importParameters.getJobDateRange();
			imageToImport.setUploadTime( services.getRandomUtilsService().getRandomDate( jobDateRange.getStartDate(), jobDateRange.getEndDate() ) );

			imageToImport.setPhotoDescription( String.format( "The photo is imported from disk: %s", imageDiscEntry.getImageFile().getCanonicalPath() ) );
			imageToImport.setPhotoKeywords( "imported, from, disk" );

			pictureIterator.remove();

			createPhotoDBEntry( imageToImport );

			if ( importParameters.isDeletePictureAfterImport() ) {
				FileUtils.deleteQuietly( imageToImport.getImageDiscEntry().getImageFile() );
			}

			job.increment();

			if ( job.hasJobFinishedWithAnyResult() || job.isFinished() ) {
				break;
			}
		}
	}

	private String getRandomPhotoName( final Genre genre ) {
		return FakePhotoNameGenerator.getFakePhotoName( genre, services.getRandomUtilsService() );
	}

	@Override
	public int getTotalOperations( final int operationsLimitByProperties ) throws IOException {
		if ( operationsLimitByProperties > 0 && operationsLimitByProperties != AbstractJob.OPERATION_COUNT_UNKNOWN ) {
			return operationsLimitByProperties;
		}

		return imageToImports.size();
	}

	private void createNecessaryGenres() {

		log.debug( "Creating necessary genres" );

		final File pictureRoot = new File( importParameters.getPictureDir() );
		final File[] genreDirList = pictureRoot.listFiles( getPredicateUtilsService().getDirFilter() );
		for ( File genreDir : genreDirList ) {
			createNecessaryGenre( genreDir.getName() );
		}
	}

	private Set<Genre> getGenres() {

		final Set<Genre> result = newHashSet();

		final List<Genre> persistedGenres = services.getGenreService().loadAll();
		for ( final Genre genre : persistedGenres ) {
			result.add( genre );
		}

		return result;
	}

	private Genre getGenreByName( final Set<Genre> genres, final String genreName ) {
		for ( final Genre genre : genres ) {
			if ( genre.getName().equalsIgnoreCase( genreName ) ) {
				return genre;
			}
		}
		throw new BaseRuntimeException( String.format( "Can not find genre by name: '%s'", genreName ) );
	}

	private List<ImageToImport> collectFileSystemImages( final File pictureRoot ) {

		log.debug( "Collecting images on disk info" );

		final List<ImageToImport> importedPictures = newArrayList();

		final File[] genreDirList = pictureRoot.listFiles( getPredicateUtilsService().getDirFilter() );
		if ( genreDirList == null ) {
			return newArrayList();
		}

		for ( File genreDir : genreDirList ) {

			final String genreName = genreDir.getName();

			final File[] files = genreDir.listFiles( getPredicateUtilsService().getFileFilter() );
			if ( files == null ) {
				return newArrayList();
			}
			for ( File file : files ) {

				final List<String> allowedExtensions = services.getConfigurationService().getListString( ConfigurationKey.PHOTO_UPLOAD_FILE_ALLOWED_EXTENSIONS );
				if ( PhotoUtils.isPhotoContentTypeSupported( allowedExtensions, services.getImageFileUtilsService().getContentType( file ) ) ) {

					final ImageDiscEntry imageDiscEntry = new ImageDiscEntry( file, GenreDiscEntry.getByName( genreName ) );
					final ImageToImport imageToImport = new ImageToImport( imageDiscEntry );

					importedPictures.add( imageToImport );
				}
			}
		}

		Collections.shuffle( importedPictures );

		return importedPictures;
	}

	private PredicateUtilsService getPredicateUtilsService() {
		return services.getPredicateUtilsService();
	}
}
