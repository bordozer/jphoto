package core.general.menus.photo.items;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.userTeam.UserTeamMember;
import utils.TranslatorUtils;

public class PhotoMenuItemGoToAuthorPhotoByTeamMember extends AbstractPhotoMenuItem {

	public static final String BEAN_NAME = "photoMenuItemGoToAuthorPhotoByTeamMember";

	private PhotoTeamMember photoTeamMember;

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_TEAM_MEMBER;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int photoId, final User userWhoIsCallingMenu ) {
		final UserTeamMember userTeamMember = photoTeamMember.getUserTeamMember();
		final User photoAuthor = getPhotoAuthor( photoId );

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "$1: photos with $2 $3 ( $4 )"
					, photoAuthor.getNameEscaped(), userTeamMember.getTeamMemberType().getNameTranslated().toLowerCase(), userTeamMember.getTeamMemberName(), String.valueOf( getTeamMemberPhotosQty() ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotosByTeamMember( %d, %d );", photoAuthor.getId(), userTeamMember.getId() );
			}

			@Override
			public String getCommandIcon() {
				return String.format( "userTeamMemberTypeIcons/%s", userTeamMember.getTeamMemberType().getIcon() );
			}
		};
	}

	@Override
	public boolean isAccessibleForPhoto( final Photo photo, final User userWhoIsCallingMenu ) {
		if ( hideMenuItemBecauseEntryOfMenuCaller( photo, userWhoIsCallingMenu ) ) {
			return false;
		}

		return super.isAccessibleForPhoto( photo, userWhoIsCallingMenu ) && getTeamMemberPhotosQty() > 1;
	}

	private int getTeamMemberPhotosQty() {
		return userTeamService.getTeamMemberPhotosQty( photoTeamMember.getUserTeamMember().getId() );
	}

	public void setPhotoTeamMember( final PhotoTeamMember photoTeamMember ) {
		this.photoTeamMember = photoTeamMember;
	}
}
