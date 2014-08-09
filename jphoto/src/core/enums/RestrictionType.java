package core.enums;

import core.interfaces.IdentifiableNameable;

import java.util.EnumSet;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public enum RestrictionType implements IdentifiableNameable {

	USER_LOGIN( 1, "RestrictionType: User login in" )
	, USER_PHOTO_UPLOADING( 2, "RestrictionType: User photo uploading" )
	, USER_COMMENTING( 3, "RestrictionType: User commenting" )
	, USER_MESSAGING( 4, "RestrictionType: User messaging" )
	, USER_PHOTO_APPRAISAL( 5, "RestrictionType: User photo appraisal" )
	, USER_VOTING_FOR_RANK_IN_GENRE( 6, "RestrictionType: User voting fo rank in genre" )
	, PHOTO_TO_BE_PHOTO_OF_THE_DAY( 7, "RestrictionType: Photo: To be photo of the day" )
	, PHOTO_TO_BE_BEST_IN_GENRE( 8, "RestrictionType: Photo: To show in the best photos of genre" )
	, PHOTO_COMMENTING( 9, "RestrictionType: Photo: Commenting" )
	;

	public final static List<RestrictionType> FOR_USERS = newArrayList( USER_LOGIN, USER_PHOTO_UPLOADING, USER_COMMENTING, USER_MESSAGING, USER_PHOTO_APPRAISAL, USER_VOTING_FOR_RANK_IN_GENRE );
	public final static List<RestrictionType> FOR_PHOTOS = newArrayList( PHOTO_TO_BE_PHOTO_OF_THE_DAY, PHOTO_TO_BE_BEST_IN_GENRE, PHOTO_COMMENTING );

	private final int id;
	private final String name;

	private RestrictionType( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static RestrictionType getById( final int id ) {
		for ( final RestrictionType restrictionType : RestrictionType.values() ) {
			if ( restrictionType.getId() == id ) {
				return restrictionType;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal RestrictionType id: %d", id ) );
	}
}
