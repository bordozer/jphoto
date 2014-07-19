package rest.photo.upload.userTeam;

import core.services.translator.Language;
import core.services.translator.TranslatorService;

public class UserTeamTranslationDTO {

	private final String headerTitle;
	private final String headerTitleCreateNewMemberButtonTitle;

	private final String listEntryPhotos;

	private final String entryInfoName;
	private final String entryInfoRole;
	private final String entryInfoMember;

	private final String newMemberDefaultName;

	public UserTeamTranslationDTO( final TranslatorService translatorService, final Language language ) {
		headerTitle = translatorService.translate( "Photo data / Photo team/ Header title: Select existing members", language );
		headerTitleCreateNewMemberButtonTitle = translatorService.translate( "Photo data / Photo team/ Header title: Create new team member", language );

		listEntryPhotos = translatorService.translate( "ROD PLURAL photos", language );

		entryInfoName = translatorService.translate( "Photo data / Photo team / Entry info: Name", language );
		entryInfoRole = translatorService.translate( "Photo data / Photo team / Entry info: Role", language );
		entryInfoMember = translatorService.translate( "Photo data / Photo team / Entry info: Member", language );

		newMemberDefaultName = translatorService.translate( "Photo data / Photo team: New team member default name", language );
	}

	public String getHeaderTitle() {
		return headerTitle;
	}

	public String getHeaderTitleCreateNewMemberButtonTitle() {
		return headerTitleCreateNewMemberButtonTitle;
	}

	public String getListEntryPhotos() {
		return listEntryPhotos;
	}

	public String getEntryInfoName() {
		return entryInfoName;
	}

	public String getEntryInfoRole() {
		return entryInfoRole;
	}

	public String getEntryInfoMember() {
		return entryInfoMember;
	}

	public String getNewMemberDefaultName() {
		return newMemberDefaultName;
	}
}
