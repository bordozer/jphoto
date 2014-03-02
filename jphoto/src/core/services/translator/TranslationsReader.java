package core.services.translator;

import java.io.File;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class TranslationsReader {

	public static Translator getTranslator( final File translationsFile ) {

		final Map<String, TranslationData> translationsMap = newHashMap();

		return new Translator( translationsMap );
	}
}
