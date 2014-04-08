package ui.services.breadcrumbs;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.system.Services;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import ui.controllers.photos.edit.PhotoEditWizardStep;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import ui.services.breadcrumbs.items.PhotoNameBreadcrumb;
import ui.services.breadcrumbs.items.TranslatableStringBreadcrumb;
import ui.services.breadcrumbs.items.UserNameBreadcrumb;

import static ui.services.breadcrumbs.items.BreadcrumbsBuilder.portalPage;

public class BreadcrumbsPhotoServiceImpl implements BreadcrumbsPhotoService {

	@Autowired
	private Services services;

	@Autowired
	private SecurityService securityService;

	@Override
	public PageTitleData getUploadPhotoBreadcrumbs( final User user, final PhotoEditWizardStep wizardStep ) {

		final TranslatableStringBreadcrumb photoUploadingText = new TranslatableStringBreadcrumb( "Breadcrumbs: Photo uploading", services );

		final String title = BreadcrumbsBuilder.pageTitle( user, photoUploadingText, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( photoUploadingText, services ).build();

		final String breadcrumbs = portalPage( services )
			.userCardLink( user )
			.add( photoUploadingText )
			.translatableString( wizardStep.getStepDescription() )
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoEditDataBreadcrumbs( final Photo photo ) {

		final String title = BreadcrumbsBuilder.pageTitle( photo.getUserId(), new PhotoNameBreadcrumb( photo, services ), services ).build();

		final TranslatableStringBreadcrumb photoEditingText = new TranslatableStringBreadcrumb( "Breadcrumbs: Photo editing", services );
		final String header = BreadcrumbsBuilder.pageHeader( photoEditingText, services ).build();

		final String breadcrumbs = userPhotoWithAuthor( photo )
			.photoCardLink( photo )
			.add( photoEditingText )
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoCardBreadcrumbs( final Photo photo, final User accessor ) {

		final String title = title( photo, accessor ).photoName( photo ).build();
		final String header = BreadcrumbsBuilder.pageHeader( new UserNameBreadcrumb( photo.getUserId(), services ), services ).build();

		final String breadcrumbs = userPhoto( photo, accessor )
			.photoName( photo )
			.build( accessor.getLanguage() );

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoActivitiesBreadcrumbs( final Photo photo, final User accessor ) {
		return getPhotoFullBreadcrumbs( photo, accessor, "Breadcrumbs: Photo activities" );
	}

	@Override
	public PageTitleData getUserPhotoPreviewsBreadcrumbs( final Photo photo, final User accessor ) {
		return getPhotoFullBreadcrumbs( photo, accessor, "Breadcrumbs: Photo previews" );
	}

	@Override
	public PageTitleData getPhotoAppraisementBreadcrumbs( final Photo photo, final User accessor ) {
		return getPhotoFullBreadcrumbs( photo, accessor, "Breadcrumbs: Photo appraisement" );
	}

	private PageTitleData getPhotoFullBreadcrumbs( final Photo photo, final User accessor, final String breadcrumb ) {
		final String title = title( photo, accessor ).photoName( photo ).build();
		final TranslatableStringBreadcrumb activities = new TranslatableStringBreadcrumb( breadcrumb, services );
		final String header = BreadcrumbsBuilder.pageHeader( activities, services ).build();

		final String breadcrumbs = userPhoto( photo, accessor )
			.photoCardLink( photo )
			.add( activities )
			.build( accessor.getLanguage() );

		return new PageTitleData( title, header, breadcrumbs );
	}

	private BreadcrumbsBuilder title( final Photo photo, final User accessor ) {

		final BreadcrumbsBuilder builder = new BreadcrumbsBuilder( services );

		if ( securityService.isPhotoAuthorNameMustBeHidden( photo, accessor ) ) {
			return builder.projectName();
		}

		return builder.projectName().userName( photo.getUserId() );
	}

	private BreadcrumbsBuilder userPhoto( final Photo photo, final User accessor ) {

		if ( securityService.isPhotoAuthorNameMustBeHidden( photo, accessor ) ) {
			return userPhotoAnonymously( photo );
		}

		return userPhotoWithAuthor( photo );
	}

	private BreadcrumbsBuilder userPhotoWithAuthor( final Photo photo ) {
		return portalPage( services )
			.photoGalleryLink()
			.photosByGenre( photo.getGenreId() )
			.userCardLink( photo.getUserId() )
			.photosByUser( photo.getUserId() )
			.photosByUserAndGenre( photo.getUserId(), photo.getGenreId() )
			;
	}

	private BreadcrumbsBuilder userPhotoAnonymously( final Photo photo ) {
		return portalPage( services )
			.photoGalleryLink()
			.photosByGenre( photo.getGenreId() )
			.anonymousUser()
			;
	}
}
