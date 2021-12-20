package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.anonym.AnonymousDay;

import java.util.List;

public interface AnonymousDaysDao {

    boolean addAnonymousDay(final AnonymousDay day);

    List<AnonymousDay> loadAll();

    void deleteAnonymousDay(final AnonymousDay day);

    boolean isDayAnonymous(final AnonymousDay day);
}
