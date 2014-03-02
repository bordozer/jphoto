package core.services.translator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class TranslationsReader {

	public static final String TRANSLATION = "translation";

	public static Translator getTranslator( final File translationsFile ) throws DocumentException {

		final SAXReader reader = new SAXReader( false );
		final Document document = reader.read( translationsFile );

		final Iterator photosIterator = document.getRootElement().elementIterator( TRANSLATION );

		final Map<String, TranslationData> translationsMap = newHashMap();

		while ( photosIterator.hasNext() ) {

			final Element nerdElement = ( Element ) photosIterator.next();
			final String nerd = nerdElement.element( Language.NERD.getCode() ).getText();

			final List<TranslationEntry> translations = newArrayList();
			translations.add( new TranslationEntryNerd( nerd ) );

			for ( final Language language : Language.values() ) {

				if ( language == Language.NERD ) {
					continue;
				}

				final String translation = nerdElement.element( language.getCode() ).getText();
				translations.add( new TranslationEntry( nerd, language, translation ) );
			}

			translationsMap.put( nerd, new TranslationData( nerd, translations ) );
		}

		return new Translator( translationsMap );
	}
}
