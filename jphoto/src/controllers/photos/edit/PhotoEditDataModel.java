package controllers.photos.edit;

import controllers.photos.edit.description.AbstractPhotoUploadAllowance;
import core.enums.PhotoActionAllowance;
import core.general.base.AbstractGeneralModel;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoEditDataModel extends AbstractGeneralModel {

	public final static String PHOTO_EDIT_DATA_ID_FORM_CONTROL = "photoId";
	public static final String PHOTO_EDIT_DATA_FILE_FORM_CONTROL = "file";
	public static final String PHOTO_EDIT_DATA_NAME_FORM_CONTROL = "name";
	public static final String PHOTO_EDIT_DATA_GENRE_ID_FORM_CONTROL = "genreId";
	public static final String PHOTO_EDIT_DATA_BGCOLOR_FORM_CONTROL = "bgColor";

	public static final String PHOTO_EDIT_DATA_DESCRIPTION_FORM_CONTROL = "description";
	public static final String PHOTO_EDIT_DATA_KEYWORDS_FORM_CONTROL = "keywords";
	public static final String PHOTO_EDIT_DATA_CONTAINS_NUDE_CONTENT_FORM_CONTROL = "containsNudeContent";
	public static final String FORM_CONTROL_NOTIFICATION_EMAIL_ABOUT_NEW_COMMENT = "notificationEmailAboutNewPhotoComment";

	public static final String FORM_CONTROL_USER_TEAM_MEMBERS_IDS = "photoTeamMemberIds";
	public static final String FORM_CONTROL_PHOTO_ALBUMS_IDS = "photoAlbumIds";

	private int photoId;
	private MultipartFile file;
	private String name;
	private int genreId;
	private String keywords;
	private String description;
	private boolean containsNudeContent;
	private String bgColor;

	private PhotoActionAllowance commentsAllowance = PhotoActionAllowance.CANDIDATES_AND_MEMBERS;
	private int notificationEmailAboutNewPhotoComment;
	private PhotoActionAllowance votingAllowance;

	private List<UserTeamMember> userTeamMembers; 						// all user team's members
	private List<String> photoTeamMemberIds = newArrayList(); 			// selected as photo team user's members IDs
	private List<UserTeamMember> photoTeamMembers = newArrayList();		// selected as photo team user's members

	private List<UserPhotoAlbum> userPhotoAlbums;						// all user's photo albums
	private List<String> photoAlbumIds = newArrayList();				// selected usr photo's album IDs
	private List<UserPhotoAlbum> photoAlbums = newArrayList();			// selected usr photo's albums

	private User photoAuthor;
	private Genre genre;

	private List<Genre> genres;
	private List<Integer> genresCanHaveNudeContent = newArrayList();
	private List<Integer> genresHaveNudeContent = newArrayList();

	private PhotoEditWizardStep currentStep;
	private PhotoEditWizardStep nextStep;

	private AbstractPhotoUploadAllowance uploadAllowance;

	private boolean isAnonymousPosting; // photo posted anonymously
	private boolean isAnonymousDay;		// posting day ai anonymous day

	private List<PhotoActionAllowance> accessibleCommentAllowances;
	private List<PhotoActionAllowance> accessibleVotingAllowances;

	private Date photoUploadTime;

	private int photoNameMaxLength;

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId( final int photoId ) {
		this.photoId = photoId;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile( final MultipartFile file ) {
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public int getGenreId() {
		return genreId;
	}

	public void setGenreId( final int genreId ) {
		this.genreId = genreId;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre( final Genre genre ) {
		this.genre = genre;
	}

	public void setKeywords( final String keywords ) {
		this.keywords = keywords.trim();
	}

	public String getKeywords() {
		return keywords;
	}

	public void setDescription( final String description ) {
		this.description = description.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setContainsNudeContent( final boolean containsNudeContent ) {
		this.containsNudeContent = containsNudeContent;
	}

	public boolean isContainsNudeContent() {
		return containsNudeContent;
	}

	public void setBgColor( final String bgColor ) {
		this.bgColor = bgColor;
	}

	public String getBgColor() {
		return bgColor;
	}

	public User getPhotoAuthor() {
		return photoAuthor;
	}

	public void setPhotoAuthor( final User userId ) {
		this.photoAuthor = userId;
	}

	public void setGenres( final List<Genre> genres ) {
		this.genres = genres;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public List<Integer> getGenresCanHaveNudeContent() {
		return genresCanHaveNudeContent;
	}

	public void setGenresCanHaveNudeContent( final List<Integer> genresCanHaveNudeContent ) {
		this.genresCanHaveNudeContent = genresCanHaveNudeContent;
	}

	public List<Integer> getGenresHaveNudeContent() {
		return genresHaveNudeContent;
	}

	public void setGenresHaveNudeContent( final List<Integer> genresHaveNudeContent ) {
		this.genresHaveNudeContent = genresHaveNudeContent;
	}

	public int getCommentsAllowanceId() {
		return commentsAllowance.getId();
	}

	public void setCommentsAllowanceId( final int commentsAllowanceId ) {
		this.commentsAllowance = PhotoActionAllowance.getById( commentsAllowanceId );
	}

	public PhotoActionAllowance getCommentsAllowance() {
		return commentsAllowance;
	}

	public void setCommentsAllowance( final PhotoActionAllowance commentsAllowance ) {
		this.commentsAllowance = commentsAllowance;
	}

	public int getNotificationEmailAboutNewPhotoComment() {
		return notificationEmailAboutNewPhotoComment;
	}

	public void setNotificationEmailAboutNewPhotoComment( final int notificationEmailAboutNewPhotoComment ) {
		this.notificationEmailAboutNewPhotoComment = notificationEmailAboutNewPhotoComment;
	}

	public int getVotingAllowanceId() {
		return votingAllowance.getId();
	}

	public void setVotingAllowanceId( final int votingAllowanceId ) {
		this.votingAllowance = PhotoActionAllowance.getById( votingAllowanceId );
	}

	public PhotoActionAllowance getVotingAllowance() {
		return votingAllowance;
	}

	public void setVotingAllowance( final PhotoActionAllowance votingAllowance ) {
		this.votingAllowance = votingAllowance;
	}

	public int getCurrentStepId() {
		return currentStep.getId();
	}

	public void setCurrentStepId( final int currentStepId ) {
		this.currentStep = PhotoEditWizardStep.getById( currentStepId );
	}

	public PhotoEditWizardStep getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep( final PhotoEditWizardStep currentStep ) {
		this.currentStep = currentStep;
	}

	public PhotoEditWizardStep getNextStep() {
		return nextStep;
	}

	public void setNextStep( final PhotoEditWizardStep nextStep ) {
		this.nextStep = nextStep;
	}

	public AbstractPhotoUploadAllowance getUploadAllowance() {
		return uploadAllowance;
	}

	public void setUploadAllowance( final AbstractPhotoUploadAllowance uploadAllowance ) {
		this.uploadAllowance = uploadAllowance;
	}

	public List<UserTeamMember> getUserTeamMembers() {
		return userTeamMembers;
	}

	public void setUserTeamMembers( final List<UserTeamMember> userTeamMembers ) {
		this.userTeamMembers = userTeamMembers;
	}

	public List<String> getPhotoTeamMemberIds() {
		return photoTeamMemberIds;
	}

	public void setPhotoTeamMemberIds( final List<String> photoTeamMemberIds ) {
		this.photoTeamMemberIds = photoTeamMemberIds;
	}

	public List<UserPhotoAlbum> getUserPhotoAlbums() {
		return userPhotoAlbums;
	}

	public void setUserPhotoAlbums( final List<UserPhotoAlbum> userPhotoAlbums ) {
		this.userPhotoAlbums = userPhotoAlbums;
	}

	public List<String> getPhotoAlbumIds() {
		return photoAlbumIds;
	}

	public void setPhotoAlbumIds( final List<String> photoAlbumIds ) {
		this.photoAlbumIds = photoAlbumIds;
	}

	public List<UserTeamMember> getPhotoTeamMembers() {
		return photoTeamMembers;
	}

	public void setPhotoTeamMembers( final List<UserTeamMember> photoTeamMembers ) {
		this.photoTeamMembers = photoTeamMembers;
	}

	public List<UserPhotoAlbum> getPhotoAlbums() {
		return photoAlbums;
	}

	public void setPhotoAlbums( final List<UserPhotoAlbum> photoAlbums ) {
		this.photoAlbums = photoAlbums;
	}

	public boolean isAnonymousPosting() {
		return isAnonymousPosting;
	}

	public void setAnonymousPosting( final boolean anonymousPosting ) {
		isAnonymousPosting = anonymousPosting;
	}

	public boolean isAnonymousDay() {
		return isAnonymousDay;
	}

	public void setAnonymousDay( final boolean anonymousDay ) {
		isAnonymousDay = anonymousDay;
	}

	public List<PhotoActionAllowance> getAccessibleCommentAllowances() {
		return accessibleCommentAllowances;
	}

	public void setAccessibleCommentAllowances( final List<PhotoActionAllowance> accessibleCommentAllowance ) {
		this.accessibleCommentAllowances = accessibleCommentAllowance;
	}

	public List<PhotoActionAllowance> getAccessibleVotingAllowances() {
		return accessibleVotingAllowances;
	}

	public void setAccessibleVotingAllowances( final List<PhotoActionAllowance> accessibleVotingAllowance ) {
		this.accessibleVotingAllowances = accessibleVotingAllowance;
	}

	public Date getPhotoUploadTime() {
		return photoUploadTime;
	}

	public void setPhotoUploadTime( final Date photoUploadTime ) {
		this.photoUploadTime = photoUploadTime;
	}

	public int getPhotoNameMaxLength() {
		return photoNameMaxLength;
	}

	public void setPhotoNameMaxLength( final int photoNameMaxLength ) {
		this.photoNameMaxLength = photoNameMaxLength;
	}

	@Override
	public void clear() {
		super.clear();
		photoId = 0;
		name = null;
		photoAuthor = null;
		genre = null;
		genreId = 0;

		userTeamMembers = null;
		photoTeamMemberIds = null;
		photoTeamMembers = null;

		userPhotoAlbums = newArrayList();
		photoAlbumIds = null;
		photoAlbums = null;
	}
}
