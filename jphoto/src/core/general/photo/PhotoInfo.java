package core.general.photo;

import core.general.genre.Genre;
import core.general.user.User;
import core.general.menus.EntryMenu;
import core.general.photoTeam.PhotoTeam;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.interfaces.Cacheable;
import controllers.users.card.MarksByCategoryInfo;

import java.util.Date;
import java.util.List;

public class PhotoInfo implements Cacheable {

	private Photo photo;

	private User user;
	private String photoAuthorAnonymousName;
	private boolean isPhotoAuthorNameMustBeHidden;
	private boolean isPhotoPreviewMustBeHidden;
	private Date photoAnonymousPeriodExpirationTime;
	private Genre genre;

	private int todayMarks;
	private int topBestMarks;
	private int totalMarks;

	private int previewCount;

	private List<MarksByCategoryInfo> marksByCategoryInfos;

	private List<PhotoAward> photoAwards;

	private boolean showPhotoRatingPosition;
	private int photoRatingPosition;
	private String photoRatingPositionDescription;

	private PhotoTeam photoTeam;
	private List<UserPhotoAlbum> userPhotoAlbums;
	private boolean photoPreviewHasToBeHiddenBecauseOfNudeContent;

	private boolean userCanEditPhoto;
	private boolean userCanDeletePhoto;

	private boolean superAdminUser;

	private int photoAuthorRankInGenre;
	private boolean photoAuthorHasEnoughPhotosInGenre;

	private int commentsCount;

	private boolean showStatisticInPhotoList;
	private boolean showUserRankInGenreInPhotoList;

	private String photoImgUrl;
	private String photoPreviewImgUrl;
	private EntryMenu photoMenu;
	private EntryMenu photoAuthorMenu;

	public PhotoInfo( final Photo photo ) {
		this.photo = photo;
	}

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}

	public Photo getPhoto() {
		return photo;
	}

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public String getPhotoAuthorAnonymousName() {
		return photoAuthorAnonymousName;
	}

	public void setPhotoAuthorAnonymousName( final String photoAuthorAnonymousName ) {
		this.photoAuthorAnonymousName = photoAuthorAnonymousName;
	}

	public boolean isPhotoAuthorNameMustBeHidden() {
		return isPhotoAuthorNameMustBeHidden;
	}

	public void setPhotoAuthorNameMustBeHidden( final boolean photoAuthorNameMustBeHidden ) {
		isPhotoAuthorNameMustBeHidden = photoAuthorNameMustBeHidden;
	}

	public boolean isPhotoPreviewMustBeHidden() {
		return isPhotoPreviewMustBeHidden;
	}

	public void setPhotoPreviewMustBeHidden( final boolean photoPreviewMustBeHidden ) {
		isPhotoPreviewMustBeHidden = photoPreviewMustBeHidden;
	}

	public Date getPhotoAnonymousPeriodExpirationTime() {
		return photoAnonymousPeriodExpirationTime;
	}

	public void setPhotoAnonymousPeriodExpirationTime( final Date photoAnonymousPeriodExpirationTime ) {
		this.photoAnonymousPeriodExpirationTime = photoAnonymousPeriodExpirationTime;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre( final Genre genre ) {
		this.genre = genre;
	}

	public int getTodayMarks() {
		return todayMarks;
	}

	public void setTodayMarks( final int todayMarks ) {
		this.todayMarks = todayMarks;
	}

	public int getTopBestMarks() {
		return topBestMarks;
	}

	public void setTopBestMarks( final int topBestMarks ) {
		this.topBestMarks = topBestMarks;
	}

	public int getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks( final int totalMarks ) {
		this.totalMarks = totalMarks;
	}

	public List<MarksByCategoryInfo> getMarksByCategoryInfos() {
		return marksByCategoryInfos;
	}

	public void setMarksByCategoryInfos( final List<MarksByCategoryInfo> marksByCategoryInfos ) {
		this.marksByCategoryInfos = marksByCategoryInfos;
	}

	public int getPreviewCount() {
		return previewCount;
	}

	public void setPreviewCount( final int previewCount ) {
		this.previewCount = previewCount;
	}

	public int getPhotoRatingPosition() {
		return photoRatingPosition;
	}

	public void setPhotoRatingPosition( final int photoRatingPosition ) {
		this.photoRatingPosition = photoRatingPosition;
	}

	public List<PhotoAward> getPhotoAwards() {
		return photoAwards;
	}

	public void setPhotoAwards( final List<PhotoAward> photoAwards ) {
		this.photoAwards = photoAwards;
	}

	public boolean isShowPhotoRatingPosition() {
		return showPhotoRatingPosition;
	}

	public void setShowPhotoRatingPosition( final boolean showPhotoRatingPosition ) {
		this.showPhotoRatingPosition = showPhotoRatingPosition;
	}

	public String getPhotoRatingPositionDescription() {
		return photoRatingPositionDescription;
	}

	public void setPhotoRatingPositionDescription( final String photoRatingPositionDescription ) {
		this.photoRatingPositionDescription = photoRatingPositionDescription;
	}

	public PhotoTeam getPhotoTeam() {
		return photoTeam;
	}

	public void setPhotoTeam( final PhotoTeam photoTeam ) {
		this.photoTeam = photoTeam;
	}

	public List<UserPhotoAlbum> getUserPhotoAlbums() {
		return userPhotoAlbums;
	}

	public void setUserPhotoAlbums( final List<UserPhotoAlbum> userPhotoAlbums ) {
		this.userPhotoAlbums = userPhotoAlbums;
	}

	public boolean isPhotoPreviewHasToBeHiddenBecauseOfNudeContent() {
		return photoPreviewHasToBeHiddenBecauseOfNudeContent;
	}

	public void setPhotoPreviewHasToBeHiddenBecauseOfNudeContent( final boolean photoPreviewHasToBeHiddenBecauseOfNudeContent ) {
		this.photoPreviewHasToBeHiddenBecauseOfNudeContent = photoPreviewHasToBeHiddenBecauseOfNudeContent;
	}

	public boolean isUserCanEditPhoto() {
		return userCanEditPhoto;
	}

	public void setUserCanEditPhoto( final boolean userCanEditPhoto ) {
		this.userCanEditPhoto = userCanEditPhoto;
	}

	public boolean isUserCanDeletePhoto() {
		return userCanDeletePhoto;
	}

	public void setUserCanDeletePhoto( final boolean userCanDeletePhoto ) {
		this.userCanDeletePhoto = userCanDeletePhoto;
	}

	public boolean isSuperAdminUser() {
		return superAdminUser;
	}

	public void setSuperAdminUser( final boolean superAdminUser ) {
		this.superAdminUser = superAdminUser;
	}

	public int getPhotoAuthorRankInGenre() {
		return photoAuthorRankInGenre;
	}

	public void setPhotoAuthorRankInGenre( final int photoAuthorRankInGenre ) {
		this.photoAuthorRankInGenre = photoAuthorRankInGenre;
	}

	public boolean isPhotoAuthorHasEnoughPhotosInGenre() {
		return photoAuthorHasEnoughPhotosInGenre;
	}

	public void setPhotoAuthorHasEnoughPhotosInGenre( final boolean photoAuthorHasEnoughPhotosInGenre ) {
		this.photoAuthorHasEnoughPhotosInGenre = photoAuthorHasEnoughPhotosInGenre;
	}

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount( final int commentsCount ) {
		this.commentsCount = commentsCount;
	}

	public String getPhotoImgUrl() {
		return photoImgUrl;
	}

	public void setPhotoImgUrl( final String photoImgUrl ) {
		this.photoImgUrl = photoImgUrl;
	}

	public String getPhotoPreviewImgUrl() {
		return photoPreviewImgUrl;
	}

	public void setPhotoPreviewImgUrl( final String photoPreviewImgUrl ) {
		this.photoPreviewImgUrl = photoPreviewImgUrl;
	}

	public EntryMenu getPhotoMenu() {
		return photoMenu;
	}

	public void setPhotoMenu( final EntryMenu photoMenu ) {
		this.photoMenu = photoMenu;
	}

	public EntryMenu getPhotoAuthorMenu() {
		return photoAuthorMenu;
	}

	public void setPhotoAuthorMenu( final EntryMenu photoAuthorMenu ) {
		this.photoAuthorMenu = photoAuthorMenu;
	}

	@Override
	public String toString() {
		return String.format( "Photo info: %s", photo );
	}

	public boolean isShowStatisticInPhotoList() {
		return showStatisticInPhotoList;
	}

	public void setShowStatisticInPhotoList( final boolean showStatisticInPhotoList ) {
		this.showStatisticInPhotoList = showStatisticInPhotoList;
	}

	public boolean isShowUserRankInGenreInPhotoList() {
		return showUserRankInGenreInPhotoList;
	}

	public void setShowUserRankInGenreInPhotoList( final boolean showUserRankInGenreInPhotoList ) {
		this.showUserRankInGenreInPhotoList = showUserRankInGenreInPhotoList;
	}
}
