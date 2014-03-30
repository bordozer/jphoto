package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.general.photo.Photo;
import core.general.user.User;
import core.log.LogHelper;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;

import java.util.Date;

public class PhotoActionGenerationPreviewsJob extends AbstractPhotoActionGenerationJob {

	public PhotoActionGenerationPreviewsJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( SavedJobType.ACTIONS_GENERATION_VIEWS, new LogHelper( PhotoActionGenerationPreviewsJob.class ), jobEnvironment );
	}

	@Override
	public boolean doPhotoAction( final Photo photo, final User user ) {
		final Date actionTime = getPhotoActionTime( photo.getUploadTime() );

		final DateUtilsService dateUtilsService = services.getDateUtilsService();
		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

		/*final Language language = getLanguage();
		final String message = services.getTranslatorService().translate( "User %s has seen photo $1 ( time: $2 )"
			, language
			, entityLinkUtilsService.getUserCardLink( user, language )
			, entityLinkUtilsService.getPhotoCardLink( photo, language )
			, dateUtilsService.formatDateTime( actionTime )
		);*/

		final TranslatableMessage translatableMessage = new TranslatableMessage( "User %s has seen photo $1 ( time: $2 )", services )
			.addUserCardLinkParameter( user )
			.addPhotoCardLinkParameter( photo )
			.addFormattedDateTimeUnit( actionTime )
			;
		addJobRuntimeLogMessage( translatableMessage );

		return savePhotoPreview( photo, user, actionTime );
	}
}
