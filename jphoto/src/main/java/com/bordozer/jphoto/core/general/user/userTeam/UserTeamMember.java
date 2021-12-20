package com.bordozer.jphoto.core.general.user.userTeam;

import com.bordozer.jphoto.core.enums.UserTeamMemberType;
import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.interfaces.Cacheable;
import com.bordozer.jphoto.core.interfaces.Favoritable;
import com.bordozer.jphoto.core.interfaces.Nameable;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.utils.StringUtilities;
import org.apache.commons.lang3.StringUtils;

public class UserTeamMember extends AbstractBaseEntity implements Nameable, Favoritable, Cacheable {

    private String name;
    private User user;
    private User teamMemberUser;
    private UserTeamMemberType teamMemberType;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public User getTeamMemberUser() {
        return teamMemberUser;
    }

    public void setTeamMemberUser(final User teamMemberUser) {
        this.teamMemberUser = teamMemberUser;
    }

    public UserTeamMemberType getTeamMemberType() {
        return teamMemberType;
    }

    public void setTeamMemberType(final UserTeamMemberType teamMemberType) {
        this.teamMemberType = teamMemberType;
    }

    public String getTeamMemberName() {
        if (StringUtils.isNotEmpty(name)) {
            return name;
        }

        if (teamMemberUser != null) {
            return teamMemberUser.getName();
        }

        return "ERROR";
    }

    public String getTeamMemberNameWithType(final TranslatorService translatorService, final Language language) {
        String teamMemberName = getTeamMemberName();
        return String.format("%s ( %s )", StringUtilities.escapeHtml(teamMemberName), translatorService.translate(teamMemberType.getName(), language)); // TODO: translate teamMemberType.getName()
    }

    @Override
    public int hashCode() {
        return 31 * user.getId();
    }

    public int getHashCode() {
        return getId();
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s)", user, name, teamMemberType.getName());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof UserTeamMember)) {
            return false;
        }

        final UserTeamMember user = (UserTeamMember) obj;
        return user.getId() == getId();
    }
}
