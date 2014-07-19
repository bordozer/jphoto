package rest.photo.upload.userTeam;

import core.services.translator.Language;
import core.services.translator.TranslatorService;

public class UserTeamTranslationDTO {

	private final String headerTitle;
	private final String headerTitleCreateNewMemberButtonTitle;

	private final String listEntryPhotos;

	private final String newMemberDefaultName;

	public UserTeamTranslationDTO( final TranslatorService translatorService, final Language language ) {
		headerTitle = translatorService.translate( "Photo data / Photo team/ Header title: Select existing members", language );
		headerTitleCreateNewMemberButtonTitle = translatorService.translate( "Photo data / Photo team/ Header title: Create new team member", language );

		listEntryPhotos = translatorService.translate( "ROD PLURAL photos", language );

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

	public String getNewMemberDefaultName() {
		return newMemberDefaultName;
	}
}
