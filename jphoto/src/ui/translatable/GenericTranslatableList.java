package ui.translatable;

import core.enums.UserGender;
import core.general.user.UserMembershipType;
import core.interfaces.IdentifiableNameable;
import core.services.translator.Language;
import core.services.translator.TranslatorService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GenericTranslatableList<T extends IdentifiableNameable> {

	private final List<GenericTranslatableEntry> entries;

	public GenericTranslatableList( final T[] list, final Language language, final TranslatorService translatorService ) {
		entries = newArrayList();

		for ( final T entry : list ) {
			entries.add( new GenericTranslatableEntry( entry, language, translatorService ) );
		}
	}

	public List<GenericTranslatableEntry> getEntries() {
		return entries;
	}

	public class GenericTranslatableEntry {

		private TranslatorService translatorService;

		private final T entry;
		private final Language language;

		public GenericTranslatableEntry( final T entry, final Language language, final TranslatorService translatorService ) {
			this.translatorService = translatorService;
			this.entry = entry;
			this.language = language;
		}

		public int getId() {
			return entry.getId();
		}

		public String getName() {
			return translatorService.translate( entry.getName(), language );
		}
	}

	public static GenericTranslatableList<UserMembershipType> userMembershipTypeTranslatableList( final Language language, final TranslatorService translatorService ) {
		return new GenericTranslatableList<UserMembershipType>( UserMembershipType.values(), language, translatorService );
	}

	public static GenericTranslatableList<UserGender> userGenderTranslatableList( final Language language, final TranslatorService translatorService ) {
		return new GenericTranslatableList<UserGender>( UserGender.values(), language, translatorService );
	}
}
