package controllers.photos.edit;

public enum PhotoEditWizardStep {
	EDIT_PHOTO_DATA( 1, "", "Step1: Photo's data" )
	, PHOTO_FILE_UPLOAD( 2, "upload", "Step 2: Photo File Uploading" )
	, PHOTO_SAVING( 3, "save", "Step 3: Saving" )
	;

	private final int id;
	private final String urlPrefix;
	private final String stepDescription;

	private PhotoEditWizardStep( final int id, final String urlPrefix, final String stepDescription ) {
		this.id = id;
		this.urlPrefix = urlPrefix;
		this.stepDescription = stepDescription;
	}

	public int getId() {
		return id;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public String getStepDescription() {
		return stepDescription;
	}

	public static PhotoEditWizardStep getById( final int id ) {
		for ( final PhotoEditWizardStep photoEditWizardStep : PhotoEditWizardStep.values() ) {
			if ( photoEditWizardStep.getId() == id ) {
				return photoEditWizardStep;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal PhotoEditWizardStep id: %s", id ) );
	}
}
