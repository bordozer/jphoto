package core.general.photo;

import core.general.base.AbstractBaseEntity;
import core.interfaces.Cacheable;
import core.interfaces.Nameable;

public class PhotoVotingCategory extends AbstractBaseEntity implements Nameable, Comparable<PhotoVotingCategory>,  Cacheable {

	private String name;
	private String description;

	@Override
	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
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

		if ( ! ( obj instanceof PhotoVotingCategory ) ) {
			return false;
		}

		final PhotoVotingCategory photoVotingCategory = ( PhotoVotingCategory ) obj;
		return photoVotingCategory.getId() == getId();
	}

	@Override
	public int compareTo( final PhotoVotingCategory category ) {
		return name.compareTo( category.getName() );
	}
}
