package com.bordozer.jphoto.core.general.favorite;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.interfaces.Favoritable;

import java.util.Date;

public class FavoriteEntry extends AbstractBaseEntity implements Favoritable {

    private User user;
    private Favoritable favoriteEntry;
    private Date created;
    private FavoriteEntryType entryType;

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Favoritable getFavoriteEntry() {
        return favoriteEntry;
    }

    public void setFavoriteEntry(final Favoritable favoriteEntry) {
        this.favoriteEntry = favoriteEntry;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public FavoriteEntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(final FavoriteEntryType entryType) {
        this.entryType = entryType;
    }
}
