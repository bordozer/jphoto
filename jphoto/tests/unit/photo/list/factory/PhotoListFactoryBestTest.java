package photo.list.factory;

import org.junit.Before;

public class PhotoListFactoryBestTest extends AbstractPhotoListFactoryTest_ {

	@Before
	public void setup() {
		super.setup();
	}

	/*@Test
	public void galleryTest() {

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory factory = new PhotoListFactoryBest( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = factory.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Best photo list title", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", null, photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Best photo list description", photoList.getPhotosCriteriasDescription() );
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

		final AbstractPhotoListFactory factory = new PhotoListFactoryBest( testData.user, testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = factory.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Best photo list title Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a>", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", null, photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Best photo list description Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a>", photoList.getPhotosCriteriasDescription() );
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

		final AbstractPhotoListFactory factory = new PhotoListFactoryBest( testData.genre, testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = factory.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Best photo list title Top best photo list title: in category Translated entry", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", null, photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Best photo list description Top best photo list title: in category Translated entry", photoList.getPhotosCriteriasDescription() );
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

		final AbstractPhotoListFactory factory = new PhotoListFactoryBest( testData.user, testData.genre, testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = factory.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Best photo list title Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a> Top best photo list title: in category Translated entry", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", null, photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Best photo list description Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a> Top best photo list title: in category Translated entry", photoList.getPhotosCriteriasDescription() );
		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Test
	public void isPhotoHiddenTest() {

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.isPhotoHidden = true;

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory factory = new PhotoListFactoryBest( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = factory.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}*/
}
