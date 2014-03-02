package core.general.user.userAlbums;


import core.general.base.AbstractBaseEntity;
import core.general.user.User;
import core.interfaces.Cacheable;
import core.interfaces.Nameable;

public class UserPhotoAlbum extends AbstractBaseEntity implements Nameable, Cacheable {

	private User user;
	private String name;
	private String description;

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

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
		return String.format( "Album %s of %s", name, user );
	}

	@Override
	public int hashCode() {
		return 31 * getId();
	}

	public int getHashCode() {
		return hashCode();
	}
}
