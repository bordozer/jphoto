package core.general.user.userTeam;

import core.enums.UserTeamMemberType;
import core.general.base.AbstractBaseEntity;
import core.general.user.User;
import core.interfaces.Cacheable;
import core.interfaces.Favoritable;
import core.interfaces.Nameable;
import org.apache.commons.lang.StringUtils;
import utils.StringUtilities;

public class UserTeamMember extends AbstractBaseEntity implements Nameable, Favoritable, Cacheable {

	private String name;
	private User user;
	private User teamMemberUser;
	private UserTeamMemberType teamMemberType;

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public User getTeamMemberUser() {
		return teamMemberUser;
	}

	public void setTeamMemberUser( final User teamMemberUser ) {
		this.teamMemberUser = teamMemberUser;
	}

	public UserTeamMemberType getTeamMemberType() {
		return teamMemberType;
	}

	public void setTeamMemberType( final UserTeamMemberType teamMemberType ) {
		this.teamMemberType = teamMemberType;
	}

	public String getTeamMemberName() {
		if ( StringUtils.isNotEmpty( name ) ) {
			return name;
		}

		return teamMemberUser.getName();
	}

	public String getTeamMemberNameWithType() {
		String teamMemberName = getTeamMemberName();

		// TODO
		/*if ( teamMemberUser != null ) {
			teamMemberName = EntityLinkUtilsService.getUserCardLink( teamMemberUser, teamMemberName );
		}*/
		return String.format( "%s ( %s )", StringUtilities.escapeHtml( teamMemberName ), teamMemberType.getName() ); // TODO: translate teamMemberType.getName()
	}

	@Override
	public int hashCode() {
		return user.hashCode();
	}

	public int getHashCode() {
		return getId();
	}

	@Override
	public String toString() {
		return String.format( "%s: %s (%s)", user, name, teamMemberType.getName() );
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof UserTeamMember ) ) {
			return false;
		}

		final UserTeamMember user = ( UserTeamMember ) obj;
		return user.getId() == getId();
	}
}
