package core.services.translator;

import core.services.utils.SystemVarsService;
import org.apache.commons.lang.StringUtils;

public class TranslationEntry {

	protected final String nerd;

	private final Language language;
	private final String value;

	protected final SystemVarsService systemVarsService;

	public TranslationEntry( final String nerd, final Language language, final String value, final SystemVarsService systemVarsService ) {
		this.nerd = nerd;

		this.language = language;
		this.value = value;

		this.systemVarsService = systemVarsService;
	}

	public String getNerd() {
		return nerd;
	}

	public Language getLanguage() {
		return language;
	}

	public String getValueWithPrefixes() {
		final String startPrefix = getStartPrefix();
		final String endPrefix = getEndPrefix();
		return String.format( "%s%s%s%s", getPrefix( startPrefix ), value, getPrefix( endPrefix ), getLanguageCode() );
	}

	public String getLanguageCode() {
		return String.format( "<sup>%s</sup>", language.getCode() );
	}

	public String getValue() {
		return value;
	}

	protected String getStartPrefix() {
		return systemVarsService.getTranslatorTranslatedStartPrefix();
	}

	protected String getEndPrefix() {
		return systemVarsService.getTranslatorTranslatedEndPrefix();
	}

	protected String getPrefix( final String prefix ) {
		return StringUtils.isNotEmpty( prefix ) ? prefix : StringUtils.EMPTY;
	}

	@Override
	public int hashCode() {
		return nerd.hashCode() * 31;
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj.getClass().equals( this.getClass() ) ) ) {
			return false;
		}

		final TranslationEntry translationEntry = ( TranslationEntry ) obj;
		return translationEntry.getLanguage() == language && translationEntry.getNerd().equals( nerd );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s '%s'", nerd, language, value );
	}
}
