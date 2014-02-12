package core.general.activity;

import core.context.EnvironmentContext;
import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.services.security.Services;
import core.services.utils.EntityLinkUtilsService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;
import utils.TranslatorUtils;

public class ActivityPhotoUpload extends AbstractActivityStreamEntry {

	private Photo uploadedPhoto;

	public ActivityPhotoUpload( final String activityXML, final Services services ) throws DocumentException {
		super( ActivityType.PHOTO_UPLOAD, services );

		final Document document = DocumentHelper.parseText( activityXML );

		final Element rootElement = document.getRootElement();
		activityOfPhotoId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_ID ).getText() );

		uploadedPhoto = services.getPhotoService().load( activityOfPhotoId );
		if ( uploadedPhoto == null ) {
			registerActivityEntryAsInvisibleForActivityStream();
		}
	}

	public ActivityPhotoUpload( final Photo photo, final Services services ) {
		super( ActivityType.PHOTO_UPLOAD, services );

		this.uploadedPhoto = photo;

		activityTime = photo.getUploadTime();

		activityOfUserId = photo.getUserId();
		activityOfPhotoId = photo.getId();
	}

	@Override
	public String buildActivityXML() {
		return getXML( ACTIVITY_XML_TAG_ID, uploadedPhoto.getId() );
	}

	@Override
	public String getActivityDescription() {
		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

		return TranslatorUtils.translate( "uploaded photo $1"
			, entityLinkUtilsService.getPhotoCardLink( uploadedPhoto )
		);
	}

	public String getDisplayActivityUserLink() {

		// there is acceptable to use EnvironmentContext.getCurrentUser() because this is UI called method
		if ( services.getSecurityService().isPhotoAuthorNameMustBeHidden( uploadedPhoto, EnvironmentContext.getCurrentUser() ) ) {
			return services.getConfigurationService().getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME );
		}

		return services.getEntityLinkUtilsService().getUserCardLink( services.getUserService().load( uploadedPhoto.getUserId() ) );
	}

	@Override
	public int getDisplayActivityUserId() {
		// there is acceptable to use EnvironmentContext.getCurrentUser() because this is UI called method
		return services.getSecurityService().isPhotoAuthorNameMustBeHidden( uploadedPhoto, EnvironmentContext.getCurrentUser() ) ? 0 : activityOfUserId;
	}

	@Override
	public String getDisplayActivityIcon() {
		return getPhotoIcon( uploadedPhoto );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", getActivityType(), uploadedPhoto );
	}
}
