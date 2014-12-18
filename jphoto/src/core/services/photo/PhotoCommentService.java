package core.services.photo;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.interfaces.Archivable;
import core.interfaces.BaseEntityService;
import ui.controllers.comment.edit.PhotoCommentInfo;
import ui.services.menu.entry.items.EntryMenuData;

import java.util.Date;
import java.util.List;

public interface PhotoCommentService extends BaseEntityService<PhotoComment>, Archivable {

	String BEAN_NAME = "photoCommentService";

	List<PhotoComment> loadAll( final int photoId );

	List<Integer> loadRootCommentsIds( final int photoId );

	List<PhotoComment> loadCommentsWithoutParent( final int photoId );

	List<PhotoComment> loadAnswersOnComment( final int commentId );

	List<Integer> loadUserCommentsIds( final int userId );

	List<Integer> loadCommentsToUserPhotosIds( final int userId );

	List<Integer> loadUnreadCommentsToUserIds( final int userId );

	void setCommentReadTime( final int commentId, final Date time );

	int getUnreadCommentsQty( final int userId );

	Date getUserLastCommentTime( final int userId );

	Date getUserNextCommentTime( final int userId );

	int getUserDelayBetweenCommentsSec( final User user );

	long getUserDelayToNextComment( final int userId );

	boolean isUserCanCommentPhotos( final int userId );

	PhotoCommentInfo getPhotoCommentInfo( final PhotoComment photoComment, final User accessor );

	PhotoCommentInfo getPhotoCommentInfo( final PhotoComment photoComment, final List<EntryMenuData> entryMenuDataList, final User accessor );

	PhotoCommentInfo getPhotoCommentInfoWithChild( final PhotoComment photoComment, final User accessor );

	PhotoCommentInfo getPhotoCommentInfoWithChild( final PhotoComment photoComment, final List<EntryMenuData> entryMenuDataList, final User accessor );

	void deletePhotoComments( final int photoId );

	int getPhotoCommentsCount( final int photoId );

	int getPhotoCommentsCount();

	void markAllUnreadCommentAsRead( final int userId );
}
