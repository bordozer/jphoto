package core.services.dao;

import core.general.anonym.AnonymousDay;

import java.util.List;

public interface AnonymousDaysDao {

	boolean addAnonymousDay( final AnonymousDay day );

	List<AnonymousDay> loadAll();

	void deleteAnonymousDay( final AnonymousDay day );

	boolean isDayAnonymous( final AnonymousDay day );
}
