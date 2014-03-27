package core.services.validation;

import core.context.EnvironmentContext;
import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;

public class DataRequirementServiceImpl implements DataRequirementService {

	@Autowired
	private TranslatorService translatorService;

	@Override
	public UserRequirement getUserRequirement() {
		return new UserRequirement( translatorService );
	}

	@Override
	public PhotoRequirement getPhotoRequirement() {
		return new PhotoRequirement( translatorService );
	}

	@Override
	public String getFieldIsMandatoryText() {
		return translatorService.translate( "The field is mandatory.", EnvironmentContext.getLanguage() );
	}

	@Override
	public String getFieldIsOptionalText() {
		return translatorService.translate( "The field is optional.", EnvironmentContext.getLanguage() );
	}
}
