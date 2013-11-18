package admin.controllers.jobs.edit.preview;

import admin.controllers.jobs.edit.AbstractAdminJobModel;
import core.general.conversion.ConversionOptions;

public class PreviewGenerationModel extends AbstractAdminJobModel {

	public final static String PREVIEW_SIZE_FORM_CONTROL = "previewSize";

	private String previewSize = String.valueOf( ConversionOptions.DEFAULT_PREVIEW_SIZE );
	private boolean skipPhotosWithExistingPreview = true;

	public String getPreviewSize() {
		return previewSize;
	}

	public void setPreviewSize( final String previewSize ) {
		this.previewSize = previewSize;
	}

	public boolean isSkipPhotosWithExistingPreview() {
		return skipPhotosWithExistingPreview;
	}

	public void setSkipPhotosWithExistingPreview( final boolean skipPhotosWithExistingPreview ) {
		this.skipPhotosWithExistingPreview = skipPhotosWithExistingPreview;
	}

	@Override
	public void clear() {
		super.clear();
		previewSize = String.valueOf( ConversionOptions.DEFAULT_PREVIEW_SIZE );
	}
}
