package core.services.pageTitle;

import core.general.user.User;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import utils.TranslatorUtils;

public class PageTitlePhotoCommentUtilsServiceImpl implements PageTitlePhotoCommentUtilsService {

	public static final String PHOTO_COMMENTS_ROOT = "comments";

	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Override
	public PageTitleData getPhotoCommentsToUserData( final User user ) {
		final String rootTranslated = getPhotoRootTranslated();
		final String comments = TranslatorUtils.translate( "Comments" );

		final String title = pageTitleUtilsService.getTitleDataString( user.getName(), comments );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), entityLinkUtilsService.getUserCardLink( user ), comments );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUnreadPhotoCommentsToUserData( final User user ) {
		final String rootTranslated = getPhotoRootTranslated();
		final String comments = TranslatorUtils.translate( "Comments" );
		final String unread = TranslatorUtils.translate( "Unread" );

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
		return TranslatorUtils.translate( PHOTO_COMMENTS_ROOT );
	}
}
