package core.enums;

public enum SavedJobParameterKey {

	PARAM_ACTIONS_QTY( 1 )
	, PARAM_PICTURES_DIR( 3 )
	, PARAM_PHOTOS_QTY( 4 )
	, PARAM_DATE_FROM( 5 )
	, PARAM_DATE_TO( 6 )
	, DELETE_PICTURE_AFTER_IMPORT( 7 )
	, PARAM_PREVIEW_SIZE( 8 )
	, PARAM_SKIP_PHOTO_WITH_PREVIEW( 9 )
	, ACTIONS_QTY( 10 ) /* TODO: duplicate */
	, PARAM_USER_QTY( 11 )
	, PARAM_DATE_RANGE_TYPE_ID( 12 )
	, PARAM_TIME_PERIOD( 13 )
	, PARAM_AVATARS_DIR( 14 )
	, PARAM_FAVORITE_ENTRIES( 15 )
	, PARAM_USER_ID( 16 )
	, PARAM_SAVED_JOB_CHAIN( 17 )
	, JOB_RUN_MODE_ID( 18 )
	, BREAK_CHAIN_EXECUTION_IF_ERROR( 19 )
	, USER_NAME( 20 )
	, USER_GENDER_ID( 21 )
	, USER_MEMBERSHIP_ID( 22 )
	, IMPORT_REMOTE_PHOTO_SITE_COMMENTS( 23 )
	, DELAY_BETWEEN_REQUESTS( 24 )
	, PHOTOS_IMPORT_SOURCE( 25 )
	, IMPORT_PAGE_QTY( 26 )
	, DAYS( 27 )
	, ENTRY_TYPES( 28 )
	, REMOTE_PHOTO_SITE_CATEGORIES( 29 )
	, BREAK_IMPORT_IF_ALREADY_IMPORTED_PHOTO_FOUND( 30 )
	, PHOTO_IMAGE_IMPORT_STRATEGY( 31 )

	, PREVIEWS_ARCHIVING_ENABLED( 40 )
	, APPRAISAL_ARCHIVING_ENABLED( 41 )
	, PHOTOS_ARCHIVING_ENABLED( 42 )
	;

	private final int id;

	private SavedJobParameterKey( final int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static SavedJobParameterKey getById( final int id ) {
		for ( final SavedJobParameterKey upgradeTaskResult : SavedJobParameterKey.values() ) {
			if ( upgradeTaskResult.getId() == id ) {
				return upgradeTaskResult;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal SavedJobParameterKey id: %d", id ) );
	}
}
