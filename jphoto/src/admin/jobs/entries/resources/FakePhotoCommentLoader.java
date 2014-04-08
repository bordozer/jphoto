package admin.jobs.entries.resources;

import core.log.LogHelper;
import core.services.system.Services;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FakePhotoCommentLoader {

	private static final String PHOTO_COMMENTS_XML = "admin/jobs/entries/resources/photo-comments.xml";
	private static final String COMMENT_TAG = "comment";

	private static final List<String> comments = newArrayList();

	public static String getRandomFakeComment( final Services services ) {
		return services.getRandomUtilsService().getRandomGenericListElement( getFakeComments() );
	}

	private static List<String> getFakeComments() {

		if ( comments.size() > 0 ) {
			return comments;
		}

		synchronized ( comments ) {

			if ( comments.size() > 0 ) {
				return comments;
			}

			return loadFakeComments();
		}
	}

	private static List<String> loadFakeComments() {
		final File translationsFile = new File( PHOTO_COMMENTS_XML );

		final SAXReader reader = new SAXReader( false );
		final Document document;
		try {
			document = reader.read( translationsFile );
		} catch ( DocumentException e ) {
			final LogHelper log = new LogHelper( FakePhotoCommentLoader.class );
			log.error( String.format( "Can not read file of fake comments '%s'", PHOTO_COMMENTS_XML ), e );
			return newArrayList();
		}

		final Iterator photosIterator = document.getRootElement().elementIterator( COMMENT_TAG );

		while ( photosIterator.hasNext() ) {
			final Element commentElement = ( Element ) photosIterator.next();
			final String comment = commentElement.getText();

			comments.add( comment );
		}

		return comments;
	}
}
