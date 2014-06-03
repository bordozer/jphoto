package ui.services.validation;

import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import ui.context.EnvironmentContext;

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
		return translatorService.translate( "DataRequirement: The field is mandatory", EnvironmentContext.getLanguage() );
	}

	@Override
	public String getFieldIsOptionalText() {
		return translatorService.translate( "DataRequirement: The field is optional", EnvironmentContext.getLanguage() );
	}
}
