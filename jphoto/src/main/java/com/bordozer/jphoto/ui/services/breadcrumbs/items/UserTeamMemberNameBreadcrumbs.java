package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class UserTeamMemberNameBreadcrumbs extends AbstractBreadcrumb {

    private UserTeamMember userTeamMember;

    public UserTeamMemberNameBreadcrumbs(final UserTeamMember userTeamMember, final Services services) {
        super(services);
        this.userTeamMember = userTeamMember;
    }

    @Override
    public String getValue(final Language language) {
        return userTeamMember.getTeamMemberName();
    }
}
