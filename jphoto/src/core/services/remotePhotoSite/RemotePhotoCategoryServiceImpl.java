package core.services.remotePhotoSite;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategoriesMappingStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategoryToGenreMapping;
import core.general.genre.Genre;
import core.log.LogHelper;
import core.services.entry.GenreService;
import org.springframework.beans.factory.annotation.Autowired;

public class RemotePhotoCategoryServiceImpl implements RemotePhotoCategoryService {

	@Autowired
	private GenreService genreService;

	private final LogHelper log = new LogHelper( RemotePhotoCategoryServiceImpl.class );

	@Override
	public GenreDiscEntry getMappedGenreDiscEntry( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {

		for ( final RemotePhotoSiteCategoryToGenreMapping photoCategoryMapping : RemotePhotoSiteCategoriesMappingStrategy.getStrategyFor( remotePhotoSiteCategory ).getMapping() ) {
			if ( photoCategoryMapping.getRemotePhotoSiteCategory() == remotePhotoSiteCategory ) {
				return photoCategoryMapping.getGenreDiscEntry();
			}
		}

		return null;
	}

	@Override
	public Genre getMappedGenreOrOther( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {

		final GenreDiscEntry genreDiscEntry = getMappedGenreDiscEntry( remotePhotoSiteCategory );
		if ( genreDiscEntry != null ) {
			final Genre genre = getGenreBy( genreDiscEntry );
			if ( genre != null ) {
				return genre;
			}
		}

		log.warn( String.format( "Remote photo site category %s does not mach any genre", remotePhotoSiteCategory ) );

		return getGenreBy( GenreDiscEntry.OTHER );
	}

	@Override
	public Genre getMappedGenreOrOther( final PhotosImportSource photosImportSource, final String genreName ) {

		final Genre genre = getMappedGenreOrNull( photosImportSource, genreName );
		if ( genre != null ) {
			return genre;
		}

		return getGenreBy( GenreDiscEntry.OTHER );
	}

	@Override
	public Genre getMappedGenreOrNull( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {

		final GenreDiscEntry genreDiscEntry = getMappedGenreDiscEntry( remotePhotoSiteCategory );
		if ( genreDiscEntry != null ) {
			return genreService.loadByName( genreDiscEntry.getName() );
		}

		return null;
	}

	@Override
	public Genre getMappedGenreOrNull( final PhotosImportSource photosImportSource, final String genreName ) {

		final RemotePhotoSiteCategory[] remotePhotoSiteCategories = RemotePhotoSiteCategory.getRemotePhotoSiteCategories( photosImportSource );

		for ( final RemotePhotoSiteCategory remotePhotoSiteCategory : remotePhotoSiteCategories ) {
			if ( remotePhotoSiteCategory.getName().equals( genreName ) ) {
				return getMappedGenreOrNull( remotePhotoSiteCategory );
			}
		}

		return null;
	}

	private Genre getGenreBy( final GenreDiscEntry genreDiscEntry ) {
		final Genre genre = genreService.loadByName( genreDiscEntry.getName() );
		if ( genre != null ) {
			return genre;
		}

		return null;
	}
}
