package core.general.photo;

public class PhotoVotingCategoryMarks {

	private final PhotoVotingCategory photoVotingCategory;
	private final int marks;

	public PhotoVotingCategoryMarks( final PhotoVotingCategory votingCategory, final int marks ) {
		this.photoVotingCategory = votingCategory;
		this.marks = marks;
	}

	public PhotoVotingCategory getPhotoVotingCategory() {
		return photoVotingCategory;
	}

	public int getMarks() {
		return marks;
	}
}
