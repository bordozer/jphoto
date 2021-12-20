package com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photoTeam.PhotoTeamMember;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;

public class PhotoMenuItemGoToAuthorPhotoByTeamMember extends AbstractPhotoGoToAuthorPhotos {

    private final PhotoTeamMember photoTeamMember;

    public PhotoMenuItemGoToAuthorPhotoByTeamMember(final Photo photo, final User accessor, final Services services, final PhotoTeamMember photoTeamMember) {
        super(photo, accessor, services);

        this.photoTeamMember = photoTeamMember;
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_TEAM_MEMBER;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {

        final UserTeamMember userTeamMember = photoTeamMember.getUserTeamMember();
        final User photoAuthor = getPhotoAuthor();

        return new AbstractEntryMenuItemCommand<Photo>(menuEntry, accessor, services) {

            @Override
            public String getMenuText() {
                final String teamMemberTypeNameTranslated = getTranslatorService().translate(userTeamMember.getTeamMemberType().getName(), getLanguage()).toLowerCase();
                return getTranslatorService().translate("PhotoMenuItem: $1: photos with $2 $3 ( $4 )"
                        , getLanguage()
                        , photoAuthor.getNameEscaped()
                        , teamMemberTypeNameTranslated
                        , userTeamMember.getTeamMemberName()
                        , String.valueOf(getPhotosQty())
                );
            }

            @Override
            public String getMenuCommand() {
                return String.format("goToMemberPhotosByTeamMember( %d, %d );", photoAuthor.getId(), userTeamMember.getId());
            }
        };
    }

    @Override
    protected int getPhotosQty() {
        return services.getUserTeamService().getTeamMemberPhotosQty(photoTeamMember.getUserTeamMember().getId());
    }

    @Override
    public String getCommandIcon() {
        return String.format("userTeamMemberTypeIcons/%s", photoTeamMember.getUserTeamMember().getTeamMemberType().getIcon());
    }
}
