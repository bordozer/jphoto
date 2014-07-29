package rest.admin.jobs.photosImport.remoteSiteCategories;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategoriesMappingStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategoryToGenreMapping;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.services.entry.GenreService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping( "admin/jobs/photos-import/{importSourceId}/categories" )
@Controller
public class RemoteSiteCategoriesController {

	@Autowired
	private GenreService genreService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<RemotePhotoSiteCategoryDTO> userCardVotingAreas( final @PathVariable( "importSourceId" ) int importSourceId ) {

		securityService.assertSuperAdminAccess( EnvironmentContext.getCurrentUser() );

		final PhotosImportSource importSource = PhotosImportSource.getById( importSourceId );

		return getRemotePhotoSiteCategoriesCheckboxes( importSource );
	}

	private List<RemotePhotoSiteCategoryDTO> getRemotePhotoSiteCategoriesCheckboxes( final PhotosImportSource importSource ) {

		final List<RemotePhotoSiteCategory> selectedCategories = getSelectedCategories( importSource );

		final List<RemotePhotoSiteCategory> remotePhotoSiteCategories = Arrays.asList( RemotePhotoSiteCategory.getRemotePhotoSiteCategories( importSource ) );

		Collections.sort( remotePhotoSiteCategories, new Comparator<RemotePhotoSiteCategory>() {
			@Override
			public int compare( final RemotePhotoSiteCategory category1, final RemotePhotoSiteCategory category2 ) {
				return category1.getName().compareTo( category2.getName() );
			}
		} );

		final List<RemotePhotoSiteCategoryDTO> remotePhotoSiteCategoryDTOs = newArrayList();
		for ( final RemotePhotoSiteCategory remotePhotoSiteCategory : remotePhotoSiteCategories ) {

			final RemotePhotoSiteCategoryDTO categoryDTO = new RemotePhotoSiteCategoryDTO();
			categoryDTO.setRemotePhotoSiteCategoryId( remotePhotoSiteCategory.getId() );
			categoryDTO.setRemotePhotoSiteCategoryName( translatorService.translate( remotePhotoSiteCategory.getName(), getLanguage() ) );
			categoryDTO.setChecked( selectedCategories.contains( remotePhotoSiteCategory ) ); // TODO: will be 'contains' working for interface?

			final Genre genre = getGenreByByRemotePhotoSiteCategory( remotePhotoSiteCategory, importSource );
			if ( genre.isCanContainNudeContent() ) {
				categoryDTO.addCssClass( "remote-photo-site-category-nude" );
			} else {
				categoryDTO.addCssClass( "remote-photo-site-category-no-nude" );
			}

			remotePhotoSiteCategoryDTOs.add( categoryDTO );
		}

		return remotePhotoSiteCategoryDTOs;
	}

	private List<RemotePhotoSiteCategory> getSelectedCategories( final PhotosImportSource importSource ) {

		final boolean selectCategoriesWithNudeContent = configurationService.getBoolean( ConfigurationKey.ADMIN_REMOTE_PHOTO_SITE_IMPORT_JOB_IMPORT_NUDE_CONTENT );

		final List<RemotePhotoSiteCategory> selectedCategories = newArrayList( RemotePhotoSiteCategory.getRemotePhotoSiteCategories( importSource ) );

		CollectionUtils.filter( selectedCategories, new Predicate<RemotePhotoSiteCategory>() {

			@Override
			public boolean evaluate( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
				return selectCategoriesWithNudeContent || ! canContainNudeContent( remotePhotoSiteCategory, importSource );
			}
		} );

		return selectedCategories;
	}

	private boolean canContainNudeContent( final RemotePhotoSiteCategory remotePhotoSiteCategory, final PhotosImportSource importSource ) {
		return getGenreByByRemotePhotoSiteCategory( remotePhotoSiteCategory, importSource ).isCanContainNudeContent();
	}

	private Genre getGenreByByRemotePhotoSiteCategory( final RemotePhotoSiteCategory remotePhotoSiteCategory, final PhotosImportSource importSource ) {
		final List<RemotePhotoSiteCategoryToGenreMapping> genreMapping = RemotePhotoSiteCategoriesMappingStrategy.getStrategyFor( importSource ).getMapping();

		for ( final RemotePhotoSiteCategoryToGenreMapping entry : genreMapping ) {
			if ( entry.getRemotePhotoSiteCategory() == remotePhotoSiteCategory ) {
				final GenreDiscEntry genreDiscEntry = entry.getGenreDiscEntry();
				return genreService.loadIdByName( genreDiscEntry.getName() );
			}
		}

		return null;
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
