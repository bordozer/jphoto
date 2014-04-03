package ui.controllers.comment.edit;

import core.general.genre.Genre;
import core.general.menus.EntryMenu;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.user.UserAvatar;
import core.general.user.UserPhotoVote;
import ui.userRankIcons.UserRankIconContainer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoCommentInfo {

	private List<PhotoCommentInfo> childrenComments = newArrayList();
	private final PhotoComment photoComment;
	private User commentAuthor;
	private UserAvatar commentAuthorAvatar;
	private int commentAuthorRankInGenre;

	private List<UserPhotoVote> commentAuthorVotes;

	private final Photo photo;
	private User photoAuthor;
	private Genre genre;
	private boolean userHasEnoughPhotosInGenre;

	private EntryMenu entryMenu;
	private boolean authorNameMustBeHidden;

	private UserRankIconContainer userRankIconContainer;

	public PhotoCommentInfo( final Photo photo, final PhotoComment photoComment ) {
		this.photo = photo;
		this.photoComment = photoComment;
	}

	public PhotoComment getPhotoComment() {
		return photoComment;
	}

	public Photo getPhoto() {
		return photo;
	}

	public List<PhotoCommentInfo> getChildrenComments() {
		return childrenComments;
	}

	public void setChildrenComments( final List<PhotoCommentInfo> childrenComments ) {
		this.childrenComments = childrenComments;
	}

	public User getCommentAuthor() {
		return commentAuthor;
	}

	public void setCommentAuthor( final User commentAuthor ) {
		this.commentAuthor = commentAuthor;
	}

	public UserAvatar getCommentAuthorAvatar() {
		return commentAuthorAvatar;
	}

	public void setCommentAuthorAvatar( final UserAvatar commentAuthorAvatar ) {
		this.commentAuthorAvatar = commentAuthorAvatar;
	}

	public int getCommentAuthorRankInGenre() {
		return commentAuthorRankInGenre;
	}

	public void setCommentAuthorRankInGenre( final int commentAuthorRankInGenre ) {
		this.commentAuthorRankInGenre = commentAuthorRankInGenre;
	}

	public User getPhotoAuthor() {
		return photoAuthor;
	}

	public void setPhotoAuthor( final User photoAuthor ) {
		this.photoAuthor = photoAuthor;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre( final Genre genre ) {
		this.genre = genre;
	}

	public boolean isUserHasEnoughPhotosInGenre() {
		return userHasEnoughPhotosInGenre;
	}

	public void setUserHasEnoughPhotosInGenre( final boolean userHasEnoughPhotosInGenre ) {
		this.userHasEnoughPhotosInGenre = userHasEnoughPhotosInGenre;
	}

	public List<UserPhotoVote> getCommentAuthorVotes() {
		return commentAuthorVotes;
	}

	public void setCommentAuthorVotes( final List<UserPhotoVote> commentAuthorVotes ) {
		this.commentAuthorVotes = commentAuthorVotes;
	}

	public EntryMenu getEntryMenu() {
		return entryMenu;
	}

	public void setEntryMenu( final EntryMenu entryMenu ) {
		this.entryMenu = entryMenu;
	}

	public void setAuthorNameMustBeHidden( final boolean authorNameMustBeHidden ) {
		this.authorNameMustBeHidden = authorNameMustBeHidden;
	}

	public boolean isAuthorNameMustBeHidden() {
		return authorNameMustBeHidden;
	}

	public UserRankIconContainer getUserRankIconContainer() {
		return userRankIconContainer;
	}

	public void setUserRankIconContainer( final UserRankIconContainer userRankIconContainer ) {
		this.userRankIconContainer = userRankIconContainer;
	}
}
