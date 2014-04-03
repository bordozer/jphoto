package ui.controllers.users.list;

import core.general.user.UserMembershipType;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class UserFilterModel {

	public static final String USER_NAME_FORM_CONTROL = "filterUserName";

	private String filterUserName;
	private List<Integer> membershipTypeList = newArrayList( UserMembershipType.AUTHOR.getId(), UserMembershipType.MODEL.getId(), UserMembershipType.MAKEUP_MASTER.getId() );
	private boolean isVisible;

	private BindingResult bindingResult;

	public String getFilterUserName() {
		return filterUserName;
	}

	public void setFilterUserName( final String filterUserName ) {
		this.filterUserName = filterUserName;
	}

	public void clear() {
		filterUserName = StringUtils.EMPTY;
	}

	public List<Integer> getMembershipTypeList() {
		return membershipTypeList;
	}

	public void setMembershipTypeList( final List<Integer> membershipTypeList ) {
		this.membershipTypeList = membershipTypeList;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible( final boolean visible ) {
		isVisible = visible;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}

	public void setBindingResult( final BindingResult bindingResult ) {
		this.bindingResult = bindingResult;
	}
}
