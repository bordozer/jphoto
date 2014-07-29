package core.services.remotePhotoSite;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
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
	public Genre getGenre( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {

		final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( remotePhotoSiteCategory );
		if ( genreDiscEntry != null ) {
			return genreService.loadIdByName( genreDiscEntry.getName() );
		}

		return null;
	}

	@Override
	public GenreDiscEntry getGenreDiscEntryOrOther( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {

		final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( remotePhotoSiteCategory );
		if ( genreDiscEntry != null ) {
			return genreDiscEntry;
		}

		log.warn( String.format( "Remote photo site category %s does not mach any genre", remotePhotoSiteCategory ) );

		return GenreDiscEntry.OTHER;
	}

	private GenreDiscEntry getGenreDiscEntry( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		for ( final RemotePhotoSiteCategoryToGenreMapping photoCategoryMapping : RemotePhotoSiteCategoriesMappingStrategy.getStrategyFor( remotePhotoSiteCategory ).getMapping() ) {
			if ( photoCategoryMapping.getRemotePhotoSiteCategory() == remotePhotoSiteCategory ) {
				return photoCategoryMapping.getGenreDiscEntry();
			}
		}

		return null;
	}
}
