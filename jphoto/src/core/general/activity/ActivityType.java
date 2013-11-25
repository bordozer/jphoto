package core.general.activity;

import admin.jobs.enums.SavedJobType;
import utils.TranslatorUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public enum ActivityType {

	USER_REGISTRATION( 1, "New member registration", SavedJobType.USER_GENERATION.getIcon() )
	, PHOTO_UPLOAD( 2, "New photo upload", SavedJobType.PREVIEW_GENERATION.getIcon() )
	, PHOTO_VOTING( 3, "Photo voting", SavedJobType.ACTIONS_GENERATION.getIcon() )
	, PHOTO_COMMENT( 4, "Photo comment", SavedJobType.ACTIONS_GENERATION_COMMENTS.getIcon() )
	, PHOTO_PREVIEW( 5, "Photo preview", SavedJobType.ACTIONS_GENERATION_VIEWS.getIcon() )
	, FAVORITE_ACTION( 6, "Favorite action", SavedJobType.FAVORITES_GENERATION.getIcon() )
	, VOTING_FOR_USER_RANK_IN_GENRE( 7, "Voting for user rank in genre", SavedJobType.RANK_VOTING_GENERATION.getIcon() )
	;

	public final static List<ActivityType> SYSTEM_ACTIVITIES = newArrayList( ActivityType.values() );
	public final static List<ActivityType> USER_ACTIVITIES = SYSTEM_ACTIVITIES;
	public final static List<ActivityType> PHOTO_ACTIVITIES = newArrayList( PHOTO_UPLOAD, PHOTO_VOTING, PHOTO_COMMENT, PHOTO_PREVIEW );

	private final int id;
	private final String name;
	private final String icon;

	private ActivityType( final int id, final String name, final String icon ) {
		this.id = id;
		this.name = name;
		this.icon = icon;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNameTranslated() {
		return TranslatorUtils.translate( name );
	}

	public String getIcon() {
		return icon;
	}

	public static ActivityType getById( final int id ) {
		for ( final ActivityType upgradeTaskResult : ActivityType.values() ) {
			if ( upgradeTaskResult.getId() == id ) {
				return upgradeTaskResult;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal ActivityType id: %d", id ) );
	}
}
