package core.services.translator;

public interface TranslatorService {

	String BEAN_NAME = "translatorService";

	String translate( final String nerd );

	String translate( final String nerd, final String param );

	String translate( final String nerd, final String param1, final String param2 );

	String translate( final String nerd, final String param1, final String param2, final String param3 );

	String translate( final String nerd, final String param1, final String param2, final String param3, final String param4 );

	String translate( final String nerd, final int param1, final int param2 );

	String translate( final String nerd, final long param );

	String translateWithParameters( final String nerd, final String... params );
}
