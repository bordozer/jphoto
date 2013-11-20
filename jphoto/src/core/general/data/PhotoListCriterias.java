package core.general.data;

import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.utils.sql.PhotoSqlHelperServiceImpl;

import java.util.Date;

public class PhotoListCriterias {

	private int minimalMarks = PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK;
	private int photoQtyLimit;

	private Date uploadDateFrom;
	private Date uploadDateTo;

	private Date votingTimeFrom;
	private Date votingTimeTo;

	private User user;
	private Genre genre;

	private User votedUser;
	private PhotoVotingCategory votingCategory;

	private UserMembershipType membershipType;
	private boolean topBestPhotoList;

	private PhotoSort photoSort;

	public boolean hasVotingCriterias() {
		return ! ( minimalMarks == PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK
			 && ( votingTimeFrom == null || votingTimeFrom.getTime() == 0 ) // TODO: use DateUtilsService.isEmptyTime()
			 && ( votingTimeTo == null || votingTimeTo.getTime() == 0 )     // TODO: use DateUtilsService.isEmptyTime()
			 && votingCategory == null
			 && votedUser == null
		);
	}

	public int getMinimalMarks() {
		return minimalMarks;
	}

	public void setMinimalMarks( final int minimalMarks ) {
		this.minimalMarks = minimalMarks;
	}

	public int getPhotoQtyLimit() {
		return photoQtyLimit;
	}

	public void setPhotoQtyLimit( final int photoQtyLimit ) {
		this.photoQtyLimit = photoQtyLimit;
	}

	public Date getUploadDateFrom() {
		return uploadDateFrom;
	}

	public void setUploadDateFrom( final Date uploadDateFrom ) {
		this.uploadDateFrom = uploadDateFrom;
	}

	public Date getUploadDateTo() {
		return uploadDateTo;
	}

	public void setUploadDateTo( final Date uploadDateTo ) {
		this.uploadDateTo = uploadDateTo;
	}

	public Date getVotingTimeFrom() {
		return votingTimeFrom;
	}

	public void setVotingTimeFrom( final Date votingTimeFrom ) {
		this.votingTimeFrom = votingTimeFrom;
	}

	public Date getVotingTimeTo() {
		return votingTimeTo;
	}

	public void setVotingTimeTo( final Date votingTimeTo ) {
		this.votingTimeTo = votingTimeTo;
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

	public void setVotingCategory( final PhotoVotingCategory votingCategory ) {
		this.votingCategory = votingCategory;
	}

	public PhotoVotingCategory getVotingCategory() {
		return votingCategory;
	}

	public User getVotedUser() {
		return votedUser;
	}

	public void setVotedUser( final User votedUser ) {
		this.votedUser = votedUser;
	}

	public void setMembershipType( final UserMembershipType membershipType ) {
		this.membershipType = membershipType;
	}

	public UserMembershipType getMembershipType() {
		return membershipType;
	}

	public boolean isTopBestPhotoList() {
		return topBestPhotoList;
	}

	public void setTopBestPhotoList( final boolean topBestPhotoList ) {
		this.topBestPhotoList = topBestPhotoList;
	}

	public PhotoSort getPhotoSort() {
		return photoSort;
	}

	public void setPhotoSort( final PhotoSort photoSort ) {
		this.photoSort = photoSort;
	}
}
