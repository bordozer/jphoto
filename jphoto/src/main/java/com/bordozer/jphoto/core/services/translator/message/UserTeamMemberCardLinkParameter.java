package com.bordozer.jphoto.core.services.translator.message;

import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class UserTeamMemberCardLinkParameter extends AbstractTranslatableMessageParameter {

    private UserTeamMember userTeamMember;

    public UserTeamMemberCardLinkParameter(final UserTeamMember userTeamMember, final Services services) {
        super(services);
        this.userTeamMember = userTeamMember;
    }

    @Override
    public String getValue(final Language language) {
        return services.getEntityLinkUtilsService().getUserTeamMemberCardLink(userTeamMember, language);
    }
}
