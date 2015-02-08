package rest.portal.photos.best;

import core.general.configuration.ConfigurationKey;
import core.general.img.Dimension;
import core.general.photo.Photo;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.security.RestrictionService;
import core.services.system.ConfigurationService;
import core.services.utils.DateUtilsService;
import core.services.utils.ImageFileUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import core.services.utils.sql.PhotoListQueryBuilder;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import rest.portal.photos.LatestPhotoDTO;
import rest.portal.photos.LatestPhotosDTO;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "portal-page" )
public class PortalPageBestPhotosController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private RestrictionService restrictionService;

	@Autowired
	private PhotoVotingService photoVotingService;

	@Autowired
	private ImageFileUtilsService imageFileUtilsService;

	@RequestMapping( method = RequestMethod.GET, value = "/photos/best/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public LatestPhotosDTO theBestPhotos() throws IOException {

		final List<LatestPhotoDTO> dtos = newArrayList();

		final List<Integer> photos = getBestPhotos();

		int index = 0;
		for ( final int photoId : photos ) {

			final Photo photo = photoService.load( photoId );

			final LatestPhotoDTO photoDTO = new LatestPhotoDTO();
			photoDTO.setPhotoId( photo.getId() );
			photoDTO.setPhotoName( photo.getName() ); // TODO: escape!
			photoDTO.setPhotoImageUrl( userPhotoFilePathUtilsService.getPhotoImageUrl( photo ) );
			photoDTO.setPhotoCardUrl( urlUtilsService.getPhotoCardLink( photoId ) );

			final Dimension dimension = imageFileUtilsService.resizeImageToDimensionAndReturnResultDimension( photo.getPhotoImageFile(), new Dimension( 300, 300 ) );
			photoDTO.setDimension( dimension );

			photoDTO.setIndex( index );

			dtos.add( photoDTO );

			index++;
		}

		return new LatestPhotosDTO( 1, dtos );
	}

	private List<Integer> getBestPhotos() {
		final SqlIdsSelectQuery query = new PhotoListQueryBuilder( dateUtilsService )
			.filterByMinimalMarks( configurationService.getInt( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_PHOTO_OF_THE_DAY ) )
			.filterByVotingTime( photoVotingService.getPortalPageBestDateRange() )
			.forPage( 1, configurationService.getInt( ConfigurationKey.PHOTO_RATING_PORTAL_PAGE_BEST_PHOTOS_FROM_PHOTOS_THAT_GOT_ENOUGH_MARKS_FOR_N_LAST_DAYS ) )
			.sortBySumMarksDesc()
			.getQuery();

		final SqlSelectIdsResult sqlSelectIdsResult = photoService.load( query );

		final Date currentTime = dateUtilsService.getCurrentTime();
		final List<Integer> ids = sqlSelectIdsResult.getIds();
		CollectionUtils.filter( ids, new Predicate<Integer>() {
			@Override
			public boolean evaluate( final Integer photoId ) {
				return !restrictionService.isPhotoOfTheDayRestrictedOn( photoId, currentTime );
			}
		} );

		return ids;
	}
}
