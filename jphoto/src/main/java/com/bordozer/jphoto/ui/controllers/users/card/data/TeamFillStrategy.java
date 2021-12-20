package com.bordozer.jphoto.ui.controllers.users.card.data;

import com.bordozer.jphoto.core.general.user.userTeam.UserTeam;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.ui.controllers.users.card.UserCardModel;
import com.bordozer.jphoto.ui.elements.PhotoList;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class TeamFillStrategy extends AbstractUserCardModelFillStrategy {

    public TeamFillStrategy(final UserCardModel model, final UserCardModelFillService userCardModelFillService) {
        super(model, userCardModelFillService);
    }

    @Override
    public void performCustomActions() {
        userCardModelFillService.setUserTeam(model);

        final UserTeam userTeam = model.getUserTeam();
        final List<UserTeamMember> userTeamMembers = userTeam.getUserTeamMembers();

        final List<PhotoList> userTeamMemberPhotoListsMap = newArrayList();
        for (final UserTeamMember userTeamMember : userTeamMembers) {
            final PhotoList photoList = userCardModelFillService.getUserTeamMemberLastPhotos(getUser(), userTeamMember, getCurrentUser()).getPhotoList(userTeamMember.getId(), 1, getLanguage(), userCardModelFillService.getDateUtilsService().getCurrentTime());

            if (photoList.hasPhotos()) {
                userTeamMemberPhotoListsMap.add(photoList);
            }
        }

        model.setUserTeamMemberPhotoLists(userTeamMemberPhotoListsMap);
    }
}
