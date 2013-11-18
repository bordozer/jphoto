package controllers.photos.card;

import controllers.comment.edit.PhotoCommentInfo;
import core.general.base.AbstractGeneralModel;
import core.general.genre.Genre;
import core.general.menus.EntryMenu;
import core.general.photo.Photo;
import core.general.photo.ValidationResult;
import controllers.users.genreRank.VotingModel;
import core.general.photo.PhotoInfo;
import core.general.user.User;
import core.general.user.UserPhotoVote;

import java.util.Date;
import java.util.List;

public class PhotoCardModel extends AbstractGeneralModel {

	private Photo photo;

	private User user;
	private Genre genre;

	private List<PhotoCommentInfo> photoCommentInfos;
	private long commentDelay;
	private Date userNextCommentTime;
	private int usedDelayBetweenComments;

	private List<UserPhotoVote> userPhotoVotes;
	private PhotoInfo photoInfo;

	private int minCommentLength;
	private int maxCommentLength;

	private int votingUserMinAccessibleMarkForGenre;
	private int votingUserMaxAccessibleMarkForGenre;

	private ValidationResult commentingValidationResult;
	private ValidationResult votingValidationResult;

	private VotingModel votingModel;

	private EntryMenu entryMenu;

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto( Photo photo ) {
		this.photo = photo;
	}

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre( final Genre genre ) {
		this.genre = genre;
	}

	public List<PhotoCommentInfo> getPhotoCommentInfos() {
		return photoCommentInfos;
	}

	public void setPhotoCommentInfos( final List<PhotoCommentInfo> photoCommentInfos ) {
		this.photoCommentInfos = photoCommentInfos;
	}

	public long getCommentDelay() {
		return commentDelay;
	}

	public void setCommentDelay( final long commentDelay ) {
		this.commentDelay = commentDelay;
	}

	public int getUsedDelayBetweenComments() {
		return usedDelayBetweenComments;
	}

	public void setUsedDelayBetweenComments( final int usedDelayBetweenComments ) {
		this.usedDelayBetweenComments = usedDelayBetweenComments;
	}

	public Date getUserNextCommentTime() {
		return userNextCommentTime;
	}

	public void setUserNextCommentTime( final Date userNextCommentTime ) {
		this.userNextCommentTime = userNextCommentTime;
	}

	public List<UserPhotoVote> getUserPhotoVotes() {
		return userPhotoVotes;
	}

	public void setUserPhotoVotes( final List<UserPhotoVote> userPhotoVotes ) {
		this.userPhotoVotes = userPhotoVotes;
	}

	public PhotoInfo getPhotoInfo() {
		return photoInfo;
	}

	public void setPhotoInfo( final PhotoInfo photoInfo ) {
		this.photoInfo = photoInfo;
	}

	public int getMinCommentLength() {
		return minCommentLength;
	}

	public void setMinCommentLength( final int minCommentLength ) {
		this.minCommentLength = minCommentLength;
	}

	public int getMaxCommentLength() {
		return maxCommentLength;
	}

	public void setMaxCommentLength( final int maxCommentLength ) {
		this.maxCommentLength = maxCommentLength;
	}

	public int getVotingUserMinAccessibleMarkForGenre() {
		return votingUserMinAccessibleMarkForGenre;
	}

	public void setVotingUserMinAccessibleMarkForGenre( final int votingUserMinAccessibleMarkForGenre ) {
		this.votingUserMinAccessibleMarkForGenre = votingUserMinAccessibleMarkForGenre;
	}

	public int getVotingUserMaxAccessibleMarkForGenre() {
		return votingUserMaxAccessibleMarkForGenre;
	}

	public void setVotingUserMaxAccessibleMarkForGenre( final int votingUserMaxAccessibleMarkForGenre ) {
		this.votingUserMaxAccessibleMarkForGenre = votingUserMaxAccessibleMarkForGenre;
	}

	public ValidationResult getCommentingValidationResult() {
		return commentingValidationResult;
	}

	public void setCommentingValidationResult( final ValidationResult commentingValidationResult ) {
		this.commentingValidationResult = commentingValidationResult;
	}

	public ValidationResult getVotingValidationResult() {
		return votingValidationResult;
	}

	public void setVotingValidationResult( final ValidationResult votingValidationResult ) {
		this.votingValidationResult = votingValidationResult;
	}

	public VotingModel getVotingModel() {
		return votingModel;
	}

	public void setVotingModel( final VotingModel votingModel ) {
		this.votingModel = votingModel;
	}

	public EntryMenu getEntryMenu() {
		return entryMenu;
	}

	public void setEntryMenu( final EntryMenu entryMenu ) {
		this.entryMenu = entryMenu;
	}

	@Override
	public void clear() {
		super.clear();
		photo = null;
	}
}
