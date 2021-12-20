package com.bordozer.jphoto.core.general.user.userAlbums;


import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.interfaces.Cacheable;
import com.bordozer.jphoto.core.interfaces.Nameable;
import org.apache.commons.lang3.StringEscapeUtils;

public class UserPhotoAlbum extends AbstractBaseEntity implements Nameable, Cacheable {

    private User user;
    private String name;
    private String description;

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public String getNameEscaped() {
        return StringEscapeUtils.escapeHtml3(name);
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Album %s of %s", name, user);
    }

    @Override
    public int hashCode() {
        return 31 * getId();
    }

    public int getHashCode() {
        return hashCode();
    }
}
