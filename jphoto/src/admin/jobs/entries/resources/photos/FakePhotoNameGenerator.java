package admin.jobs.entries.resources.photos;

import admin.jobs.entries.resources.FakePhotoCommentLoader;
import core.log.LogHelper;
import core.services.utils.RandomUtilsService;
import core.services.utils.RandomUtilsServiceImpl;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FakePhotoNameGenerator {

	private static final String SUBJECTS_XML = "admin/jobs/entries/resources/photos/fake-photo-subjects.xml";
	private static final String PREPOSITIONS_XML = "admin/jobs/entries/resources/photos/fake-photo-prepositions.xml";
	private static final String OTHER_XML = "admin/jobs/entries/resources/photos/fake-photo-details.xml";

	private static final String ITEM_TAG = "item";
	private static final String CASE_TAG = "case";
	private static final String VALUE_TAG = "value";

	private static final List<String> subjects = newArrayList();
	private static final List<CaseSensitiveData> prepositions = newArrayList();
	private static final List<CaseSensitiveData> details = newArrayList();

	public static void main( String[] args ) throws UnsupportedEncodingException {
		final String name = getFakePhotoName( new RandomUtilsServiceImpl() );

		final byte bytes[] = name.getBytes( "UTF-8" );
		final String value = new String( bytes, "UTF-8" );

		System.out.println( value );
	}

	public static String getFakePhotoName( final RandomUtilsService randomUtilsService ) {

		final List<String> subjects = getSubjects();
		final List<CaseSensitiveData> prepositions = getPrepositions();
		final List<CaseSensitiveData> details = getDetails();

		final StringBuilder result = new StringBuilder( randomUtilsService.getRandomGenericListElement( subjects ) );

		if ( randomUtilsService.getRandomInt( 0, 10 ) > 3 ) {
			result.append( " " );
			result.append( getRandomCombination( prepositions, details, randomUtilsService ) );
		}

		if ( randomUtilsService.getRandomInt( 0, 10 ) > 7 ) {
			result.append( " " );
			result.append( getRandomCombination( prepositions, details, randomUtilsService ) );
		}

		return result.toString();
	}

	private static String getRandomCombination( final List<CaseSensitiveData> prepositions, final List<CaseSensitiveData> details, final RandomUtilsService randomUtilsService ) {

		final CaseSensitiveData randomCase = randomUtilsService.getRandomGenericListElement( prepositions );

		if ( randomCase.getValues().size() == 0 ) { // ???????????? ?????
			return randomUtilsService.getRandomGenericListElement( details.get( 0 ).getValues() );
		}

		final String randomPreposition = randomUtilsService.getRandomGenericListElement( randomCase.getValues() );

		for ( final CaseSensitiveData detail : details ) {
			if ( detail.getCaseName().equals( randomCase.getCaseName() ) ) {
				final String randomOther = randomUtilsService.getRandomGenericListElement( detail.getValues() );
				return String.format( "%s %s", randomPreposition, randomOther );
			}
		}

		throw new IllegalStateException( String.format( "FakePhotoNameGenerator: can not generate random photo name" ) );
	}

	private static List<String> getSubjects() {

		if ( subjects.size() > 0 ) {
			return subjects;
		}

		synchronized ( subjects ) {

			if ( subjects.size() > 0 ) {
				return subjects;
			}

			return loadSubjects();
		}
	}

	private static List<CaseSensitiveData> getPrepositions() {
		if ( prepositions.size() > 0 ) {
			return prepositions;
		}

		synchronized ( prepositions ) {

			if ( prepositions.size() > 0 ) {
				return prepositions;
			}

			return loadCaseSensitiveData( PREPOSITIONS_XML, prepositions );
		}
	}

	private static List<CaseSensitiveData> getDetails() {
		if ( details.size() > 0 ) {
			return details;
		}

		synchronized ( details ) {

			if ( details.size() > 0 ) {
				return details;
			}

			return loadCaseSensitiveData( OTHER_XML, details );
		}
	}

	private static List<String> loadSubjects() {
		final File translationsFile = new File( SUBJECTS_XML );

		final SAXReader reader = new SAXReader( false );
		final Document document;
		try {
			document = reader.read( translationsFile );
		} catch ( DocumentException e ) {
			final LogHelper log = new LogHelper( FakePhotoCommentLoader.class );
			log.error( String.format( "Can not read file '%s'", SUBJECTS_XML ), e );

			return newArrayList();
		}

		final Iterator photosIterator = document.getRootElement().elementIterator( ITEM_TAG );

		while ( photosIterator.hasNext() ) {
			subjects.add( ( ( Element ) photosIterator.next() ).getText().trim() );
		}

		return subjects;
	}

	private static List<CaseSensitiveData> loadCaseSensitiveData( final String fileName, final List<CaseSensitiveData> list ) {
		final File translationsFile = new File( fileName );

		final SAXReader reader = new SAXReader( false );
		final Document document;
		try {
			document = reader.read( translationsFile );
		} catch ( DocumentException e ) {
			final LogHelper log = new LogHelper( FakePhotoCommentLoader.class );
			log.error( String.format( "Can not read file '%s'", fileName ), e );

			return newArrayList();
		}

		final Iterator itemsIterator = document.getRootElement().elementIterator( ITEM_TAG );

		while ( itemsIterator.hasNext() ) {
			final Element itemElement = ( Element ) itemsIterator.next();

			final String caseName = itemElement.element( CASE_TAG ).getText();

			final List<String> values = newArrayList();
			final Iterator valuesIterator = itemElement.elementIterator( VALUE_TAG );
			while ( valuesIterator.hasNext() ) {
				final Element valueElement = ( Element ) valuesIterator.next();
				values.add( valueElement.getText() );
			}

			final CaseSensitiveData data = new CaseSensitiveData( caseName, values );

			list.add( data );
		}

		return list;
	}
}
