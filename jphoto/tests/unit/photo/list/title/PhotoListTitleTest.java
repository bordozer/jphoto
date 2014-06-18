package photo.list.title;

import common.AbstractTestCase;
import mocks.GenreMock;
import core.general.data.PhotoListCriterias;
import core.services.system.ServicesImpl;
import core.services.translator.Language;
import core.services.utils.DateUtilsServiceImpl;
import mocks.UserMock;
import org.junit.Before;
import org.junit.Test;
import ui.controllers.photos.list.title.AbstractPhotoListTitle;
import ui.controllers.photos.list.title.PhotoListTitle;

import static junit.framework.Assert.assertEquals;

public class PhotoListTitleTest extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void noCriteriasTest() {

		final PhotoListCriterias criterias = new PhotoListCriterias();

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, Language.EN, getServices() );

		assertEquals( PhotoGalleryTitleTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title", galleryTitle.getPhotoListTitle() );
		assertEquals( PhotoGalleryTitleTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description", galleryTitle.getPhotoListDescription() );
	}

	@Test
	public void userCriteriasTest() {

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setUser( new UserMock( 111 ) );

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, Language.EN, getServices() );

		assertEquals( PhotoGalleryTitleTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/111/card/\" title=\"EntityLinkUtilsService: Mock user #111: user card link title\">Mock user #111</a>", galleryTitle.getPhotoListTitle() );
		assertEquals( PhotoGalleryTitleTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/111/card/\" title=\"EntityLinkUtilsService: Mock user #111: user card link title\">Mock user #111</a>", galleryTitle.getPhotoListDescription() );
	}

	@Test
	public void genreCriteriasTest() {

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setGenre( new GenreMock( 333 ) );

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, Language.EN, getServices() );

		assertEquals( PhotoGalleryTitleTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title Top best photo list title: in category Translated entry", galleryTitle.getPhotoListTitle() );
		assertEquals( PhotoGalleryTitleTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description Top best photo list title: in category Translated entry", galleryTitle.getPhotoListDescription() );
	}

	@Override
	protected ServicesImpl getServices() {
		final ServicesImpl services = super.getServices();

		services.setDateUtilsService( new DateUtilsServiceImpl() );
		services.setEntityLinkUtilsService( entityLinkUtilsService );

		return services;
	}
}
