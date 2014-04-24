package core.general.menus.photo.goTo;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.photo.Photo;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.User;
import core.general.user.userTeam.UserTeamMember;
import core.services.system.Services;

public class PhotoMenuItemGoToAuthorPhotoByTeamMember extends AbstractPhotoGoToAuthorPhotos {

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
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {

		final UserTeamMember userTeamMember = photoTeamMember.getUserTeamMember();
		final User photoAuthor = getPhotoAuthor();

		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {

			@Override
			public String getMenuText() {
				final String teamMemberTypeNameTranslated = getTranslatorService().translate( userTeamMember.getTeamMemberType().getName(), getLanguage() ).toLowerCase();
				return getTranslatorService().translate( "PhotoMenuItem: $1: photos with $2 $3 ( $4 )"
					, getLanguage()
					, photoAuthor.getNameEscaped()
					, teamMemberTypeNameTranslated
					, userTeamMember.getTeamMemberName()
					, String.valueOf( getPhotosQty() )
				);
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotosByTeamMember( %d, %d );", photoAuthor.getId(), userTeamMember.getId() );
			}
		};
	}

	@Override
	protected int getPhotosQty() {
		return services.getUserTeamService().getTeamMemberPhotosQty( photoTeamMember.getUserTeamMember().getId() );
	}

	@Override
	public String getCommandIcon() {
		return String.format( "userTeamMemberTypeIcons/%s", photoTeamMember.getUserTeamMember().getTeamMemberType().getIcon() );
	}
}
