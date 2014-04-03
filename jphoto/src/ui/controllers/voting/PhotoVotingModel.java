package ui.controllers.voting;

import core.general.base.AbstractGeneralModel;
import core.general.photo.Photo;
import core.general.user.UserPhotoVote;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoVotingModel extends AbstractGeneralModel {

	public static final String VOTING_CATEGORY_MARK_CONTROL = "votingCategoryMark";

	private Photo photo;

	private List<UserPhotoVote> userPhotoVotes;

	private int votingUserMinAccessibleMarkForGenre;
	private int votingUserMaxAccessibleMarkForGenre;

	private boolean showPhotoVotingForm;

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}

	public List<UserPhotoVote> getUserPhotoVotes() {
		return userPhotoVotes;
	}

	public void setUserPhotoVotes( final List<UserPhotoVote> userPhotoVotes ) {
		this.userPhotoVotes = userPhotoVotes;
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

	public boolean isShowPhotoVotingForm() {
		return showPhotoVotingForm;
	}

	public void setShowPhotoVotingForm( final boolean showPhotoVotingForm ) {
		this.showPhotoVotingForm = showPhotoVotingForm;
	}

	@Override
	public void clear() {
		super.clear();

		photo = null;
		userPhotoVotes = newArrayList();
	}
}
