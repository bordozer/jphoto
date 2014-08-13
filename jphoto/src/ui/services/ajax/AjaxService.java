package ui.services.ajax;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteUserDTO;
import rest.users.picker.UserDTO;
import ui.dtos.AjaxResultDTO;
import ui.dtos.CommentDTO;
import ui.dtos.ComplaintMessageDTO;
import ui.dtos.PrivateMessageSendingDTO;

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

	void restrictEntryForPeriod( final int entryId, final int period, final int unitId, final String[] restrictionTypeIds );

	void restrictEntryForRange( final int entryId, final long _timeFrom, final long _timeTo, final String[] restrictionTypeIds );

	String translate( final String nerd );
}
