package admin.controllers.jobs.edit.photosImport.strategies.filesystem;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import core.general.genre.Genre;
import core.general.user.UserMembershipType;

import java.util.List;

public abstract class AbstractPhotographStrategy {

	public abstract List<GenreDiscEntry> getSupportedGenres();

	public abstract List<UserMembershipType> getSupportedMembershipType();

	public final boolean doesThisGenreFitsTheUser( final Genre genre, final UserMembershipType membershipType ) {
		return getSupportedMembershipType().contains( membershipType ) && doesSupportGenre( genre );
	}

	public boolean doesSupportGenre( final Genre genre ) {
		for ( final GenreDiscEntry genreDiscEntry : getSupportedGenres() ) {
			if ( genreDiscEntry.getName().equalsIgnoreCase( genre.getName() ) ) {
				return true;
			}
		}
		return false;
	}
}
