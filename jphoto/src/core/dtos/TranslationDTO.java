package core.dtos;

import java.util.Map;

public class TranslationDTO extends AjaxResultDTO {

	private final Map<String, String> translations;

	public TranslationDTO( final Map<String, String> translations ) {
		this.translations = translations;
	}

	public Map<String, String> getTranslations() {
		return translations;
	}
}
