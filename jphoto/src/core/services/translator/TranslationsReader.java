package core.services.translator;

import core.services.utils.SystemVarsService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

public class TranslationsReader {

	public static final String TRANSLATION = "translation";

	public static Translator getTranslator( final File translationsFile, final SystemVarsService systemVarsService ) throws DocumentException {

		final SAXReader reader = new SAXReader( false );
		final Document document = reader.read( translationsFile );

		final Iterator photosIterator = document.getRootElement().elementIterator( TRANSLATION );

		final Map<NerdKey, TranslationData> translationsMap = newHashMap();

		while ( photosIterator.hasNext() ) {

			final Element nerdElement = ( Element ) photosIterator.next();
			final String nerd = nerdElement.element( Language.NERD.getCode() ).getText();

			final Set<TranslationEntry> translations = newHashSet();
			translations.add( new TranslationEntryNerd( nerd, systemVarsService ) );

			for ( final Language language : Language.values() ) {

				if ( language == Language.NERD ) {
					continue;
				}

				final Element element = nerdElement.element( language.getCode() );
				if ( element == null ) {
					translations.add( new TranslationEntryNerd( nerd, systemVarsService ) );
					continue;
				}

				final String translation = element.getText();
				translations.add( new TranslationEntry( nerd, language, translation, systemVarsService ) );
			}

			translationsMap.put( new NerdKey( nerd ), new TranslationData( nerd, translations ) );
		}

		return new Translator( translationsMap, systemVarsService );
	}
}
