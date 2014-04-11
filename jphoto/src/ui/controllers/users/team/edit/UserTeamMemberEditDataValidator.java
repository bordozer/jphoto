package ui.controllers.users.team.edit;

import ui.context.EnvironmentContext;
import core.general.user.User;
import core.general.user.userTeam.UserTeamMember;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.user.UserTeamService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;
import utils.NumberUtils;

public class UserTeamMemberEditDataValidator implements Validator {

	@Autowired
	private UserTeamService userTeamService;

	@Autowired
	private UserService userService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return clazz.equals( UserTeamMemberEditDataModel.class );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final UserTeamMemberEditDataModel model = ( UserTeamMemberEditDataModel ) target;

		validateName( model, errors );

		validateTeamMemberUser( model, errors );
	}

	private void validateName( final UserTeamMemberEditDataModel model, final Errors errors ) {
		if ( StringUtils.isEmpty( model.getTeamMemberName() ) ) {
			errors.rejectValue( UserTeamMemberEditDataModel.FORM_CONTROL_TEAM_MEMBER_NAME
				, translatorService.translate( "$1 should not be empty.", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Team member custom name" ) ) );
			return;
		}

		final UserTeamMember userTeamMember = userTeamService.loadUserTeamMemberByName( model.getUser().getId(), model.getTeamMemberName() );
		if ( userTeamMember != null && userTeamMember.getId() != model.getUserTeamMemberId() ) {
			errors.rejectValue( UserTeamMemberEditDataModel.FORM_CONTROL_TEAM_MEMBER_NAME
				, translatorService.translate( "$1 should be unique. You already have this name in your team", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Team member custom name" ) ) );
		}
	}

	private void validateTeamMemberUser( final UserTeamMemberEditDataModel model, final Errors errors ) {
		final int teamMemberUserId = NumberUtils.convertToInt( model.getTeamMemberUserId() );

		if ( teamMemberUserId == 0 ) {
			return;
		}

		final User teamMemberUser = userService.load( teamMemberUserId );
		if ( teamMemberUser == null ) {
			errors.rejectValue( UserTeamMemberEditDataModel.FORM_CONTROL_TEAM_MEMBER_USER_ID
				, translatorService.translate( "$1 does not exist", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "User" ) ) );
		}
	}
}
