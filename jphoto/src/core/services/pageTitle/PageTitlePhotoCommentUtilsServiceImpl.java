package core.services.pageTitle;

import core.general.user.User;
import core.services.translator.TranslatorService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;

public class PageTitlePhotoCommentUtilsServiceImpl implements PageTitlePhotoCommentUtilsService {

	public static final String PHOTO_COMMENTS_ROOT = "comments";

	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public PageTitleData getPhotoCommentsToUserData( final User user ) {
		final String rootTranslated = getPhotoRootTranslated();
		final String comments = translatorService.translate( "Comments" );

		final String title = pageTitleUtilsService.getTitleDataString( user.getName(), comments );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), entityLinkUtilsService.getUserCardLink( user ), comments );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUnreadPhotoCommentsToUserData( final User user ) {
		final String rootTranslated = getPhotoRootTranslated();
		final String comments = translatorService.translate( "Comments" );
		final String unread = translatorService.translate( "Unread" );

		final String title = pageTitleUtilsService.getTitleDataString( user.getName(), comments, unread );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(
			entityLinkUtilsService.getUsersRootLink()
			, entityLinkUtilsService.getUserCardLink( user )
			, entityLinkUtilsService.getPhotoCommentsToUserLink( user )
			, unread
		);

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public String getPhotoRootTranslated() {
		return translatorService.translate( PHOTO_COMMENTS_ROOT );
	}
}
