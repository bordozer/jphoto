package rest.admin.restriction;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FilterDTO {

	private List<String> selectedRestrictionTypeIds = newArrayList();
	private List<String> restrictionStatusIds = newArrayList();
	private int userId;

	public List<String> getSelectedRestrictionTypeIds() {
		return selectedRestrictionTypeIds;
	}

	public void setSelectedRestrictionTypeIds( final List<String> selectedRestrictionTypeIds ) {
		this.selectedRestrictionTypeIds = selectedRestrictionTypeIds;
	}

	public List<String> getRestrictionStatusIds() {
		return restrictionStatusIds;
	}

	public void setRestrictionStatusIds( final List<String> restrictionStatusIds ) {
		this.restrictionStatusIds = restrictionStatusIds;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}
}
