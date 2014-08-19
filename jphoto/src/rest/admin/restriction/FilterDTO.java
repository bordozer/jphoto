package rest.admin.restriction;

import core.enums.RestrictionStatus;
import core.enums.RestrictionType;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FilterDTO {

	private int userId;

	private List<String> selectedRestrictionTypeIds;
	private List<String> restrictionStatusIds;

	public FilterDTO() {

		selectedRestrictionTypeIds = newArrayList();
		for ( final RestrictionType restrictionType : RestrictionType.values() ) {
			selectedRestrictionTypeIds.add( String.valueOf( restrictionType.getId() ) );
		}

		restrictionStatusIds = newArrayList( String.valueOf( RestrictionStatus.PROGRESS.getId() ) );
	}

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
