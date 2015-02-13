package admin.controllers.configuration.edit;

import core.general.configuration.Configuration;
import core.general.configuration.ConfigurationDataType;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.services.entry.GenreService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ui.context.EnvironmentContext;
import utils.NumberUtils;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class ConfigurationValidator implements Validator {

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private GenreService genreService;

	private List<ConfigurationKey> integerConfigurationKeys = newArrayList();
	private List<ConfigurationKey> numericConfigurationKeys = newArrayList();
	private List<ConfigurationKey> positiveConfigurationKeys = newArrayList();
	private List<ConfigurationKey> positiveOrZeroConfigurationKeys = newArrayList();
	private List<ConfigurationKey> negativeOrZeroConfigurationKeys = newArrayList();
	private List<ConfigurationKey> negativeConfigurationKeys = newArrayList();

	{
		positiveConfigurationKeys.add( ConfigurationKey.SYSTEM_LOGIN_MAX_LENGTH );
		positiveConfigurationKeys.add( ConfigurationKey.SYSTEM_LOGIN_MIN_LENGTH );
		positiveConfigurationKeys.add( ConfigurationKey.SYSTEM_USER_NAME_MIN_LENGTH );
		positiveConfigurationKeys.add( ConfigurationKey.SYSTEM_USER_NAME_MIN_LENGTH );
		positiveConfigurationKeys.add( ConfigurationKey.MEMBERS_FILE_MAX_SIZE_KB );
		positiveConfigurationKeys.add( ConfigurationKey.CANDIDATES_FILE_MAX_SIZE_KB );
		positiveConfigurationKeys.add( ConfigurationKey.CANDIDATES_PHOTO_VOTING_HIGHEST_MARK );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_UPLOAD_MAX_WIDTH );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_UPLOAD_MAX_HEIGHT );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_UPLOAD_AVATAR_MAX_WIDTH );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_UPLOAD_AVATAR_MAX_HEIGHT );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_UPLOAD_AVATAR_MAX_SIZE_KB );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_LIST_PHOTOS_ON_PAGE );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_LIST_PHOTOS_ON_PAGE_FOR_MOBILE_DEVICES );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY );
		positiveConfigurationKeys.add( ConfigurationKey.RANK_VOTING_POINTS_BASE_STEP );
		positiveConfigurationKeys.add( ConfigurationKey.COMMENTS_MAX_LENGTH );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_VOTING_HIGHEST_POSITIVE_MARK );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_VOTING_LOWEST_POSITIVE_MARK );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
		positiveConfigurationKeys.add( ConfigurationKey.ADMIN_JOB_HISTORY_ITEMS_ON_PAGE );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_CARD_MAX_HEIGHT );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_CARD_MAX_WIDTH );
		positiveConfigurationKeys.add( ConfigurationKey.PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT );
		positiveConfigurationKeys.add( ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE );

		for ( final ConfigurationKey configurationKey : ConfigurationKey.values() ) {

			if ( ConfigurationDataType.INTEGER_KEYS.contains( configurationKey.getDataType() ) ) {
				integerConfigurationKeys.add( configurationKey );
			}

			if ( ConfigurationDataType.NUMERIC_KEYS.contains( configurationKey.getDataType() ) ) {
				numericConfigurationKeys.add( configurationKey );
			}

			/*if ( configurationKey.getTab() == ConfigurationTab.CACHE ) {
				positiveConfigurationKeys.add( configurationKey );
			}*/
		}
	}

	{
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.MEMBERS_DAILY_FILE_SIZE_LIMIT );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.MEMBERS_WEEKLY_FILE_SIZE_LIMIT );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.MEMBERS_PHOTOS_PER_DAY_LIMIT );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.MEMBERS_PHOTOS_PER_WEEK_LIMIT );
//		positiveOrZeroConfigurationKeys.add( ConfigurationKey.CANDIDATES_ADMIN_MUST_CONFIRM_FIRST_N_PHOTOS_OF_CANDIDATE );
//		positiveOrZeroConfigurationKeys.add( ConfigurationKey.CANDIDATES_PHOTOS_QTY_TO_BECOME_MEMBER );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.CANDIDATES_DAILY_FILE_SIZE_LIMIT );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.CANDIDATES_WEEKLY_FILE_SIZE_LIMIT );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.CANDIDATES_PHOTOS_PER_DAY_LIMIT );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.CANDIDATES_PHOTOS_PER_WEEK_LIMIT );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_PHOTOS );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.PHOTO_UPLOAD_ADDITIONAL_SIZE_WEEKLY_LIMIT_PER_RANK_KB );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.COMMENTS_MIN_LENGTH );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.COMMENTS_DELAY_AFTER_COMMENT_SEC );
//		positiveOrZeroConfigurationKeys.add( ConfigurationKey.COMMENTS_USER_MUST_HAVE_N_APPROVED_PHOTOS_BEFORE_CAN_LEAVE_COMMENT );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.PHOTO_RATING_PORTAL_PAGE_BEST_PHOTOS_FROM_PHOTOS_THAT_GOT_ENOUGH_MARKS_FOR_N_LAST_DAYS );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_PHOTO_OF_THE_DAY );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
		positiveOrZeroConfigurationKeys.add( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO );
	}

	{
		negativeOrZeroConfigurationKeys.add( ConfigurationKey.PHOTO_VOTING_HIGHEST_NEGATIVE_MARK );
		negativeOrZeroConfigurationKeys.add( ConfigurationKey.CANDIDATES_PHOTO_VOTING_LOWEST_MARK );
		negativeOrZeroConfigurationKeys.add( ConfigurationKey.PHOTO_VOTING_LOWEST_NEGATIVE_MARK );
	}

//	{
//		negativeConfigurationKeys.add( ConfigurationKey.PHOTO_VOTING_HIGHEST_NEGATIVE_MARK );
//		negativeConfigurationKeys.add( ConfigurationKey.PHOTO_VOTING_LOWEST_NEGATIVE_MARK );
//	}

	@Override
	public boolean supports( final Class<?> clazz ) {
		return ConfigurationEditModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final ConfigurationEditModel model = ( ConfigurationEditModel ) target;

		final Map<String,Configuration> configurationMap = model.getConfigurationMap();

		validatePositiveKeys( configurationMap, errors );

		validatePositiveOrZeroKeys( configurationMap, errors );

		validateNegativeKeys( configurationMap, errors );

		validateNegativeOrZeroKeys( configurationMap, errors );

		validatePhotoVoting( configurationMap, errors );

		validateMinMaxLoginLength( configurationMap, errors );

		validateMinMaxUserNameLength( configurationMap, errors );

		validateMaxPhotoNameLength( configurationMap, errors );

		validateAllowedForUploadingFileTypes( configurationMap, errors );

		validateIntegerValues( configurationMap, errors );

		validateNumericValues( configurationMap, errors );

		validatePhotoAppraisalCategoriesCount( configurationMap, errors );
	}

	private void validatePhotoAppraisalCategoriesCount( final Map<String, Configuration> configurationMap, final Errors errors ) {

		final ConfigurationKey configurationKey = ConfigurationKey.PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT;
		final int photoAppraisalCategoriesCount = getConfiguration( configurationMap, configurationKey ).getValueInt();

		final List<Genre> genres = genreService.loadAll();
		for ( final Genre genre : genres ) {

			final int categoriesChecked = genre.getPhotoVotingCategories().size();

			if ( categoriesChecked < photoAppraisalCategoriesCount ) {
				errors.rejectValue( "configurationMap", addConfigurationTab( translatorService.translate( "Genre '$1': add $2 appraisal category first"
					, getLanguage()
					, genre.getName()
					, String.valueOf( photoAppraisalCategoriesCount - categoriesChecked )
				), configurationKey ) );
			}
		}
	}

	private void validateMaxPhotoNameLength( final Map<String, Configuration> configurationMap, final Errors errors ) {
		final ConfigurationKey configurationKey = ConfigurationKey.SYSTEM_PHOTO_NAME_MAX_LENGTH;

		final Configuration configuration = getConfiguration( configurationMap, configurationKey );

		if ( configuration.getValueInt() > 255 ) {
			errors.rejectValue( "configurationMap", addConfigurationTab( translatorService.translate( "$1 should be less then 255 symbols", getLanguage()
				, String.valueOf( configurationKey.getId() ) ), configurationKey ) );
		}
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}

	private void validateAllowedForUploadingFileTypes( final Map<String, Configuration> configurationMap, final Errors errors ) {
		final ConfigurationKey configurationKey = ConfigurationKey.PHOTO_UPLOAD_FILE_ALLOWED_EXTENSIONS;

		final Configuration configuration = getConfiguration( configurationMap, configurationKey );

		if ( configuration.getValueListString().size() == 0 ) {
			errors.rejectValue( "configurationMap", addConfigurationTab( translatorService.translate( "$1 should not be empty", getLanguage(), String.valueOf(  configurationKey.getId() ) ), configurationKey ) );
		}
	}

	private void validateMinMaxLoginLength( final Map<String, Configuration> configurationMap, final Errors errors ) {
		final ConfigurationKey configurationKey1 = ConfigurationKey.SYSTEM_LOGIN_MIN_LENGTH;
		final ConfigurationKey configurationKey2 = ConfigurationKey.SYSTEM_LOGIN_MAX_LENGTH;

		final Configuration configuration1 = getConfiguration( configurationMap, configurationKey1 );
		final Configuration configuration2 = getConfiguration( configurationMap, configurationKey2 );

		if ( configuration1.getValueInt() > configuration2.getValueInt() ) {
			errors.rejectValue( "configurationMap", addConfigurationTab( translatorService.translate( "$1 should be less then $2", getLanguage()
				, String.valueOf( configurationKey1.getId() ), String.valueOf( configurationKey2.getId() ) ), configurationKey1 ) );
		}
	}

	private void validateMinMaxUserNameLength( final Map<String, Configuration> configurationMap, final Errors errors ) {
		final ConfigurationKey configurationKey1 = ConfigurationKey.SYSTEM_USER_NAME_MIN_LENGTH;
		final ConfigurationKey configurationKey2 = ConfigurationKey.SYSTEM_USER_NAME_MAX_LENGTH;

		final Configuration configuration1 = getConfiguration( configurationMap, configurationKey1 );
		final Configuration configuration2 = getConfiguration( configurationMap, configurationKey2 );

		if ( configuration1.getValueInt() > configuration2.getValueInt() ) {
			errors.rejectValue( "configurationMap", addConfigurationTab( translatorService.translate( "$1 should be less then $2", getLanguage()
				, String.valueOf( configurationKey1.getId() ), String.valueOf( configurationKey2.getId() ) ), configurationKey1 ) );
		}
	}

	private void validatePhotoVoting( final Map<String, Configuration> configurationMap, final Errors errors ) {
		final Configuration configuration1 = getConfiguration( configurationMap, ConfigurationKey.PHOTO_VOTING_HIGHEST_POSITIVE_MARK );
		final Configuration configuration2 = getConfiguration( configurationMap, ConfigurationKey.PHOTO_VOTING_LOWEST_POSITIVE_MARK );
		if ( configuration1.getValueInt() < configuration2.getValueInt() ) {
			errors.rejectValue( "configurationMap"
				, addConfigurationTab( translatorService.translate( "$1 should be less then $2", getLanguage()
				, String.valueOf( ConfigurationKey.PHOTO_VOTING_HIGHEST_POSITIVE_MARK.getId() ), String.valueOf( ConfigurationKey.PHOTO_VOTING_LOWEST_POSITIVE_MARK.getId() ) ), configuration1.getConfigurationKey() ) );
		}

		final Configuration configuration3 = getConfiguration( configurationMap, ConfigurationKey.PHOTO_VOTING_HIGHEST_NEGATIVE_MARK );
		final Configuration configuration4 = getConfiguration( configurationMap, ConfigurationKey.PHOTO_VOTING_LOWEST_NEGATIVE_MARK );
		if ( configuration3.getValueInt() < configuration4.getValueInt() ) {
			errors.rejectValue( "configurationMap"
				, addConfigurationTab( translatorService.translate( "$1 should be more then $2", getLanguage()
				, String.valueOf( ConfigurationKey.PHOTO_VOTING_HIGHEST_NEGATIVE_MARK.getId() ), String.valueOf( ConfigurationKey.PHOTO_VOTING_LOWEST_NEGATIVE_MARK.getId() ) ), configuration1.getConfigurationKey() ) );
		}
	}

	private void validatePositiveKeys( final Map<String, Configuration> configurationMap, final Errors errors ) {
		for ( final ConfigurationKey configurationKey : positiveConfigurationKeys ) {
			if ( getConfiguration( configurationMap, configurationKey ).getValueInt() <= 0 ) {
				errors.rejectValue( "configurationMap", addConfigurationTab( translatorService.translate( "$1 should be positive", getLanguage(), String.valueOf( configurationKey.getId() ) ), configurationKey ) );
			}
		}
	}

	private void validateIntegerValues( final Map<String, Configuration> configurationMap, final Errors errors ) {
		for ( final ConfigurationKey configurationKey : integerConfigurationKeys ) {
			final String value = getConfiguration( configurationMap, configurationKey ).getValue();
			if ( /*StringUtils.isNotEmpty( value ) &&*/ ! NumberUtils.isInteger( value ) ) {
				errors.rejectValue( "configurationMap", addConfigurationTab( translatorService.translate( "$1 should be integer", getLanguage(), String.valueOf( configurationKey.getId() ) ), configurationKey ) );
			}
		}
	}

	private void validateNumericValues( final Map<String, Configuration> configurationMap, final Errors errors ) {
		for ( final ConfigurationKey configurationKey : numericConfigurationKeys ) {
			final String value = getConfiguration( configurationMap, configurationKey ).getValue();
			if ( /*StringUtils.isNotEmpty( value ) &&*/ ! NumberUtils.isNumeric( value ) ) {
				errors.rejectValue( "configurationMap", addConfigurationTab( translatorService.translate( "$1 should be numeric", getLanguage(), String.valueOf( configurationKey.getId() ) ), configurationKey ) );
			}
		}
	}

	private void validatePositiveOrZeroKeys( final Map<String, Configuration> configurationMap, final Errors errors ) {
		for ( final ConfigurationKey configurationKey : positiveOrZeroConfigurationKeys ) {
			if ( getConfiguration( configurationMap, configurationKey ).getValueInt() < 0 ) {
				errors.rejectValue( "configurationMap", addConfigurationTab( translatorService.translate( "$1 should be 0 or positive", getLanguage(), String.valueOf( configurationKey.getId() ) ), configurationKey ) );
			}
		}
	}

	private void validateNegativeOrZeroKeys( final Map<String, Configuration> configurationMap, final Errors errors ) {
		for ( final ConfigurationKey configurationKey : negativeOrZeroConfigurationKeys ) {
			if ( getConfiguration( configurationMap, configurationKey ).getValueInt() > 0 ) {
				errors.rejectValue( "configurationMap", addConfigurationTab( translatorService.translate( "$1 ($2) should be negative or 0", getLanguage(), String.valueOf(  configurationKey.getId() ) ), configurationKey ) );
			}
		}
	}

	private void validateNegativeKeys( final Map<String, Configuration> configurationMap, final Errors errors ) {
		for ( final ConfigurationKey configurationKey : negativeConfigurationKeys ) {
			if ( getConfiguration( configurationMap, configurationKey ).getValueInt() >= 0 ) {
				errors.rejectValue( "configurationMap", addConfigurationTab( translatorService.translate( "$1 should be negative", getLanguage(), String.valueOf(  configurationKey.getId() ) ), configurationKey ) );
			}
		}
	}

	private Configuration getConfiguration( final Map<String, Configuration> configurationMap, final ConfigurationKey configurationKey ) {
		return configurationMap.get( String.valueOf( configurationKey.getId() ) );
	}

	private String addConfigurationTab( final String message, final ConfigurationKey configurationKey ) {
		return String.format( "%s. (Tab: %s)", message, translatorService.translate( configurationKey.getTab().getName(), getLanguage() )  );
	}
}
