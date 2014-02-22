package core.general.photo;

import core.enums.PhotoActionAllowance;
import core.general.base.AbstractBaseEntity;
import core.general.menus.PopupMenuAssignable;
import core.interfaces.Favoritable;
import core.interfaces.Nameable;
import core.interfaces.Cacheable;
import utils.StringUtilities;

import java.io.File;
import java.util.Date;

public class Photo extends AbstractBaseEntity implements Nameable, Favoritable, Cacheable, PopupMenuAssignable {

	private String name;

	private int userId;
	private int genreId;

	private String keywords;
	private String description;

	private File file;
	private long fileSize;

	private Date uploadTime;

	private boolean containsNudeContent;
	private String bgColor;

	private PhotoActionAllowance commentsAllowance;
	private boolean notificationEmailAboutNewPhotoComment;
	private PhotoActionAllowance votingAllowance;

	private boolean isAnonymousPosting;

	private int userGenreRank;
	private int importId;

	public Photo() {
	}

	public Photo( final Photo photo ) {
		setId( photo.getId() );
		name = photo.getName();
		userId = photo.getUserId();
		genreId = photo.getGenreId();
		keywords = photo.getKeywords();
		description = photo.getDescription();
		file = photo.getFile();
		fileSize = photo.getFileSize();
		uploadTime = photo.getUploadTime();
		containsNudeContent = photo.isContainsNudeContent();
		bgColor = photo.getBgColor();
		commentsAllowance = photo.getCommentsAllowance();
		notificationEmailAboutNewPhotoComment = photo.isNotificationEmailAboutNewPhotoComment();
		votingAllowance = photo.getVotingAllowance();
		isAnonymousPosting = photo.isAnonymousPosting();
		userGenreRank = photo.getUserGenreRank();
	}

	@Override
	public String getName() {
		return name;
	}

	public String getNameEscaped() {
		return StringUtilities.escapeHtml( name );
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public void setGenreId( final int genreId ) {
		this.genreId = genreId;
	}

	public int getGenreId() {
		return genreId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords( final String keywords ) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( final String description ) {
		this.description = description;
	}

	public File getFile() {
		return file;
	}

	public void setFile( final File file ) {
		this.file = file;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize( final long fileSize ) {
		this.fileSize = fileSize;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime( final Date uploadTime ) {
		this.uploadTime = uploadTime;
	}

	public boolean isContainsNudeContent() {
		return containsNudeContent;
	}

	public void setContainsNudeContent( final boolean containsNudeContent ) {
		this.containsNudeContent = containsNudeContent;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor( final String bgColor ) {
		this.bgColor = bgColor;
	}

	public PhotoActionAllowance getCommentsAllowance() {
		return commentsAllowance;
	}

	public void setCommentsAllowance( final PhotoActionAllowance commentsAllowance ) {
		this.commentsAllowance = commentsAllowance;
	}

	public boolean isNotificationEmailAboutNewPhotoComment() {
		return notificationEmailAboutNewPhotoComment;
	}

	public void setNotificationEmailAboutNewPhotoComment( final boolean notificationEmailAboutNewPhotoComment ) {
		this.notificationEmailAboutNewPhotoComment = notificationEmailAboutNewPhotoComment;
	}

	public PhotoActionAllowance getVotingAllowance() {
		return votingAllowance;
	}

	public void setVotingAllowance( final PhotoActionAllowance votingAllowance ) {
		this.votingAllowance = votingAllowance;
	}

	public boolean isAnonymousPosting() {
		return isAnonymousPosting;
	}

	public void setAnonymousPosting( final boolean anonymousPosting ) {
		isAnonymousPosting = anonymousPosting;
	}

	public int getUserGenreRank() {
		return userGenreRank;
	}

	public void setUserGenreRank( final int userGenreRank ) {
		this.userGenreRank = userGenreRank;
	}

	public int getImportId() {
		return importId;
	}

	public void setImportId( final int importId ) {
		this.importId = importId;
	}

	@Override
	public String toString() {
		return String.format( "# %d : %s", getId(), name );
	}

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof Photo ) ) {
			return false;
		}

		final Photo photo = ( Photo ) obj;
		return photo.getId() == getId();
	}
}
