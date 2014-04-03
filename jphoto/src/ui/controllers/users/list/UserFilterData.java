package ui.controllers.users.list;

import sql.builder.SqlIdsSelectQuery;

import java.util.List;

public class UserFilterData {

	private String filterUserName;
	private List<Integer> membershipTypeIds;
	private SqlIdsSelectQuery selectQuery;

	public String getFilterUserName() {
		return filterUserName;
	}

	public void setFilterUserName( final String filterUserName ) {
		this.filterUserName = filterUserName;
	}

	public List<Integer> getMembershipTypeIds() {
		return membershipTypeIds;
	}

	public void setMembershipTypeIds( final List<Integer> membershipTypeIds ) {
		this.membershipTypeIds = membershipTypeIds;
	}

	public SqlIdsSelectQuery getSelectQuery() {
		return selectQuery;
	}

	public void setSelectQuery( final SqlIdsSelectQuery selectQuery ) {
		this.selectQuery = selectQuery;
	}
}
