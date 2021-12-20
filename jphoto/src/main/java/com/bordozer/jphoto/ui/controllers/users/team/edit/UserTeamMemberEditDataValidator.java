package com.bordozer.jphoto.ui.controllers.users.team.edit;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.user.UserTeamService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import com.bordozer.jphoto.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserTeamMemberEditDataValidator implements Validator {

    @Autowired
    private UserTeamService userTeamService;

    @Autowired
    private UserService userService;

    @Autowired
    private TranslatorService translatorService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return clazz.equals(UserTeamMemberEditDataModel.class);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final UserTeamMemberEditDataModel model = (UserTeamMemberEditDataModel) target;

        validateName(model, errors);

        validateTeamMemberUser(model, errors);
    }

    private void validateName(final UserTeamMemberEditDataModel model, final Errors errors) {
        if (StringUtils.isEmpty(model.getTeamMemberName())) {
            errors.rejectValue(UserTeamMemberEditDataModel.FORM_CONTROL_TEAM_MEMBER_NAME
                    , translatorService.translate("$1 should not be empty.", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Team member custom name")));
            return;
        }

        final UserTeamMember userTeamMember = userTeamService.loadUserTeamMemberByName(model.getUser().getId(), model.getTeamMemberName());
        if (userTeamMember != null && userTeamMember.getId() != model.getUserTeamMemberId()) {
            errors.rejectValue(UserTeamMemberEditDataModel.FORM_CONTROL_TEAM_MEMBER_NAME
                    , translatorService.translate("$1 should be unique. You already have this name in your team", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Team member custom name")));
        }
    }

    private void validateTeamMemberUser(final UserTeamMemberEditDataModel model, final Errors errors) {
        final int teamMemberUserId = NumberUtils.convertToInt(model.getTeamMemberUserId());

        if (teamMemberUserId == 0) {
            return;
        }

        final User teamMemberUser = userService.load(teamMemberUserId);
        if (teamMemberUser == null) {
            errors.rejectValue(UserTeamMemberEditDataModel.FORM_CONTROL_TEAM_MEMBER_USER_ID
                    , translatorService.translate("$1 does not exist", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("User")));
        }
    }
}
