package photo.list.factory;

import core.enums.RestrictionType;
import core.general.base.PagingModel;
import core.services.security.RestrictionService;
import core.services.system.ServicesImpl;
import core.services.translator.Language;
import javafx.util.Pair;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import ui.controllers.photos.list.factory.AbstractPhotoListFactory;
import ui.controllers.photos.list.factory.PhotoListFactoryTopBest;
import ui.elements.PhotoList;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;

public class PhotoListFactoryTopBestRestrictionsTest extends AbstractPhotoListFactoryTest_ {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void photoIsNotShownIfItHasSuitableRestrictionTest() {

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-10 10:11:22" );
		testData.photoIds = newArrayList( 2000, 2001, 2002, 2003, 2004, 2005 );
		testData.restrictedPhotos = newArrayList();
		testData.restrictedPhotos.add( new Pair<>( 2001, RestrictionType.PHOTO_TO_BE_BEST_IN_GENRE ) );
		testData.restrictedPhotos.add( new Pair<>( 2003, RestrictionType.PHOTO_TO_BE_BEST_IN_GENRE ) );
		testData.restrictedPhotos.add( new Pair<>( 2005, RestrictionType.PHOTO_APPRAISAL ) );

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryTopBest( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "2000,2002,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Test
	public void photoIsNotShownIfItHasSuitableRestriction_ForAdminToo_Test() {

		final TestData testData = new TestData( SUPER_ADMIN_1 );
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-10 10:11:22" );
		testData.photoIds = newArrayList( 2000, 2001, 2002, 2003, 2004, 2005 );
		testData.restrictedPhotos = newArrayList();
		testData.restrictedPhotos.add( new Pair<>( 2001, RestrictionType.PHOTO_TO_BE_BEST_IN_GENRE ) );
		testData.restrictedPhotos.add( new Pair<>( 2003, RestrictionType.PHOTO_TO_BE_BEST_IN_GENRE ) );
		testData.restrictedPhotos.add( new Pair<>( 2005, RestrictionType.PHOTO_APPRAISAL ) );

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryTopBest( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "2000,2002,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Override
	protected RestrictionService getRestrictionService( final TestData testData ) {
		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );

		for ( final int photosId : testData.photoIds ) {
			EasyMock.expect( restrictionService.isPhotoShowingInTopBestRestrictedOn( photosId, testData.currentTime ) ).andReturn( isRestricted( photosId, testData.restrictedPhotos ) ).anyTimes();
		}

		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		return restrictionService;
	}

	private Boolean isRestricted( final int photosId, final List<Pair<Integer, RestrictionType>> restrictedPhotos ) {
		for ( final Pair<Integer, RestrictionType> restrictedPhoto : restrictedPhotos ) {
			if ( restrictedPhoto.getKey() == photosId && restrictedPhoto.getValue() == RestrictionType.PHOTO_TO_BE_BEST_IN_GENRE ) {
				return true;
			}
		}
		return false;
	}
}
