package admin.controllers.genres.edit;

import core.general.base.AbstractGeneralModel;
import core.general.photo.PhotoVotingCategory;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import ui.services.validation.DataRequirementService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GenreEditDataModel extends AbstractGeneralModel {

	public static final String GENRE_EDIT_DATA_ID_FORM_CONTROL = "genreId";
	public static final String GENRE_EDIT_DATA_NAME_FORM_CONTROL = "genreName";
	public static final String GENRE_EDIT_DATA_DESCRIPTION_FORM_CONTROL = "description";

	public static final String GENRE_EDIT_DATA_ALLOWED_VOTING_CATEGORIES_FORM_CONTROL = "allowedVotingCategoryIDs";

	public static final String GENRE_EDIT_MIN_MARKS_FORM_CONTROL = "minMarksForBest";

	private int genreId;
	private String genreName;
	private String description;
	private boolean canContainNudeContent;
	private boolean ContainsNudeContent;
	private int minMarksForBest;

	private List<PhotoVotingCategory> photoVotingCategories = newArrayList();
	private List<String> allowedVotingCategoryIDs = newArrayList();

	private DataRequirementService dataRequirementService;

	@Override
	public void clear() {
		super.clear();

		genreId = 0;
		genreName = null;
		allowedVotingCategoryIDs = newArrayList();
	}

	public int getGenreId() {
		return genreId;
	}

	public void setGenreId( final int genreId ) {
		this.genreId = genreId;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName( final String genreName ) {
		this.genreName = genreName;
	}

	public int getMinMarksForBest() {
		return minMarksForBest;
	}

	public void setMinMarksForBest( final int minMarksForBest ) {
		this.minMarksForBest = minMarksForBest;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( final String description ) {
		this.description = description;
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
		return canContainNudeContent;
	}

	public void setCanContainNudeContent( final boolean canContainNudeContent ) {
		this.canContainNudeContent = canContainNudeContent;
	}

	public boolean isContainsNudeContent() {
		return ContainsNudeContent;
	}

	public void setContainsNudeContent( final boolean containsNudeContent ) {
		ContainsNudeContent = containsNudeContent;
	}

	public DataRequirementService getDataRequirementService() {
		return dataRequirementService;
	}

	public void setDataRequirementService( final DataRequirementService dataRequirementService ) {
		this.dataRequirementService = dataRequirementService;
	}
}
