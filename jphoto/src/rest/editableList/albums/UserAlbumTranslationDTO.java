package rest.editableList.albums;

import core.services.translator.Language;
import core.services.translator.TranslatorService;

public class UserAlbumTranslationDTO {

	private final String headerTitleCreateNewAlbumButtonTitle;
	private final String listEntryPhotos;
	private final String entryEditIconCancelTitle;
	private final String entryEditIconSaveTitle;
	private final String entryEditIconDiscardTitle;

	private final String fieldName;

	private final String albumInfoIconTitleAlbumCard;
	private final String albumInfoIconTitleAlbumEdit;
	private final String albumInfoIconTitleAlbumDelete;
	private final String albumInfoIconTitleCanNotDelete;

	public UserAlbumTranslationDTO( final TranslatorService translatorService, final Language language ) {
		headerTitleCreateNewAlbumButtonTitle = translatorService.translate( "User albums component / Header title: Create new album", language );
		listEntryPhotos = translatorService.translate( "ROD PLURAL photos", language );

		entryEditIconCancelTitle = translatorService.translate( "User albums component / Cancel editing button title", language );
		entryEditIconSaveTitle = translatorService.translate( "User albums component / Save button title", language );
		entryEditIconDiscardTitle = translatorService.translate( "User albums component / Discard editing button title", language );

		fieldName = translatorService.translate( "User albums component / Album info / Name", language );
		albumInfoIconTitleAlbumCard = translatorService.translate( "User albums component / Album info / Album card icon title", language );
		albumInfoIconTitleAlbumEdit = translatorService.translate( "User albums component / Album info / Album edit icon title", language );
		albumInfoIconTitleAlbumDelete = translatorService.translate( "User albums component / Album info / Album delete icon title", language );
		albumInfoIconTitleCanNotDelete = translatorService.translate( "User albums component / Album info / Album can not delete icon title", language );
	}

	public String getHeaderTitleCreateNewAlbumButtonTitle() {
		return headerTitleCreateNewAlbumButtonTitle;
	}

	public String getListEntryPhotos() {
		return listEntryPhotos;
	}

	public String getEntryEditIconCancelTitle() {
		return entryEditIconCancelTitle;
	}

	public String getEntryEditIconSaveTitle() {
		return entryEditIconSaveTitle;
	}

	public String getEntryEditIconDiscardTitle() {
		return entryEditIconDiscardTitle;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getAlbumInfoIconTitleAlbumCard() {
		return albumInfoIconTitleAlbumCard;
	}

	public String getAlbumInfoIconTitleAlbumEdit() {
		return albumInfoIconTitleAlbumEdit;
	}

	public String getAlbumInfoIconTitleAlbumDelete() {
		return albumInfoIconTitleAlbumDelete;
	}

	public String getAlbumInfoIconTitleCanNotDelete() {
		return albumInfoIconTitleCanNotDelete;
	}
}
