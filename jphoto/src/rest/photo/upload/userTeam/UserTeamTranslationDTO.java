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

	private final String entryInfoIconTitleCard;
	private final String entryInfoIconTitleEdit;
	private final String entryInfoIconTitleDelete;

	private final String entryEditIconCancelCard;
	private final String entryEditIconSaveCard;
	private final String entryEditIconDiscardCard;

	private final String newEntryDefaultName;

	private final String deleteEntryConfirmation;
	private final String deleteEntryImpossible;
	private final String discardChanges;

	public UserTeamTranslationDTO( final TranslatorService translatorService, final Language language ) {
		headerTitle = translatorService.translate( "Photo data / Photo team/ Header title: Select existing members", language );
		headerTitleCreateNewMemberButtonTitle = translatorService.translate( "Photo data / Photo team/ Header title: Create new team member", language );

		listEntryPhotos = translatorService.translate( "ROD PLURAL photos", language );

		entryInfoName = translatorService.translate( "Photo data / Photo team / Entry info: Name", language );
		entryInfoRole = translatorService.translate( "Photo data / Photo team / Entry info: Role", language );
		entryInfoMember = translatorService.translate( "Photo data / Photo team / Entry info: Member", language );

		entryInfoIconTitleCard = translatorService.translate( "Photo data / Photo team / Entry info / Icon: Card", language );
		entryInfoIconTitleEdit = translatorService.translate( "Photo data / Photo team / Entry info / Icon: Edit", language );
		entryInfoIconTitleDelete = translatorService.translate( "Photo data / Photo team / Entry info / Icon: Delete", language );

		entryEditIconCancelCard = translatorService.translate( "Photo data / Photo team / Entry edit / Icon: Cancel", language );
		entryEditIconSaveCard = translatorService.translate( "Photo data / Photo team / Entry edit / Icon: Save", language );
		entryEditIconDiscardCard = translatorService.translate( "Photo data / Photo team / Entry edit / Icon: Discard", language );

		newEntryDefaultName = translatorService.translate( "Photo data / Photo team: New team member default name", language );

		deleteEntryConfirmation = translatorService.translate( "Photo data / Photo team: delete this member?", language );
		deleteEntryImpossible = translatorService.translate( "User card: You have already assigned this member to one or more photos", language );
		discardChanges = translatorService.translate( "Photo data / Photo team: New team member default name", language );
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

	public String getEntryInfoIconTitleCard() {
		return entryInfoIconTitleCard;
	}

	public String getEntryInfoIconTitleEdit() {
		return entryInfoIconTitleEdit;
	}

	public String getEntryInfoIconTitleDelete() {
		return entryInfoIconTitleDelete;
	}

	public String getEntryEditIconCancelCard() {
		return entryEditIconCancelCard;
	}

	public String getEntryEditIconSaveCard() {
		return entryEditIconSaveCard;
	}

	public String getEntryEditIconDiscardCard() {
		return entryEditIconDiscardCard;
	}

	public String getNewEntryDefaultName() {
		return newEntryDefaultName;
	}

	public String getDeleteEntryConfirmation() {
		return deleteEntryConfirmation;
	}

	public String getDeleteEntryImpossible() {
		return deleteEntryImpossible;
	}

	public String getDiscardChanges() {
		return discardChanges;
	}
}
