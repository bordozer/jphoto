package photo.list.factory;

import common.AbstractTestCase;
import core.general.data.PhotoListCriterias;
import core.general.data.PhotoSort;
import core.general.photo.group.PhotoGroupOperationMenu;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.services.entry.GroupOperationService;
import core.services.photo.PhotoListCriteriasService;
import core.services.photo.PhotoService;
import core.services.security.RestrictionService;
import core.services.system.ServicesImpl;
import org.easymock.EasyMock;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;

import java.util.Collections;

public class AbstractPhotoListFactoryTest_ extends AbstractTestCase {

	protected ServicesImpl getTestServices( final TestData testData ) {
		final ServicesImpl services = getServices();

		services.setPhotoListCriteriasService( getPhotoListCriteriasService( testData ) );
		services.setPhotoCriteriasSqlService( photoCriteriasSqlService );
		services.setPhotoService( getPhotoService( testData ) );
		services.setRestrictionService( getRestrictionService( testData ) );
		services.setUrlUtilsService( urlUtilsService );
		services.setGroupOperationService( getGroupOperationService() );

		return services;
	}

	protected GroupOperationService getGroupOperationService() {
		final GroupOperationService groupOperationService = EasyMock.createMock( GroupOperationService.class );

		EasyMock.expect( groupOperationService.getNoPhotoGroupOperationMenuContainer() ).andReturn( new PhotoGroupOperationMenuContainer( Collections.<PhotoGroupOperationMenu>emptyList() ) ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( groupOperationService );

		return groupOperationService;
	}

	protected RestrictionService getRestrictionService( final TestData testData ) {
		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );

		for ( final int photosId : testData.photoIds ) {
			EasyMock.expect( restrictionService.isPhotoShowingInTopBestRestrictedOn( photosId, testData.currentTime ) ).andReturn( false ).anyTimes();
		}

		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		return restrictionService;
	}

	protected PhotoService getPhotoService( final TestData testData ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		final SqlSelectIdsResult idsResult = new SqlSelectIdsResult();
		idsResult.setIds( testData.photoIds );
		idsResult.setRecordQty( 4 );

		EasyMock.expect( photoService.load( EasyMock.<SqlIdsSelectQuery>anyObject() ) ).andReturn( idsResult ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}

	protected PhotoListCriteriasService getPhotoListCriteriasService( final TestData testData ) {
		final PhotoListCriteriasService photoListCriteriasService = EasyMock.createMock( PhotoListCriteriasService.class );

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setPhotoSort( PhotoSort.UPLOAD_TIME );
		criterias.setVotingTimeFrom( dateUtilsService.getDatesOffset( testData.currentTime, - 2 ) );
		criterias.setVotingTimeTo( testData.currentTime );

		if ( testData.user != null ) {
			criterias.setUser( testData.user );
		}

		if ( testData.genre != null ) {
			criterias.setGenre( testData.genre );
		}

		EasyMock.expect( photoListCriteriasService.getForAllPhotosTopBest( testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForGenreTopBest( testData.genre, testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForUserTopBest( testData.user, testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForUserAndGenreTopBest( testData.user, testData.genre, testData.accessor ) ).andReturn( criterias ).anyTimes();

		EasyMock.expect( photoListCriteriasService.getLinkToFullListText( criterias ) ).andReturn( "Link To Full List Text" ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoListCriteriasService );

		return photoListCriteriasService;
	}
}
