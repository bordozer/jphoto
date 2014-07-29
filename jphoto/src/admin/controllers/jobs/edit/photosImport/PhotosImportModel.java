package admin.controllers.jobs.edit.photosImport;

import admin.controllers.jobs.edit.DateRangableModel;
import core.enums.UserGender;
import core.general.user.User;
import org.apache.commons.lang.StringUtils;
import ui.translatable.GenericTranslatableList;

import java.util.List;


public class PhotosImportModel extends DateRangableModel {

	private PhotosImportSource importSource;

	public final static String FORM_CONTROL_PHOTOS_IMPORT_SOURCE_ID = "importSourceId";

	// file system import fields -->
	public final static String PICTURE_DIR_FORM_CONTROL = "pictureDir";
	public final static String PHOTO_QTY_LIMIT_FORM_CONTROL = "photoQtyLimit";
	public final static String DELETE_PICTURE_AFTER_IMPORT_CONTROL = "deletePictureFromDiskAfterImport";
	public final static String USER_ID_FORM_CONTROL = "assignAllGeneratedPhotosToUserId";

	private String pictureDir;
	private String photoQtyLimit = "5";
	private int assignAllGeneratedPhotosToUserId;
	private User assignAllGeneratedPhotosToUser;
	// file system import fields<--

	// photosight import fields -->
	public final static String FORM_CONTROL_PHOTOSIGHT_USER_ID = "remotePhotoSiteUserIds";
	public final static String FORM_CONTROL_USER_NAME = "userName";
	public final static String ACTIONS_QTY_FORM_CONTROL = "pageQty";
	public final static String USER_GENDER_ID_FORM_CONTROL = "userGenderId";
	public final static String USER_MEMBERSHIP_ID_FORM_CONTROL = "userMembershipId";
	public final static String IMPORT_COMMENTS_FORM_CONTROL = "importComments";
	public final static String BREAK_IMPORT_IF_ALREADY_IMPORTED_PHOTO_FOUND_FORM_CONTROL = "breakImportIfAlreadyImportedPhotoFound";
	public static final String PHOTOSIGHT_CATEGORIES_FORM_CONTROL = "remotePhotoSiteCategories";

	public final static String DELAY_BETWEEN_REQUEST_FORM_CONTROL = "delayBetweenRequest";
	private String remotePhotoSiteUserIds;

	private String pageQty;
	private String userGenderId;
	private String userMembershipId;
	private boolean importComments;
	private boolean breakImportIfAlreadyImportedPhotoFound;
	private String delayBetweenRequest;
	private List<String> remotePhotoSiteCategories;

	// photosight import fields <--
	private boolean deletePictureFromDiskAfterImport;
	
	private GenericTranslatableList userMembershipTypeTranslatableList;
	private GenericTranslatableList<UserGender> userGenderTranslatableList;
	private GenericTranslatableList<PhotosImportSource> photosImportSourceTranslatableList;

	private boolean remotePhotoSiteImport_importNudeContentByDefault;

	public PhotosImportSource getImportSource() {
		return importSource;
	}

	public void setImportSource( final PhotosImportSource importSource ) {
		this.importSource = importSource;
	}

	public int getImportSourceId() {
		return importSource.getId();
	}

	public void setImportSourceId( final int importSourceId ) {
		importSource = PhotosImportSource.getById( importSourceId );
	}

	public String getPictureDir() {
		return pictureDir;
	}

	public void setPictureDir( final String pictureDir ) {
		this.pictureDir = pictureDir;
	}

	public String getPhotoQtyLimit() {
		return photoQtyLimit;
	}

	public void setPhotoQtyLimit( final String photoQtyLimit ) {
		this.photoQtyLimit = photoQtyLimit;
	}

	public int getAssignAllGeneratedPhotosToUserId() {
		return assignAllGeneratedPhotosToUserId;
	}

	public void setAssignAllGeneratedPhotosToUserId( final int assignAllGeneratedPhotosToUserId ) {
		this.assignAllGeneratedPhotosToUserId = assignAllGeneratedPhotosToUserId;
	}

	public User getAssignAllGeneratedPhotosToUser() {
		return assignAllGeneratedPhotosToUser;
	}

	public void setAssignAllGeneratedPhotosToUser( final User assignAllGeneratedPhotosToUser ) {
		this.assignAllGeneratedPhotosToUser = assignAllGeneratedPhotosToUser;
	}

	public String getRemotePhotoSiteUserIds() {
		return remotePhotoSiteUserIds;
	}

	public void setRemotePhotoSiteUserIds( final String remotePhotoSiteUserIds ) {
		this.remotePhotoSiteUserIds = remotePhotoSiteUserIds;
	}

	public String getPageQty() {
		return pageQty;
	}

	public void setPageQty( final String pageQty ) {
		this.pageQty = pageQty;
	}

	public String getUserGenderId() {
		return userGenderId;
	}

	public void setUserGenderId( final String userGenderId ) {
		this.userGenderId = userGenderId;
	}

	public String getUserMembershipId() {
		return userMembershipId;
	}

	public void setUserMembershipId( final String userMembershipId ) {
		this.userMembershipId = userMembershipId;
	}

	public boolean isImportComments() {
		return importComments;
	}

	public void setImportComments( final boolean importComments ) {
		this.importComments = importComments;
	}

	public boolean isBreakImportIfAlreadyImportedPhotoFound() {
		return breakImportIfAlreadyImportedPhotoFound;
	}

	public void setBreakImportIfAlreadyImportedPhotoFound( final boolean breakImportIfAlreadyImportedPhotoFound ) {
		this.breakImportIfAlreadyImportedPhotoFound = breakImportIfAlreadyImportedPhotoFound;
	}

	public String getDelayBetweenRequest() {
		return delayBetweenRequest;
	}

	public void setDelayBetweenRequest( final String delayBetweenRequest ) {
		this.delayBetweenRequest = delayBetweenRequest;
	}

	public boolean isDeletePictureFromDiskAfterImport() {
		return deletePictureFromDiskAfterImport;
	}

	public void setDeletePictureFromDiskAfterImport( final boolean deletePictureFromDiskAfterImport ) {
		this.deletePictureFromDiskAfterImport = deletePictureFromDiskAfterImport;
	}

	public List<String> getRemotePhotoSiteCategories() {
		return remotePhotoSiteCategories;
	}

	public void setRemotePhotoSiteCategories( final List<String> remotePhotoSiteCategories ) {
		this.remotePhotoSiteCategories = remotePhotoSiteCategories;
	}

	public void setUserMembershipTypeTranslatableList( final GenericTranslatableList userMembershipTypeTranslatableList ) {
		this.userMembershipTypeTranslatableList = userMembershipTypeTranslatableList;
	}

	public GenericTranslatableList getUserMembershipTypeTranslatableList() {
		return userMembershipTypeTranslatableList;
	}

	public void setUserGenderTranslatableList( final GenericTranslatableList<UserGender> userGenderTranslatableList ) {
		this.userGenderTranslatableList = userGenderTranslatableList;
	}

	public GenericTranslatableList<UserGender> getUserGenderTranslatableList() {
		return userGenderTranslatableList;
	}

	public boolean isRemotePhotoSiteImport_importNudeContentByDefault() {
		return remotePhotoSiteImport_importNudeContentByDefault;
	}

	public void setRemotePhotoSiteImport_importNudeContentByDefault( final boolean remotePhotoSiteImport_importNudeContentByDefault ) {
		this.remotePhotoSiteImport_importNudeContentByDefault = remotePhotoSiteImport_importNudeContentByDefault;
	}

	@Override
	public void clear() {
		super.clear();

		pageQty = StringUtils.EMPTY;
		delayBetweenRequest = StringUtils.EMPTY;
		deletePictureFromDiskAfterImport = false;
	}

	public void setPhotosImportSourceTranslatableList( final GenericTranslatableList<PhotosImportSource> photosImportSourceTranslatableList ) {
		this.photosImportSourceTranslatableList = photosImportSourceTranslatableList;
	}

	public GenericTranslatableList<PhotosImportSource> getPhotosImportSourceTranslatableList() {
		return photosImportSourceTranslatableList;
	}
}
