package core.services.dao;

import core.general.photo.PhotoComment;

import java.util.Date;
import java.util.List;

public interface PhotoCommentDao extends BaseEntityDao<PhotoComment> {

	List<Integer> loadAllIds( final int photoId );

	List<Integer> loadRootCommentsIds( final int photoId );

	List<Integer> loadUserCommentsIds( final int userId );

	List<Integer> loadCommentsToUserPhotosIds( final int userId );

	List<Integer> loadAnswersOnCommentIds( final int commentId );

	List<Integer> loadUnreadCommentsToUserIds( final int userId );

	void setCommentReadTime( final int commentId, final Date time );

	int getUnreadCommentsQty( final int userId );

	List<Integer> getUnreadCommentsIds( final int userId );

	int getWrittenCommentsQty( final int userId );

	int getReceivedCommentsQty( final int userId );

	void deletePhotoComments( final int photoId );

	int getPhotoCommentsCount( final int photoId );

	void markAllUnreadCommentAsRead( final int userId );
}
