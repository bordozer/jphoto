package ui.controllers.comment.list;

import ui.controllers.comment.edit.PhotoCommentInfo;
import ui.context.EnvironmentContext;
import core.general.base.PagingModel;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.photo.PhotoPreviewWrapper;
import core.general.user.User;
import core.services.menu.EntryMenuService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.system.Services;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import utils.PagingUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

@Controller
@RequestMapping( value = "members/{userId}/comments/" )
public class PhotoCommentsListController {

	private static final String MODEL_NAME = "photoCommentsListModel";
	private static final String PAGING_MODEL_NAME = "pagingModel";

	private static final String VIEW = "comments/list/CommentList";

	private static final int COMMENTS_ON_PAGE = 10;

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private BreadcrumbsUserService breadcrumbsUserService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private EntryMenuService entryMenuService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private Services services;

	@ModelAttribute( MODEL_NAME )
	public PhotoCommentsListModel prepareModel( final @PathVariable( "userId" ) int userId ) {
		final PhotoCommentsListModel model = new PhotoCommentsListModel();

		final User user = userService.load( userId );
		model.setUser( user );

		return model;
	}
	
	@ModelAttribute
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel( services );

		pagingModel.setCurrentPage( PagingUtils.getPageFromRequest( request ) );
		pagingModel.setItemsOnPage( COMMENTS_ON_PAGE );

		return pagingModel;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/received/" )
	public String commentsToUser( final @ModelAttribute( MODEL_NAME ) PhotoCommentsListModel model, final @ModelAttribute( PAGING_MODEL_NAME ) PagingModel pagingModel ) {
		final User user = model.getUser();

		final List<Integer> commentIds = photoCommentService.loadCommentsToUserPhotosIds( user.getId() );

		final List<PhotoCommentInfo> photoCommentInfos = getCommentsInfos( commentIds, pagingModel );

		model.setPhotoCommentInfoMap( getPhotoCommentInfoMap( photoCommentInfos ) );

		model.setShowPaging( true );

		model.setPageTitleData( breadcrumbsUserService.getUserReceivedCommentsBreadcrumb( user ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/received/unread/" )
	public String unreadCommentsToUser( final @ModelAttribute( MODEL_NAME ) PhotoCommentsListModel model, final @ModelAttribute( PAGING_MODEL_NAME ) PagingModel pagingModel ) {

		final User user = model.getUser();

		final List<Integer> commentIds = photoCommentService.loadUnreadCommentsToUserIds( user.getId() );

		final List<PhotoCommentInfo> photoCommentInfos = getCommentsInfos( commentIds, pagingModel );

		model.setPhotoCommentInfoMap( getPhotoCommentInfoMap( photoCommentInfos ) );

		model.setShowPaging( false );

		model.setPageTitleData( breadcrumbsUserService.getUserWrittenUnreadCommentsBreadcrumb( user ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/written/" )
	public String userComments( final @ModelAttribute( MODEL_NAME ) PhotoCommentsListModel model, final @ModelAttribute( PAGING_MODEL_NAME ) PagingModel pagingModel ) {

		final User user = model.getUser();

		final List<Integer> commentIds = photoCommentService.loadUserCommentsIds( user.getId() );

		final List<PhotoCommentInfo> photoCommentInfos = getCommentsInfos( commentIds, pagingModel );

		model.setPhotoCommentInfoMap( getPhotoCommentInfoMap( photoCommentInfos ) );

		model.setShowPaging( true );

		model.setPageTitleData( breadcrumbsUserService.getUserWrittenCommentsBreadcrumb( user ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/received/markAllAsRead" )
	public String markAllCommentsAsRead( final @ModelAttribute( MODEL_NAME ) PhotoCommentsListModel model ) {

		final User user = model.getUser();

		securityService.assertUserEqualsToCurrentUser( user );

		photoCommentService.markAllUnreadCommentAsRead( user.getId() );

		return String.format( "redirect:%s/members/%d/comments/received/", urlUtilsService.getBaseURLWithPrefix(), user.getId() );
	}

	private List<PhotoCommentInfo> getCommentsInfos( final List<Integer> commentIds, final PagingModel pagingModel ) {

		final int startIndex = PagingUtils.getPageItemStartIndex( pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
		final int endIndex = PagingUtils.getPageItemEndIndex( pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );

		pagingModel.setTotalItems( commentIds.size() );

		int counter = 0;
		int photoId = 0;
		int prevPagePhotoId = 0;
		int extraShownComments = 0;
		final List<PhotoCommentInfo> photoCommentInfos = newArrayList();
		for ( final Integer commentId : commentIds ) {
			if ( counter == startIndex - 1 ) {
				// comment's photo from previous page - all comments was shown there so we have to skip this comments on thi page
				final PhotoComment comment = photoCommentService.load( commentId );
				final Photo photo = photoService.load( comment.getPhotoId() );
				prevPagePhotoId = photo.getId();
			}

			if ( counter >= startIndex && counter <= endIndex ) {

				final PhotoComment comment = photoCommentService.load( commentId );
				final Photo photo = photoService.load( comment.getPhotoId() );

				if ( prevPagePhotoId == photo.getId() ) {
					continue;
				}

				photoCommentInfos.add( photoCommentService.getPhotoCommentInfoWithChild( comment, entryMenuService.getCommentComplaintOnlyMenuItems(), EnvironmentContext.getCurrentUser() ) );

				markCommentAsReadIfNecessary( photo, comment );

				photoId = comment.getPhotoId();
			}

			if ( counter > endIndex ) {
				// extra comments that technically should be on the next page
				// but belongs to the photo shown on this page so will be shown here ans will be skipped at the next page
				final PhotoComment comment = photoCommentService.load( commentId );
				if ( comment.getPhotoId() == photoId ) {
					final Photo photo = photoService.load( comment.getPhotoId() );

					photoCommentInfos.add( photoCommentService.getPhotoCommentInfoWithChild( comment, entryMenuService.getCommentComplaintOnlyMenuItems(), EnvironmentContext.getCurrentUser() ) );

					markCommentAsReadIfNecessary( photo, comment );

					extraShownComments++;
				} else {
					break;
				}
			}
			counter++;
		}

		if ( counter == commentIds.size() ) {
			// to prevent showing the last page that is empty because comments from the last page is shown on the previous one
			pagingModel.setTotalItems( pagingModel.getTotalItems() - extraShownComments );
		}

		return photoCommentInfos;
	}

	private Map<PhotoPreviewWrapper, List<PhotoCommentInfo>> getPhotoCommentInfoMap( final List<PhotoCommentInfo> photoCommentInfos ) {
		final Map<PhotoPreviewWrapper, List<PhotoCommentInfo>> photoCommentInfoMap = newLinkedHashMap();

		for ( final PhotoCommentInfo photoCommentInfo : photoCommentInfos ) {
			final Photo photo = photoCommentInfo.getPhoto();

			final PhotoPreviewWrapper previewWrapper = photoService.getPhotoPreviewWrapper( photo, EnvironmentContext.getCurrentUser() );

			if ( photoCommentInfoMap.containsKey( previewWrapper ) ) {
				photoCommentInfoMap.get( previewWrapper ).add( photoCommentInfo );
			} else {
				photoCommentInfoMap.put( previewWrapper, newArrayList( photoCommentInfo ) );
			}
		}

		return photoCommentInfoMap;
	}

	// TODO: duplicates with PhotoCardController.java
	private void markCommentAsReadIfNecessary( final Photo photo, final PhotoComment comment ) {
		final User currentUser = EnvironmentContext.getCurrentUser();
		if ( dateUtilsService.isEmptyTime( comment.getReadTime() ) && securityService.userOwnThePhoto( currentUser, photo ) ) {
			photoCommentService.setCommentReadTime( comment.getId(), dateUtilsService.getCurrentTime() );
		}
	}
}
