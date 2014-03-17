package admin.controllers.jobs.edit.photosImport;

import admin.controllers.jobs.edit.DateRangableModel;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightCategory;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import core.general.user.User;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotosImportModel extends DateRangableModel {

	private PhotosImportSource importSource;

	public final static String FORM_CONTROL_PHOTOS_IMPORT_SOURCE_ID = "importSourceId";

	// file system import fields -->
	public final static String PICTURE_DIR_FORM_CONTROL = "pictureDir";
	public final static String PHOTO_QTY_LIMIT_FORM_CONTROL = "photoQtyLimit";
	public final static String DELETE_PICTURE_AFTER_IMPORT_CONTROL = "deletePictureFromDiskAfterImport";
	public final static String USER_ID_FORM_CONTROL = "assignAllGeneratedPhotosToUserId";

	private String pictureDir = "/home/blu/Pictures/"; // TODO: this is default value for dev time
	private String photoQtyLimit = "5";
	private int assignAllGeneratedPhotosToUserId;
	private User assignAllGeneratedPhotosToUser;
	// file system import fields<--

	// photosight import fields -->
	public final static String FORM_CONTROL_PHOTOSIGHT_USER_ID = "photosightUserId";
	public final static String FORM_CONTROL_USER_NAME = "userName";
	public final static String ACTIONS_QTY_FORM_CONTROL = "pageQty";
	public final static String USER_GENDER_ID_FORM_CONTROL = "userGenderId";
	public final static String USER_MEMBERSHIP_ID_FORM_CONTROL = "userMembershipId";
	public final static String IMPORT_COMMENTS_FORM_CONTROL = "importComments";
	public static final String PHOTOSIGHT_CATEGORIES_FORM_CONTROL = "photosightCategories";

	public final static String DELAY_BETWEEN_REQUEST_FORM_CONTROL = "delayBetweenRequest";
	private String photosightUserId;
	private String userName;

	private String pageQty;
	private String userGenderId;
	private String userMembershipId;
	private boolean importComments;
	private String delayBetweenRequest;
	private List<String> photosightCategories;

	// photosight import fields <--
	private boolean deletePictureFromDiskAfterImport;

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

	public String getPhotosightUserId() {
		return photosightUserId;
	}

	public void setPhotosightUserId( final String photosightUserId ) {
		this.photosightUserId = photosightUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
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

	public List<String> getPhotosightCategories() {
		return photosightCategories;
	}

	public void setPhotosightCategories( final List<String> photosightCategories ) {
		this.photosightCategories = photosightCategories;
	}

	@Override
	public void clear() {
		super.clear();

		pageQty = StringUtils.EMPTY;
		delayBetweenRequest = StringUtils.EMPTY;
		deletePictureFromDiskAfterImport = false;
	}
}
