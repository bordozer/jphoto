package admin.jobs.entries;

import admin.jobs.enums.SavedJobType;
import core.general.photo.Photo;
import core.general.user.User;
import core.log.LogHelper;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;

import java.util.Date;

public class PhotoActionGenerationPreviewsJob extends AbstractPhotoActionGenerationJob {

	public PhotoActionGenerationPreviewsJob() {
		super( SavedJobType.ACTIONS_GENERATION_VIEWS, new LogHelper( PhotoActionGenerationPreviewsJob.class ) );
	}

	@Override
	public boolean doPhotoAction( final Photo photo, final User user ) {
		final Date actionTime = getPhotoActionTime( photo.getUploadTime() );

		final DateUtilsService dateUtilsService = services.getDateUtilsService();
		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();
		addJobExecutionFinalMessage( String.format( "User %s has seen photo %s ( time: %s )", entityLinkUtilsService.getUserCardLink( user ), entityLinkUtilsService.getPhotoCardLink( photo ), dateUtilsService.formatDateTime( actionTime ) ) );

		return savePhotoPreview( photo, user, actionTime );
	}
}
