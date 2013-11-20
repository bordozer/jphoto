package core.services.entry;

import core.general.anonym.AnonymousDay;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface AnonymousDaysService {

	String BEAN_NAME = "anonymousDaysService";

	boolean addAnonymousDay( final AnonymousDay day );

	List<AnonymousDay> loadAll();

	void deleteAnonymousDay( final AnonymousDay day );

	boolean isDayAnonymous( final Date date );

	void saveAnonymousDayAjax( final String date );

	void deleteAnonymousDayAjax( final String date );
}
