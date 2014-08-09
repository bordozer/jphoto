package ui.services.ajax;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteUserDTO;
import rest.users.picker.UserDTO;
import ui.dtos.*;

import java.util.List;

public interface AjaxService {

	String BEAN_NAME = "ajaxService";

	AjaxResultDTO sendComplaintMessageAjax( final ComplaintMessageDTO complaintMessageDTO );

	RemotePhotoSiteUserDTO getRemoteUserDTO( final String _remoteUserId, final String _importSourceId );

	AjaxResultDTO addEntryToFavoritesAjax( final int userId, final int photoId, final int entryTypeId );

	AjaxResultDTO removeEntryFromFavoritesAjax( final int userId, final int photoId, final int entryTypeId );

	// Transactional
	AjaxResultDTO sendPrivateMessageAjax( final PrivateMessageSendingDTO messageDTO );

	CommentDTO markCommentAsDeletedAjax( final int userId, final int commentId );

	List<UserDTO> userLinkAjax( final String searchString );

	long getUserDelayToNextCommentAjax( final int userId );

	CommentDTO getCommentDTOAjax( final int commentId );

	boolean isUserCanCommentPhotosAjax( final int userId );

	boolean isEntryInFavoritesAjax( final int userWhoIsAddingToFavorites, final int beingAddedEntryId, final int entryTypeId );

	void setPhotoNudeContent( final int photoId, final boolean isNudeContent );

	void restrictUserPeriod( final int userId, final int period, final int unitId, final String[] restrictionTypeIds );

	void restrictUserRange( final int userId, final String timeFrom, final String timeTo, final List<Integer> restrictionTypeIds );

	void lockPhoto( final int photoId, final String timeFrom, final String timeTo, final List<Integer> restrictionTypeIds );
}
