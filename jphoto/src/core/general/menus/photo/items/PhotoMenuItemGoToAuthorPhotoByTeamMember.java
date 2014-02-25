package core.general.menus.photo.items;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.photo.Photo;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.User;
import core.general.user.userTeam.UserTeamMember;
import core.services.security.Services;
import utils.TranslatorUtils;

public class PhotoMenuItemGoToAuthorPhotoByTeamMember extends AbstractGoToAuthorPhotos {

	private final PhotoTeamMember photoTeamMember;

	public PhotoMenuItemGoToAuthorPhotoByTeamMember( final Photo photo, final User accessor, final Services services, final PhotoTeamMember photoTeamMember ) {
		super( photo, accessor, services );

		this.photoTeamMember = photoTeamMember;
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_TEAM_MEMBER;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		final UserTeamMember userTeamMember = photoTeamMember.getUserTeamMember();
		final User photoAuthor = getPhotoAuthor();

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "$1: photos with $2 $3 ( $4 )"
					, photoAuthor.getNameEscaped(), userTeamMember.getTeamMemberType().getNameTranslated().toLowerCase(), userTeamMember.getTeamMemberName(), String.valueOf( getPhotosQty() ) );
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
	protected int getPhotosQty() {
		return services.getUserTeamService().getTeamMemberPhotosQty( photoTeamMember.getUserTeamMember().getId() );
	}
}
