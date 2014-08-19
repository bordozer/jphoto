package rest.admin.restriction;

import core.enums.RestrictionStatus;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FilterDTO {

	private List<String> selectedRestrictionTypeIds = newArrayList( "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11" );
	private List<String> restrictionStatusIds = newArrayList( String.valueOf( RestrictionStatus.PROGRESS.getId() ) );
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
