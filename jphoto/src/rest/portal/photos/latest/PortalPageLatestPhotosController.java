package rest.portal.photos.latest;

import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.services.photo.PhotoService;
import core.services.system.ConfigurationService;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import core.services.utils.sql.PhotoListQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sql.builder.SqlIdsSelectQuery;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "portal-page" )
public class PortalPageLatestPhotosController {

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

	@RequestMapping( method = RequestMethod.GET, value = "/photos/latest/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public LatestPhotosDTO theLatestPhotos() {

		final List<LatestPhotoDTO> dtos = newArrayList();

		final List<Integer> photos = getLatestUploadedPhotos();

		for ( final int photoId : photos ) {

			final Photo photo = photoService.load( photoId );

			final LatestPhotoDTO photoDTO = new LatestPhotoDTO();
			photoDTO.setPhotoId( photo.getId() );
			photoDTO.setPhotoName( photo.getName() ); // TODO: escape!
			photoDTO.setPhotoImageUrl( userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );
			photoDTO.setPhotoCardUrl( urlUtilsService.getPhotoCardLink( photoId ) );

			dtos.add( photoDTO );
		}

		return new LatestPhotosDTO( 1, dtos );
	}

	private List<Integer> getLatestUploadedPhotos() {
		final SqlIdsSelectQuery query = new PhotoListQueryBuilder( dateUtilsService )
			.forPage( 1, configurationService.getInt( ConfigurationKey.SYSTEM_UI_PORTAL_PAGE_LATEST_PHOTOS_COUNT ) )
			.sortByUploadTimeDesc()
			.getQuery();

		return photoService.load( query ).getIds();
	}
}
