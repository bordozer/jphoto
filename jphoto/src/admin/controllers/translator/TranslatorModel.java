package admin.controllers.translator;

import core.general.base.AbstractGeneralModel;
import core.services.translator.TranslationData;

import java.util.Map;

public class TranslatorModel extends AbstractGeneralModel {

	private Map<String, TranslationData> translationsMap;

	public void setTranslationsMap( final Map<String,TranslationData> translationsMap ) {
		this.translationsMap = translationsMap;
	}

	public Map<String, TranslationData> getTranslationsMap() {
		return translationsMap;
	}
}
