package admin.controllers.genres.edit;

import core.general.base.AbstractGeneralModel;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GenreEditDataModel extends AbstractGeneralModel {

	public static final String GENRE_EDIT_DATA_ID_FORM_CONTROL = "genreId";
	public static final String GENRE_EDIT_DATA_NAME_FORM_CONTROL = "name";

	public static final String GENRE_EDIT_DATA_ALLOWED_VOTING_CATEGORIES_FORM_CONTROL = "allowedVotingCategoryIDs";

	public static final String GENRE_EDIT_MIN_MARKS_FORM_CONTROL = "minMarksForBest";

	public static final String GENRE_EDIT_DATA_CAN_CONTAIN_NUDE_CONTENT_FORM_CONTROL = "canContainNudeContent";
	public static final String GENRE_EDIT_DATA_CONTAINS_NUDE_CONTENT_FORM_CONTROL = "containsNudeContent";

	public static final String GENRE_EDIT_DATA_DESCRIPTION_FORM_CONTROL = "description";

	private Genre genre;
	private List<PhotoVotingCategory> photoVotingCategories = newArrayList();
	private List<String> allowedVotingCategoryIDs = newArrayList();

	public Genre getGenre() {
		return genre;
	}

	public void setGenre( final Genre genre ) {
		this.genre = genre;
	}

	@Override
	public void clear() {
		super.clear();

		genre = null;
		allowedVotingCategoryIDs = newArrayList();
	}

	public int getGenreId() {
		return genre.getId();
	}

	public void setGenreId( final int genreId ) {
		genre.setId( genreId );
	}

	public String getName() {
		return genre.getName();
	}

	public void setName( final String name ) {
		genre.setName( name.trim() );
	}

	public String getMinMarksForBest() {
		return String.valueOf( genre.getMinMarksForBest() );
	}

	public void setMinMarksForBest( final String minMarksForBest ) {
		genre.setMinMarksForBest( Integer.parseInt( minMarksForBest ) );
	}

	public String getDescription() {
		return genre.getDescription();
	}

	public void setDescription( final String description ) {
		genre.setDescription( description.trim() );
	}

	public List<PhotoVotingCategory> getPhotoVotingCategories() {
		return photoVotingCategories;
	}

	public void setPhotoVotingCategories( final List<PhotoVotingCategory> photoVotingCategories ) {
		this.photoVotingCategories = photoVotingCategories;
	}

	public List<String> getAllowedVotingCategoryIDs() {
		return allowedVotingCategoryIDs;
	}

	public void setAllowedVotingCategoryIDs( final List<String> allowedVotingCategoryIDs ) {
		this.allowedVotingCategoryIDs = allowedVotingCategoryIDs;
	}

	public List<PhotoVotingCategory> getAllowedVotingCategories() {
		final List<PhotoVotingCategory> photoVotingCategories = newArrayList( this.photoVotingCategories );
		CollectionUtils.filter( photoVotingCategories, new Predicate<PhotoVotingCategory>() {

			@Override
			public boolean evaluate( final PhotoVotingCategory photoVotingCategory ) {
				return allowedVotingCategoryIDs.contains( String.valueOf( photoVotingCategory.getId() ) );
			}
		} );

		return photoVotingCategories;
	}

	public boolean isCanContainNudeContent() {
		return genre.isCanContainNudeContent();
	}

	public void setCanContainNudeContent( final boolean canContainNudeContent ) {
		genre.setCanContainNudeContent( canContainNudeContent );
	}

	public boolean isContainsNudeContent() {
		return genre.isContainsNudeContent();
	}

	public void setContainsNudeContent( final boolean containsNudeContent ) {
		genre.setContainsNudeContent( containsNudeContent );
	}
}
