package ui.controllers.users.card.data;

import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import ui.controllers.users.card.UserCardModel;
import ui.elements.PhotoList;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class TeamFillStrategy extends AbstractUserCardModelFillStrategy {

	public TeamFillStrategy( final UserCardModel model, final UserCardModelFillService userCardModelFillService ) {
		super( model, userCardModelFillService );
	}

	@Override
	public void performCustomActions() {
		userCardModelFillService.setUserTeam( model );

		final UserTeam userTeam = model.getUserTeam();
		final List<UserTeamMember> userTeamMembers = userTeam.getUserTeamMembers();

		final List<PhotoList> userTeamMemberPhotoListsMap = newArrayList();
		for ( final UserTeamMember userTeamMember : userTeamMembers ) {
			final PhotoList photoList = userCardModelFillService.getUserTeamMemberLastPhotos( getUser(), userTeamMember, getCurrentUser() ).getPhotoList( userTeamMember.getId(), 1, getLanguage(), userCardModelFillService.getDateUtilsService().getCurrentTime() );

			if ( photoList.hasPhotos() ) {
				userTeamMemberPhotoListsMap.add( photoList );
			}
		}

		model.setUserTeamMemberPhotoLists( userTeamMemberPhotoListsMap );
	}
}
