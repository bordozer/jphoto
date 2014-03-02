package core.services.translator;

import core.services.utils.SystemVarsService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class TranslatorServiceImpl implements TranslatorService {

	public static final String TRANSLATIONS_XML = "translations.xml";

	private Translator translator;

	@Autowired
	private SystemVarsService systemVarsService;

	@Override
	public String translate( final String nerd ) {
		return translateWithParameters( nerd );
	}

	@Override
	public String translate( final String nerd, final String param ) {
		return translateWithParameters( nerd, param );
	}

	@Override
	public String translate( final String nerd, final String param1, final String param2 ) {
		return translateWithParameters( nerd, param1, param2 );
	}

	@Override
	public String translate( final String nerd, final String param1, final String param2, final String param3 ) {
		return translateWithParameters( nerd, param1, param2, param3 );
	}

	@Override
	public String translate( final String nerd, final String param1, final String param2, final String param3, final String param4 ) {
		return translateWithParameters( nerd, param1, param2, param3, param4 );
	}

	@Override
	public String translate( final String nerd, final int param1, final int param2 ) {
		return translateWithParameters( nerd, String.valueOf( param1 ), String.valueOf( param2 ) );
	}

	@Override
	public String translate( final String nerd, final long param ) {
		return translateWithParameters( nerd, String.valueOf( param ) );
	}

	@Override
	public String translateWithParameters( final String nerd, final String... params ) {

		String result = translator.translate( nerd, Language.RU ); // TODO: set System or User defined Language

		int i = 1;
		for ( String param : params ) {
			result = result.replace( String.format( "$%d", i++ ), param );
		}

		return markAsTranslated( result );
	}

	public void initTranslations() throws DocumentException {

		final File translationsFile = new File( systemVarsService.getPropertiesPath(), TRANSLATIONS_XML );

		translator = TranslationsReader.getTranslator( translationsFile );
	}

	private String markAsTranslated( final String nerd ) {
		final String translatedSign = systemVarsService.getTranslatedSign();
		return String.format( "%s%s", nerd, StringUtils.isNotEmpty( translatedSign ) ? translatedSign : StringUtils.EMPTY );
	}

	public void setSystemVarsService( final SystemVarsService systemVarsService ) {
		this.systemVarsService = systemVarsService;
	}
}
