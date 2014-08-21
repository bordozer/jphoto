package photo.list.factory;

import common.AbstractTestCase;
import core.enums.RestrictionType;
import core.general.data.PhotoListCriterias;
import core.general.data.PhotoSort;
import core.general.photo.Photo;
import core.general.photo.group.PhotoGroupOperationMenu;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.services.entry.GroupOperationService;
import core.services.photo.PhotoListCriteriasService;
import core.services.photo.PhotoListFilteringService;
import core.services.photo.PhotoService;
import core.services.security.RestrictionService;
import core.services.system.ServicesImpl;
import javafx.util.Pair;
import org.easymock.EasyMock;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import ui.services.photo.listFactory.factory.AbstractPhotoFilteringStrategy;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AbstractPhotoListFactoryTest_ extends AbstractTestCase {

	protected ServicesImpl getTestServices( final TestData testData ) {
		final ServicesImpl services = getServices();

		services.setPhotoListCriteriasService( getPhotoListCriteriasService( testData ) );
		services.setPhotoCriteriasSqlService( photoCriteriasSqlService );
		services.setPhotoService( getPhotoService( testData ) );
		services.setRestrictionService( getRestrictionService( testData ) );
		services.setUrlUtilsService( urlUtilsService );
		services.setGroupOperationService( getGroupOperationService( testData ) );
		services.setPhotoListFilteringService( getPhotoListFilteringService( testData ) );

		return services;
	}

	protected GroupOperationService getGroupOperationService( final TestData testData ) {
		final GroupOperationService groupOperationService = EasyMock.createMock( GroupOperationService.class );

		EasyMock.expect( groupOperationService.getNoPhotoGroupOperationMenuContainer() ).andReturn( new PhotoGroupOperationMenuContainer( Collections.<PhotoGroupOperationMenu>emptyList() ) ).anyTimes();
		EasyMock.expect( groupOperationService.getPhotoListPhotoGroupOperationMenuContainer( testData.accessor ) ).andReturn( new PhotoGroupOperationMenuContainer( Collections.<PhotoGroupOperationMenu>emptyList() ) ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( groupOperationService );

		return groupOperationService;
	}

	protected RestrictionService getRestrictionService( final TestData testData ) {
		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );

		for ( final int photosId : testData.getPhotoIds() ) {
			EasyMock.expect( restrictionService.isPhotoShowingInTopBestRestrictedOn( photosId, testData.currentTime ) ).andReturn( false ).anyTimes();
			EasyMock.expect( restrictionService.isPhotoShowingInPhotoGalleryRestrictedOn( photosId, testData.currentTime ) ).andReturn( false ).anyTimes();
		}

		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		return restrictionService;
	}

	protected RestrictionService getRestrictionService( final TestData testData, final RestrictionType restrictionType ) {
		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );

		for ( final int photosId : testData.getPhotoIds() ) {
			EasyMock.expect( restrictionService.isPhotoShowingInTopBestRestrictedOn( photosId, testData.currentTime ) ).andReturn( isRestricted( photosId, testData.restrictedPhotos, restrictionType ) ).anyTimes();
			EasyMock.expect( restrictionService.isPhotoShowingInPhotoGalleryRestrictedOn( photosId, testData.currentTime ) ).andReturn( isRestricted( photosId, testData.restrictedPhotos, restrictionType ) ).anyTimes();
		}

		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		return restrictionService;
	}

	protected PhotoService getPhotoService( final TestData testData ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		final SqlSelectIdsResult idsResult = new SqlSelectIdsResult();
		idsResult.setIds( testData.getPhotoIds() );
		idsResult.setRecordQty( 4 );

		EasyMock.expect( photoService.load( EasyMock.<SqlIdsSelectQuery>anyObject() ) ).andReturn( idsResult ).anyTimes();
		for ( final Photo photo : testData.photos ) {
			EasyMock.expect( photoService.load( photo.getId() ) ).andReturn( photo ).anyTimes();
		}

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}

	protected PhotoListCriteriasService getPhotoListCriteriasService( final TestData testData ) {
		final PhotoListCriteriasService photoListCriteriasService = EasyMock.createMock( PhotoListCriteriasService.class );

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setPhotoSort( PhotoSort.UPLOAD_TIME );

		if ( testData.votingTimeFrom != null ) {
			criterias.setVotingTimeFrom( testData.votingTimeFrom );
		}
		if ( testData.votingTimeTo != null ) {
			criterias.setVotingTimeTo( testData.votingTimeTo );
		}

		if ( testData.user != null ) {
			criterias.setUser( testData.user );
		}

		if ( testData.genre != null ) {
			criterias.setGenre( testData.genre );
		}

		EasyMock.expect( photoListCriteriasService.getForPhotoGalleryTopBest( testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForGenreTopBest( testData.genre, testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForUserTopBest( testData.user, testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForUserAndGenreTopBest( testData.user, testData.genre, testData.accessor ) ).andReturn( criterias ).anyTimes();

		EasyMock.expect( photoListCriteriasService.getForAllPhotos( testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForUser( testData.user, testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForGenre( testData.genre, testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForUserAndGenre( testData.user, testData.genre, testData.accessor ) ).andReturn( criterias ).anyTimes();

		EasyMock.expect( photoListCriteriasService.getForAbsolutelyBest( testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForUserAbsolutelyBest( testData.user, testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForGenreBestForPeriod( testData.genre, testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForUserAndGenreAbsolutelyBest( testData.user, testData.genre, testData.accessor ) ).andReturn( criterias ).anyTimes();

		EasyMock.expect( photoListCriteriasService.getLinkToFullListText() ).andReturn( "Link To Full List Text" ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoListCriteriasService );

		return photoListCriteriasService;
	}

	private Boolean isRestricted( final int photosId, final List<Pair<Integer, RestrictionType>> restrictedPhotos, RestrictionType restrictionType ) {
		for ( final Pair<Integer, RestrictionType> restrictedPhoto : restrictedPhotos ) {
			if ( restrictedPhoto.getKey() == photosId && restrictedPhoto.getValue() == restrictionType ) {
				return true;
			}
		}
		return false;
	}

	private PhotoListFilteringService getPhotoListFilteringService( final TestData testData ) {
		final PhotoListFilteringService photoListFilteringService = EasyMock.createMock( PhotoListFilteringService.class );

		final AbstractPhotoFilteringStrategy filteringStrategy = new AbstractPhotoFilteringStrategy() {
			@Override
			public boolean isPhotoHidden( final int photoId, final Date time ) {
				return testData.isPhotoHidden;
			}
		};

		EasyMock.expect( photoListFilteringService.galleryFilteringStrategy( testData.accessor ) ).andReturn( filteringStrategy ).anyTimes();
		EasyMock.expect( photoListFilteringService.topBestFilteringStrategy() ).andReturn( filteringStrategy ).anyTimes();
		EasyMock.expect( photoListFilteringService.bestFilteringStrategy( testData.accessor ) ).andReturn( filteringStrategy ).anyTimes();
		EasyMock.expect( photoListFilteringService.userCardFilteringStrategy( testData.user, testData.accessor ) ).andReturn( filteringStrategy ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoListFilteringService );

		return photoListFilteringService;
	}
}
