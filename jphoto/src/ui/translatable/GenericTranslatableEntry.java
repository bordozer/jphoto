package ui.translatable;

import core.interfaces.IdentifiableNameable;
import core.services.translator.Language;
import core.services.translator.TranslatorService;

public class GenericTranslatableEntry<T extends IdentifiableNameable> {

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
