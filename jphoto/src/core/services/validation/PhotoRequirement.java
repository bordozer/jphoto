package core.services.validation;

import core.context.ApplicationContextHelper;
import core.general.configuration.ConfigurationKey;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;

public class PhotoRequirement {

	private TranslatorService translatorService;

	public PhotoRequirement( final TranslatorService translatorService ) {
		this.translatorService = translatorService;
	}

	public String getNameRequirement() {

		final ConfigurationService configurationService = ApplicationContextHelper.getBean( ConfigurationService.BEAN_NAME );
		final StringBuilder builder = new StringBuilder();

		builder.append( translatorService.translate( "The name of your photo. Max length is $1 symbols", configurationService.getInt( ConfigurationKey.SYSTEM_PHOTO_NAME_MAX_LENGTH ) ) );

		return builder.toString();
	}

	public String getKeywordsRequirement() {
		final StringBuilder builder = new StringBuilder();

		builder.append( translatorService.translate( "Comma (,) separated keywords." ) );

		return builder.toString();
	}

	public String getDescriptionRequirement() {
		final StringBuilder builder = new StringBuilder();

		builder.append( translatorService.translate( "Any additional information about the photo" ) );

		return builder.toString();
	}
}
