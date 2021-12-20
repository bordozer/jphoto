package com.bordozer.jphoto.rest.portal.photos.best;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.img.Dimension;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.photo.PhotoVotingService;
import com.bordozer.jphoto.core.services.security.RestrictionService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.ImageFileUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsService;
import com.bordozer.jphoto.core.services.utils.sql.PhotoListQueryBuilder;
import com.bordozer.jphoto.rest.portal.photos.LatestPhotoDTO;
import com.bordozer.jphoto.rest.portal.photos.LatestPhotosDTO;
import com.bordozer.jphoto.sql.SqlSelectIdsResult;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/portal-page")
public class PortalPageBestPhotosController {

    private static final Dimension PORTAL_PAGE_BEST_PHOTO_DIMENSION = new Dimension(500, 300);

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

    @RequestMapping(method = RequestMethod.GET, value = "/photos/best/", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public LatestPhotosDTO theBestPhotos() throws IOException {

        final List<LatestPhotoDTO> dtos = newArrayList();

        final List<Integer> photos = getBestPhotos();

        int index = 0;
        for (final int photoId : photos) {

            final Photo photo = photoService.load(photoId);

            final LatestPhotoDTO photoDTO = new LatestPhotoDTO();
            photoDTO.setPhotoId(photo.getId());
            photoDTO.setPhotoName(photo.getName()); // TODO: escape!
            photoDTO.setPhotoImageUrl(userPhotoFilePathUtilsService.getPhotoImageUrl(photo));
            photoDTO.setPhotoCardUrl(urlUtilsService.getPhotoCardLink(photoId));

            final Dimension dimension = imageFileUtilsService.resizeImageToDimensionAndReturnResultDimension(photo.getPhotoImageFile(), PORTAL_PAGE_BEST_PHOTO_DIMENSION);
            photoDTO.setDimension(dimension);

            photoDTO.setIndex(index);

            dtos.add(photoDTO);

            index++;
        }

        return new LatestPhotosDTO(1, dtos);
    }

    private List<Integer> getBestPhotos() {
        final SqlIdsSelectQuery query = new PhotoListQueryBuilder(dateUtilsService)
                .filterByMinimalMarks(configurationService.getInt(ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_PHOTO_OF_THE_DAY))
                .filterByVotingTime(photoVotingService.getPortalPageBestDateRange())
                .forPage(1, configurationService.getInt(ConfigurationKey.PHOTO_RATING_PORTAL_PAGE_BEST_PHOTOS_FROM_PHOTOS_THAT_GOT_ENOUGH_MARKS_FOR_N_LAST_DAYS))
                .sortBySumMarksDesc()
                .getQuery();

        final SqlSelectIdsResult sqlSelectIdsResult = photoService.load(query);

        final Date currentTime = dateUtilsService.getCurrentTime();
        final List<Integer> ids = sqlSelectIdsResult.getIds();
        CollectionUtils.filter(ids, new Predicate<Integer>() {
            @Override
            public boolean evaluate(final Integer photoId) {
                return !restrictionService.isPhotoOfTheDayRestrictedOn(photoId, currentTime);
            }
        });

        Collections.shuffle(ids);

        return ids;
    }
}
