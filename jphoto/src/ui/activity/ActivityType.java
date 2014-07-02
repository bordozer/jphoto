package ui.activity;

import admin.jobs.enums.SavedJobType;
import core.interfaces.IdentifiableNameable;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public enum ActivityType implements IdentifiableNameable {

	USER_REGISTRATION( 1, "ActivityType: New member registration", SavedJobType.USER_GENERATION.getIcon() )
	, PHOTO_UPLOAD( 2, "ActivityType: New photo upload", SavedJobType.PHOTOS_IMPORT.getIcon() )
	, PHOTO_VOTING( 3, "ActivityType: Photo voting", SavedJobType.ACTIONS_GENERATION.getIcon() )
	, PHOTO_COMMENT( 4, "ActivityType: Photo comment", SavedJobType.ACTIONS_GENERATION_COMMENTS.getIcon() )
	, PHOTO_PREVIEW( 5, "ActivityType: Photo preview", SavedJobType.ACTIONS_GENERATION_VIEWS.getIcon() )
	, FAVORITE_ACTION( 6, "ActivityType: Favorite action", SavedJobType.FAVORITES_GENERATION.getIcon() )
	, VOTING_FOR_USER_RANK_IN_GENRE( 7, "ActivityType: Voting for user rank in genre", SavedJobType.RANK_VOTING_GENERATION.getIcon() )
	, USER_STATUS( 8, "ActivityType: Member status", SavedJobType.USER_STATUS.getIcon() )
	, USER_RANK_IN_GENRE_CHANGED( 9, "ActivityType: Member's rank in genre changed", SavedJobType.USER_GENRES_RANKS_RECALCULATING.getIcon() )
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
