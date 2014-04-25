package ui.translatable;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.jobs.enums.DateRangeType;
import core.enums.FavoriteEntryType;
import core.enums.UserGender;
import core.enums.YesNo;
import core.general.user.EmailNotificationType;
import core.general.user.UserMembershipType;
import core.general.user.UserStatus;
import core.interfaces.IdentifiableNameable;
import core.services.translator.Language;
import core.services.translator.TranslatorService;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GenericTranslatableList<T extends IdentifiableNameable> {

	private final List<GenericTranslatableEntry> entries;

	public GenericTranslatableList( final T[] array, final Language language, final TranslatorService translatorService ) {
		this( Arrays.asList( array ), language, translatorService );
	}

	public GenericTranslatableList( final List<T> list, final Language language, final TranslatorService translatorService ) {
		entries = newArrayList();

		for ( final T entry : list ) {
			entries.add( new GenericTranslatableEntry<T>( entry, language, translatorService ) );
		}
	}

	public GenericTranslatableList( final List<GenericTranslatableEntry> list, final TranslatorService translatorService ) {
		entries = list;
	}

	public List<GenericTranslatableEntry> getEntries() {
		return entries;
	}

	public static GenericTranslatableList<UserMembershipType> userMembershipTypeTranslatableList( final Language language, final TranslatorService translatorService ) {
		return new GenericTranslatableList<UserMembershipType>( UserMembershipType.values(), language, translatorService );
	}

	public static GenericTranslatableList<UserGender> userGenderTranslatableList( final Language language, final TranslatorService translatorService ) {
		return new GenericTranslatableList<UserGender>( UserGender.values(), language, translatorService );
	}

	public static GenericTranslatableList<PhotosImportSource> photosImportSourceTranslatableList( final Language language, final TranslatorService translatorService ) {
		return new GenericTranslatableList<PhotosImportSource>( PhotosImportSource.values(), language, translatorService );
	}

	public static GenericTranslatableList<DateRangeType> dateRangeTypeTranslatableList( final Language language, final TranslatorService translatorService ) {
		return new GenericTranslatableList<DateRangeType>( DateRangeType.values(), language, translatorService );
	}

	public static GenericTranslatableList<YesNo> yesNoTranslatableList( final Language language, final TranslatorService translatorService ) {
		return new GenericTranslatableList<YesNo>( YesNo.values(), language, translatorService );
	}

	public static GenericTranslatableList<FavoriteEntryType> favoriteEntryTypeTranslatableList( final Language language, final TranslatorService translatorService ) {
		return new GenericTranslatableList<FavoriteEntryType>( FavoriteEntryType.values(), language, translatorService );
	}

	public static GenericTranslatableList<UserStatus> userStatusTranslatableList( final Language language, final TranslatorService translatorService ) {
		return new GenericTranslatableList<UserStatus>( UserStatus.values(), language, translatorService );
	}

	public static GenericTranslatableList<EmailNotificationType> emailNotificationTypeTranslatableList( final Language language, final TranslatorService translatorService ) {
		return new GenericTranslatableList<EmailNotificationType>( EmailNotificationType.values(), language, translatorService );
	}

	public static GenericTranslatableList<Language> languageTranslatableList( final TranslatorService translatorService ) {
		return languageTranslatableList( Arrays.asList( Language.values() ), translatorService );
	}

	public static GenericTranslatableList<Language> languageTranslatableList( final List<Language> values, final TranslatorService translatorService ) {
		final List<GenericTranslatableEntry> entries = newArrayList();

		for ( final Language entry : values ) {
			entries.add( new GenericTranslatableEntry<Language>( entry, entry, translatorService ) );
		}

		return new GenericTranslatableList<Language>( entries, translatorService );
	}
}
