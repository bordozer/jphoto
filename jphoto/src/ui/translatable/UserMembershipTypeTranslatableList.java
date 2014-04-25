package ui.translatable;

import core.general.user.UserMembershipType;
import core.services.translator.Language;
import core.services.translator.TranslatorService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class UserMembershipTypeTranslatableList {

	private final List<UserMembershipTypeTranslatableEntry> entries;

	public UserMembershipTypeTranslatableList( final Language language, final TranslatorService translatorService ) {
		entries = newArrayList();

		for ( final UserMembershipType userMembershipType : UserMembershipType.values() ) {
			entries.add( new UserMembershipTypeTranslatableEntry( userMembershipType, language, translatorService ) );
		}
	}

	public List<UserMembershipTypeTranslatableEntry> getEntries() {
		return entries;
	}

	public class UserMembershipTypeTranslatableEntry {

		private TranslatorService translatorService;

		private final UserMembershipType userMembershipType;
		private final Language language;

		public UserMembershipTypeTranslatableEntry( final UserMembershipType userMembershipType, final Language language, final TranslatorService translatorService ) {
			this.translatorService = translatorService;
			this.userMembershipType = userMembershipType;
			this.language = language;
		}

		public int getId() {
			return userMembershipType.getId();
		}

		public String getName() {
			return translatorService.translate( userMembershipType.getName(), language );
		}
	}
}
