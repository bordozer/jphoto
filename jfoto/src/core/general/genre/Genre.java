package core.general.genre;

import core.general.base.AbstractBaseEntity;
import core.general.photo.PhotoVotingCategory;
import core.interfaces.Nameable;
import core.interfaces.Cacheable;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class Genre extends AbstractBaseEntity implements Nameable, Comparable<Genre>, Cacheable {

	private String name;
	private List<PhotoVotingCategory> photoVotingCategories;

	private int minMarksForBest;

	private boolean canContainNudeContent;
	private boolean containsNudeContent;

	private String description;

	public Genre() {

	}
	public Genre( final Genre genre ) {
		setId( genre.getId() );

		name = genre.getName();
		photoVotingCategories = newArrayList( genre.getPhotoVotingCategories() );
		minMarksForBest = genre.getMinMarksForBest();
		canContainNudeContent = genre.isCanContainNudeContent();
		containsNudeContent = genre.isContainsNudeContent();
		description = genre.getDescription();
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public List<PhotoVotingCategory> getPhotoVotingCategories() {
		return photoVotingCategories;
	}

	public void setPhotoVotingCategories( final List<PhotoVotingCategory> photoVotingCategories ) {
		this.photoVotingCategories = photoVotingCategories;
	}

	public int getMinMarksForBest() {
		return minMarksForBest;
	}

	public void setMinMarksForBest( final int minMarksForBest ) {
		this.minMarksForBest = minMarksForBest;
	}

	public boolean isCanContainNudeContent() {
		return canContainNudeContent;
	}

	public void setCanContainNudeContent( final boolean canContainNudeContent ) {
		this.canContainNudeContent = canContainNudeContent;
	}

	public boolean isContainsNudeContent() {
		return containsNudeContent;
	}

	public void setContainsNudeContent( final boolean containsNudeContent ) {
		this.containsNudeContent = containsNudeContent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( final String description ) {
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", getId(), name );
	}

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof Genre ) ) {
			return false;
		}

		final Genre genre = ( Genre ) obj;
		return genre.getId() == getId();
	}

	@Override
	public int compareTo( final Genre genre ) {
		return name.compareTo( genre.getName() );
	}
}
