package json.photo.appraisal;

import java.util.List;

public class AppraisalSection {

	private final int number;

	private final List<PhotoAppraisalCategory> accessibleAppraisalCategories;
	private final List<Mark> accessibleMarks;

	public AppraisalSection( final int number, final List<PhotoAppraisalCategory> accessibleAppraisalCategories, final List<Mark> accessibleMarks ) {
		this.number = number;
		this.accessibleAppraisalCategories = accessibleAppraisalCategories;
		this.accessibleMarks = accessibleMarks;
	}

	public int getNumber() {
		return number;
	}

	public List<PhotoAppraisalCategory> getAccessibleAppraisalCategories() {
		return accessibleAppraisalCategories;
	}

	public List<Mark> getAccessibleMarks() {
		return accessibleMarks;
	}
}
