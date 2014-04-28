package ui.controllers.photos.edit;

import core.enums.PhotoActionAllowance;
import core.general.base.AbstractGeneralModel;
import core.general.photo.Photo;
import org.springframework.web.multipart.MultipartFile;
import ui.translatable.GenericTranslatableList;

import java.io.File;
import java.util.List;

public class PhotoEditDataModel extends AbstractGeneralModel {

	private Photo photo;

	private MultipartFile photoFile;
	private File tempPhotoFile;

	private String photoName;
	private String photoDescription;
	private String photoKeywords;

	private int selectedGenreId;
	private List<GenreWrapper> genreWrappers;

	private boolean containsNudeContent;
	private boolean anonymousPosting;

	private GenericTranslatableList<PhotoActionAllowance> accessibleCommentAllowancesTranslatableList;
	private PhotoActionAllowance commentsAllowance;
	private int sendNotificationEmailAboutNewPhotoComment;

	private GenericTranslatableList<PhotoActionAllowance> accessibleVotingAllowancesTranslatableList;
	private PhotoActionAllowance votingAllowance;

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}

	public Photo getPhoto() {
		return photo;
	}

	public MultipartFile getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile( final MultipartFile photoFile ) {
		this.photoFile = photoFile;
	}

	public void setTempPhotoFile( final File tempPhotoFile ) {
		this.tempPhotoFile = tempPhotoFile;
	}

	public File getTempPhotoFile() {
		return tempPhotoFile;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName( final String photoName ) {
		this.photoName = photoName;
	}

	public String getPhotoDescription() {
		return photoDescription;
	}

	public void setPhotoDescription( final String photoDescription ) {
		this.photoDescription = photoDescription;
	}

	public String getPhotoKeywords() {
		return photoKeywords;
	}

	public void setPhotoKeywords( final String photoKeywords ) {
		this.photoKeywords = photoKeywords;
	}

	public int getSelectedGenreId() {
		return selectedGenreId;
	}

	public void setSelectedGenreId( final int selectedGenreId ) {
		this.selectedGenreId = selectedGenreId;
	}

	public List<GenreWrapper> getGenreWrappers() {
		return genreWrappers;
	}

	public void setGenreWrappers( final List<GenreWrapper> genreWrappers ) {
		this.genreWrappers = genreWrappers;
	}

	public boolean isContainsNudeContent() {
		return containsNudeContent;
	}

	public void setContainsNudeContent( final boolean containsNudeContent ) {
		this.containsNudeContent = containsNudeContent;
	}

	public boolean isAnonymousPosting() {
		return anonymousPosting;
	}

	public void setAnonymousPosting( final boolean anonymousPosting ) {
		this.anonymousPosting = anonymousPosting;
	}

	public GenericTranslatableList<PhotoActionAllowance> getAccessibleCommentAllowancesTranslatableList() {
		return accessibleCommentAllowancesTranslatableList;
	}

	public void setAccessibleCommentAllowancesTranslatableList( final GenericTranslatableList<PhotoActionAllowance> accessibleCommentAllowancesTranslatableList ) {
		this.accessibleCommentAllowancesTranslatableList = accessibleCommentAllowancesTranslatableList;
	}

	public GenericTranslatableList<PhotoActionAllowance> getAccessibleVotingAllowancesTranslatableList() {
		return accessibleVotingAllowancesTranslatableList;
	}

	public void setAccessibleVotingAllowancesTranslatableList( final GenericTranslatableList<PhotoActionAllowance> accessibleVotingAllowancesTranslatableList ) {
		this.accessibleVotingAllowancesTranslatableList = accessibleVotingAllowancesTranslatableList;
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

	public int getSendNotificationEmailAboutNewPhotoComment() {
		return sendNotificationEmailAboutNewPhotoComment;
	}

	public void setSendNotificationEmailAboutNewPhotoComment( final int sendNotificationEmailAboutNewPhotoComment ) {
		this.sendNotificationEmailAboutNewPhotoComment = sendNotificationEmailAboutNewPhotoComment;
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
}
