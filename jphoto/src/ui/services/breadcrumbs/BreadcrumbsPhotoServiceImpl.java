package ui.services.breadcrumbs;

import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.system.Services;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import ui.controllers.photos.edit.PhotoEditWizardStep;
import ui.services.breadcrumbs.items.*;

import static com.google.common.collect.Lists.newArrayList;

public class BreadcrumbsPhotoServiceImpl implements BreadcrumbsPhotoService {

	@Autowired
	private Services services;

	@Autowired
	private BreadcrumbsPhotoGalleryService breadcrumbsPhotoGalleryService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public PageTitleData getUploadPhotoBreadcrumbs( final User user, final PhotoEditWizardStep wizardStep ) {

		final TranslatableStringBreadcrumb breadcrumb = new TranslatableStringBreadcrumb( "Breadcrumbs: Photo uploading", services );
		final String title = BreadcrumbsBuilder.pageTitle( breadcrumb, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( breadcrumb, services ).build();

		final String breadcrumbs = portalPage()
			.userCardLink( user )
			.add( breadcrumb )
			.translatableString( wizardStep.getStepDescription() )
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoEditDataBreadcrumbs( final Photo photo ) {

		final String title = BreadcrumbsBuilder.pageTitle( new PhotoNameBreadcrumb( photo, services ), services ).build();

		final TranslatableStringBreadcrumb breadcrumb = new TranslatableStringBreadcrumb( "Breadcrumbs: Photo editing", services );
		final String header = BreadcrumbsBuilder.pageHeader( breadcrumb, services ).build();

		final String breadcrumbs = userPhotoWithAuthor( photo )
			.photoCardLink( photo )
			.add( breadcrumb )
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoCardBreadcrumbs( final Photo photo, final User accessor ) {

		final PhotoNameBreadcrumb breadcrumb = new PhotoNameBreadcrumb( photo, services );

		final String title = BreadcrumbsBuilder.pageTitle( breadcrumb, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( breadcrumb, services ).build();

		final String breadcrumbs = userPhoto( photo, accessor )
			.photoName( photo )
			.build( accessor.getLanguage() );

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoActivitiesBreadcrumbs( final Photo photo, final User accessor ) {
		return new PageTitleData( "", "", "" ); //getPhotoActivitiesBreadcrumbs( photo, accessor );
	}

	/*@Override
	public PageTitleData getPhotoCardForHiddenAuthor( final Photo photo ) {
		*//*final String rootTranslated = getPhotoRootTranslated();

		final String userAnonymousName = configurationService.getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME );
		final String fullTitle = pageTitleUtilsService.getTitleDataString( rootTranslated, userAnonymousName, photo.getName(), title );

		final List<String> breadcrumbList = newArrayList();
		breadcrumbList.add( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ) );
		breadcrumbList.add( entityLinkUtilsService.getPhotosByGenreLink( genre, getLanguage() ) );
		breadcrumbList.add( userAnonymousName );
		breadcrumbList.add( StringUtils.isNotEmpty( title ) ? entityLinkUtilsService.getPhotoCardLink( photo, EnvironmentContext.getLanguage() ) : photo.getNameEscaped() );
		if ( StringUtils.isNotEmpty( title ) ) {
			breadcrumbList.add( title );
		}

		final String portalPage = pageTitleUtilsService.getBreadcrumbsDataString( breadcrumbList );*//*

		return new PageTitleData( "", "", "" );
	}*/

	private BreadcrumbsBuilder portalPage() {
		return BreadcrumbsBuilder.portalPage( services );
	}

	private BreadcrumbsBuilder userPhoto( final Photo photo, final User accessor ) {

		if ( securityService.isPhotoAuthorNameMustBeHidden( photo, accessor ) ) {
			return userPhotoAnonymously( photo, accessor );
		}

		return userPhotoWithAuthor( photo );
	}

	private BreadcrumbsBuilder userPhotoWithAuthor( final Photo photo ) {
		return portalPage()
			.photoGalleryLink()
			.userCardLink( photo.getUserId() )
			.photosByUserAndGenre( photo.getUserId(), photo.getGenreId() )
//			.photoName( photo )
			;
	}

	private BreadcrumbsBuilder userPhotoAnonymously( final Photo photo, final User accessor ) {
		return portalPage()
			.photoGalleryLink()
			.string( configurationService.getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME ) )
//			.photoName( photo )
			;
	}
}
