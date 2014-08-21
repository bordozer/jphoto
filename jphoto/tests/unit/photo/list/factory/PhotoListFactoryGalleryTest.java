package photo.list.factory;

import core.general.base.PagingModel;
import core.general.genre.Genre;
import core.general.user.User;
import core.services.system.ServicesImpl;
import core.services.translator.Language;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import ui.controllers.photos.list.factory.AbstractPhotoListFactory;
import ui.controllers.photos.list.factory.PhotoListFactoryGallery;
import ui.elements.PhotoList;

import static junit.framework.Assert.assertEquals;

public class PhotoListFactoryGalleryTest extends AbstractPhotoListFactoryTest_ {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void galleryTest() {

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory factory = new PhotoListFactoryGallery( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = factory.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Photo gallery root", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", null, photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Photo gallery title: descriptions", photoList.getPhotosCriteriasDescription() );
		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Test
	public void galleryForUserTest() {

		final User user = new User();
		user.setId( 112 );
		user.setName( "Just a user" );

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.user = user;

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory factory = new PhotoListFactoryGallery( testData.user, testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = factory.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Photo list title Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a>", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", null, photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Photo list description Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a>", photoList.getPhotosCriteriasDescription() );
		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Test
	public void galleryForGenreTest() {

		final Genre genre = new Genre();
		genre.setId( 56 );
		genre.setName( "Portrait" );

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.genre = genre;

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory factory = new PhotoListFactoryGallery( testData.genre, testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = factory.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Photo list title Top best photo list title: in category Translated entry", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", null, photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Photo list description Top best photo list title: in category Translated entry", photoList.getPhotosCriteriasDescription() );
		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Test
	public void galleryBestForUserAndGenresTest() {

		final User user = new User();
		user.setId( 112 );
		user.setName( "Just a user" );

		final Genre genre = new Genre();
		genre.setId( 222 );
		genre.setName( "Glamour" );

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.user = user;
		testData.genre = genre;

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory factory = new PhotoListFactoryGallery( testData.user, testData.genre, testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = factory.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Photo list title Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a> Top best photo list title: in category Translated entry", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", null, photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Photo list description Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a> Top best photo list title: in category Translated entry", photoList.getPhotosCriteriasDescription() );
		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Test
	public void photoIsHiddenTest() {

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.isPhotoHidden = true;

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory factory = new PhotoListFactoryGallery( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = factory.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	/*@Test
	public void photoIsNotShownIfItHasSuitableRestrictionTest() {

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-10 10:11:22" );
		testData.restrictedPhotos = newArrayList();
		testData.restrictedPhotos.add( new Pair<>( 2000, RestrictionType.PHOTO_SHOWING_IN_TOP_OF_GENRE ) );
		testData.restrictedPhotos.add( new Pair<>( 2005, RestrictionType.PHOTO_SHOWING_IN_PHOTO_GALLERY ) );
		testData.restrictedPhotos.add( new Pair<>( 2005, RestrictionType.PHOTO_APPRAISAL ) );

		final ServicesImpl services = getTestServices( testData );
		services.setRestrictionService( getRestrictionService( testData, RestrictionType.PHOTO_SHOWING_IN_PHOTO_GALLERY ) );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryGallery( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}*/

	/*@Test
	public void photoIsNotShownIfItHasSuitableRestriction_ButAdminTest() {

		final TestData testData = new TestData( SUPER_ADMIN_1 );
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-10 10:11:22" );
		testData.restrictedPhotos = newArrayList();
		testData.restrictedPhotos.add( new Pair<>( 2000, RestrictionType.PHOTO_SHOWING_IN_TOP_OF_GENRE ) );
		testData.restrictedPhotos.add( new Pair<>( 2005, RestrictionType.PHOTO_SHOWING_IN_PHOTO_GALLERY ) );
		testData.restrictedPhotos.add( new Pair<>( 2005, RestrictionType.PHOTO_APPRAISAL ) );

		final ServicesImpl services = getTestServices( testData );
		services.setRestrictionService( getRestrictionService( testData, RestrictionType.PHOTO_SHOWING_IN_PHOTO_GALLERY ) );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryGallery( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}*/
}
