package controllers.photos.groupoperations;

import core.general.base.AbstractGeneralModel;
import core.general.photo.group.PhotoGroupOperationType;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class PhotoGroupOperationModel extends AbstractGeneralModel {

	public static final String FORM_CONTROL_PHOTO_GROUP_OPERATION_ID = "photoGroupOperationId";
	public static final String FORM_CONTROL_SELECTED_PHOTO_IDS = "selectedPhotoIds";
	public static final String FORM_CONTROL_PHOTO_ALBUM_ID = "photoAlbumId";
	public static final String FORM_CONTROL_MOVE_TO_GENRE_ID = "moveToGenreId";

	private String photoGroupOperationId;

	private PhotoGroupOperationType photoGroupOperationType;

	private List<String> selectedPhotoIds = newArrayList();

	private List<PhotoGroupOperationEntry> photoGroupOperationEntries;
	private List<UserPhotoAlbum> userPhotoAlbums;

	private List<UserTeamMember> userTeamMembers;

	private Map<String, PhotoGroupOperationEntryProperty> photoGroupOperationEntryPropertiesMap = newLinkedHashMap();
	private List<GroupOperationResult> operationResults;

	private int moveToGenreId;
	private List<GenreEntry> genreEntries;

	private String returnUrl;

	public String getPhotoGroupOperationId() {
		return photoGroupOperationId;
	}

	public void setPhotoGroupOperationId( final String photoGroupOperationId ) {
		this.photoGroupOperationId = photoGroupOperationId;
	}

	public List<String> getSelectedPhotoIds() {
		return selectedPhotoIds;
	}

	public void setSelectedPhotoIds( final List<String> selectedPhotoIds ) {
		this.selectedPhotoIds = selectedPhotoIds;
	}

	public List<PhotoGroupOperationEntry> getPhotoGroupOperationEntries() {
		return photoGroupOperationEntries;
	}

	public void setPhotoGroupOperationEntries( final List<PhotoGroupOperationEntry> photoGroupOperationEntries ) {
		this.photoGroupOperationEntries = photoGroupOperationEntries;
	}

	public PhotoGroupOperationType getPhotoGroupOperationType() {
		return photoGroupOperationType;
	}

	public void setPhotoGroupOperationType( final PhotoGroupOperationType photoGroupOperationType ) {
		this.photoGroupOperationType = photoGroupOperationType;
	}

	public void setUserPhotoAlbums( final List<UserPhotoAlbum> userPhotoAlbums ) {
		this.userPhotoAlbums = userPhotoAlbums;
	}

	public List<UserPhotoAlbum> getUserPhotoAlbums() {
		return userPhotoAlbums;
	}

	public List<UserTeamMember> getUserTeamMembers() {
		return userTeamMembers;
	}

	public void setUserTeamMembers( final List<UserTeamMember> userTeamMembers ) {
		this.userTeamMembers = userTeamMembers;
	}

	public Map<String, PhotoGroupOperationEntryProperty> getPhotoGroupOperationEntryPropertiesMap() {
		return photoGroupOperationEntryPropertiesMap;
	}

	public void setPhotoGroupOperationEntryPropertiesMap( final Map<String, PhotoGroupOperationEntryProperty> photoGroupOperationEntryPropertiesMap ) {
		this.photoGroupOperationEntryPropertiesMap = photoGroupOperationEntryPropertiesMap;
	}

	public List<GroupOperationResult> getOperationResults() {
		return operationResults;
	}

	public void setOperationResults( final List<GroupOperationResult> operationResults ) {
		this.operationResults = operationResults;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl( final String returnUrl ) {
		this.returnUrl = returnUrl;
	}

	public int getMoveToGenreId() {
		return moveToGenreId;
	}

	public void setMoveToGenreId( final int moveToGenreId ) {
		this.moveToGenreId = moveToGenreId;
	}

	public List<GenreEntry> getGenreEntries() {
		return genreEntries;
	}

	public void setGenreEntries( final List<GenreEntry> genreEntries ) {
		this.genreEntries = genreEntries;
	}

	@Override
	public void clear() {
		super.clear();

		photoGroupOperationEntries = null;
		photoGroupOperationType = null;
		userPhotoAlbums = null;
		photoGroupOperationEntryPropertiesMap = newLinkedHashMap();
		operationResults = null;
		returnUrl = null;
		moveToGenreId = 0;
	}
}
