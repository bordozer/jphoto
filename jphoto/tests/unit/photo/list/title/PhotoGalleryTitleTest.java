package photo.list.title;

import common.AbstractTestCase;
import core.general.data.PhotoListCriterias;
import core.services.translator.Language;
import org.junit.Before;
import org.junit.Test;
import ui.controllers.photos.list.title.AbstractPhotoListTitle;
import ui.controllers.photos.list.title.PhotoGalleryTitle;

import static junit.framework.Assert.assertEquals;

public class PhotoGalleryTitleTest extends AbstractTestCase {

	static final Language LANGUAGE = Language.EN;

	static final String EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL = "expected and actual titles are not equal";
	static final String EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL = "expected and actual descriptions are not equal";

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void galleryTest() {

		final AbstractPhotoListTitle galleryTitle = new PhotoGalleryTitle( new PhotoListCriterias(), getServices() );

		assertEquals( EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo gallery root", galleryTitle.getPhotoListTitle().build( LANGUAGE ) );
		assertEquals( EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo gallery title: descriptions", galleryTitle.getPhotoListDescription().build( LANGUAGE ) );
	}
}
