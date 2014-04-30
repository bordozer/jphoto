package json.photo.appraisal;

import java.util.List;

public class AppraisalSection {

	private int number;

	private List<PhotoAppraisalCategory> accessibleAppraisalCategories;
	private List<Mark> accessibleMarks;

	private int selectedCategoryId;
	private int selectedMark;

	public AppraisalSection() {
	}

	public AppraisalSection( final int number, final List<PhotoAppraisalCategory> accessibleAppraisalCategories, final List<Mark> accessibleMarks ) {
		this.number = number;
		this.accessibleAppraisalCategories = accessibleAppraisalCategories;
		this.accessibleMarks = accessibleMarks;
	}

	public void setNumber( final int number ) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public List<PhotoAppraisalCategory> getAccessibleAppraisalCategories() {
		return accessibleAppraisalCategories;
	}

	public void setAccessibleAppraisalCategories( final List<PhotoAppraisalCategory> accessibleAppraisalCategories ) {
		this.accessibleAppraisalCategories = accessibleAppraisalCategories;
	}

	public List<Mark> getAccessibleMarks() {
		return accessibleMarks;
	}

	public void setAccessibleMarks( final List<Mark> accessibleMarks ) {
		this.accessibleMarks = accessibleMarks;
	}

	public int getSelectedCategoryId() {
		return selectedCategoryId;
	}

	public void setSelectedCategoryId( final int selectedCategoryId ) {
		this.selectedCategoryId = selectedCategoryId;
	}

	public int getSelectedMark() {
		return selectedMark;
	}

	public void setSelectedMark( final int selectedMark ) {
		this.selectedMark = selectedMark;
	}
}
