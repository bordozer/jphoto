package com.bordozer.jphoto.core.general.data;

import com.bordozer.jphoto.core.general.user.User;

public class UserRating {

    private User user;
    private int rating;

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(final int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("%s: %d", user.getName(), rating);
    }
}
