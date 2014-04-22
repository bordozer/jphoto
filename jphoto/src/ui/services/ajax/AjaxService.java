package ui.services.ajax;

import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightUserDTO;
import ui.dtos.*;

import java.util.List;

public interface AjaxService {

	String BEAN_NAME = "ajaxService";

	AjaxResultDTO sendComplaintMessageAjax( final ComplaintMessageDTO complaintMessageDTO );

	PhotosightUserDTO getPhotosightUserDTO( final String _photosightUserId );

	AjaxResultDTO addEntryToFavoritesAjax( final int userId, final int photoId, final int entryTypeId );

	AjaxResultDTO removeEntryFromFavoritesAjax( final int userId, final int photoId, final int entryTypeId );

	// Transactional
	AjaxResultDTO sendPrivateMessageAjax( final PrivateMessageSendingDTO messageDTO );

	CommentDTO markCommentAsDeletedAjax( final int userId, final int commentId );

	List<UserPickerDTO> userLinkAjax( final String searchString );

	long getUserDelayToNextCommentAjax( final int userId );

	CommentDTO getCommentDTOAjax( final int commentId );

	boolean isUserCanCommentPhotosAjax( final int userId );

	boolean isEntryInFavoritesAjax( final int userWhoIsAddingToFavorites, final int beingAddedEntryId, final int entryTypeId );

	void lockUser( final int userId, final String timeFrom, final String timeTo );

	void lockPhoto( final int photoId, final String timeFrom, final String timeTo );
}
