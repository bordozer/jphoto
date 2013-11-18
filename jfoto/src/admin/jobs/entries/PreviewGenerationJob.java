package admin.jobs.entries;

import admin.jobs.enums.SavedJobType;
import core.general.conversion.ConversionOptions;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.photo.Photo;
import core.log.LogHelper;
import core.services.utils.DateUtilsService;
import utils.TranslatorUtils;

import java.awt.*;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class PreviewGenerationJob extends AbstractJob {

	private ConversionOptions options;

	private int previewSize;
	private boolean skipPhotosWithExistingPreview;

	public PreviewGenerationJob() {
		super( new LogHelper( PreviewGenerationJob.class ) );
	}

	@Override
	public void runJob() throws Throwable {

		Photo photo;
		for ( int photoId : beingProcessedPhotosIds ) {
			photo = services.getPhotoService().load( photoId );

			if ( !skipPhotosWithExistingPreview || !services.getUserPhotoFilePathUtilsService().getPhotoPreviewFile( photo ).exists() ) {
				services.getPreviewGenerationService().generatePreview( photo.getId(), options );
			}

			increment();

			if ( hasJobFinishedWithAnyResult() ) {
				break;
			}
		}
	}

	public void setOptions( final ConversionOptions options ) {
		this.options = options;
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.PREVIEW_GENERATION;
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
		final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

		final DateUtilsService dateUtilsService = services.getDateUtilsService();
		parametersMap.put( SavedJobParameterKey.PARAM_PREVIEW_SIZE, new CommonProperty( SavedJobParameterKey.PARAM_PREVIEW_SIZE.getId(), previewSize ) );
		parametersMap.put( SavedJobParameterKey.PARAM_SKIP_PHOTO_WITH_PREVIEW, new CommonProperty( SavedJobParameterKey.PARAM_SKIP_PHOTO_WITH_PREVIEW.getId(), skipPhotosWithExistingPreview ) );

		return parametersMap;
	}

	@Override
	public String getJobParametersDescription() {
		final StringBuilder builder = new StringBuilder();

		totalJopOperations = services.getPhotoService().getPhotoQty(); // TODO: hack!

		builder.append( TranslatorUtils.translate( "Preview size: " ) ).append( previewSize ).append( "<br />" );
		builder.append( TranslatorUtils.translate( "Skip, if preview exists: " ) ).append( TranslatorUtils.translate( skipPhotosWithExistingPreview ? "Yes" : "No" ) ).append( "<br />" );

		return builder.toString();
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
		final int previewSize = jobParameters.get( SavedJobParameterKey.PARAM_PREVIEW_SIZE ).getValueInt();
		final boolean isSkip = jobParameters.get( SavedJobParameterKey.PARAM_SKIP_PHOTO_WITH_PREVIEW ).getValueBoolean();

		totalJopOperations = services.getPhotoService().getPhotoQty();

		this.previewSize = previewSize;
		this.skipPhotosWithExistingPreview = isSkip;

		options = new ConversionOptions();
		options.setDensity( 72 );
		options.setDimension( new Dimension( previewSize, previewSize ) );
	}

	public int getPreviewSize() {
		return previewSize;
	}

	public void setPreviewSize( final int previewSize ) {
		this.previewSize = previewSize;
	}

	public void setSkipPhotosWithExistingPreview( final boolean skipPhotosWithExistingPreview ) {
		this.skipPhotosWithExistingPreview = skipPhotosWithExistingPreview;
	}

	public boolean isSkipPhotosWithExistingPreview() {
		return skipPhotosWithExistingPreview;
	}
}
