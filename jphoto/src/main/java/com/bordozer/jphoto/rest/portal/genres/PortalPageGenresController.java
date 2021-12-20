package com.bordozer.jphoto.rest.portal.genres;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/portal-page/genres")
public class PortalPageGenresController {

    @Autowired
    private GenreService genreService;

    @Autowired
    private EntityLinkUtilsService entityLinkUtilsService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @RequestMapping(method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public GenresModel genres() {
        final GenresModel model = new GenresModel();

        final Date todayFirstSecond = dateUtilsService.getFirstSecondOfToday();
        final Date todayLastSecond = dateUtilsService.getLastSecondOfToday();

        final List<GenreDTO> dtos = newArrayList();

        final List<Genre> genres = genreService.loadAll();
        for (final Genre genre : genres) {
            final GenreDTO genreDTO = new GenreDTO();
            genreDTO.setGenreId(genre.getId());
            genreDTO.setGenrePhotosLink(entityLinkUtilsService.getPhotosByGenreLink(genre, EnvironmentContext.getLanguage()));
            genreDTO.setTodayPhotos(photoService.getPhotosCountByGenreForPeriod(genre, todayFirstSecond, todayLastSecond));
            genreDTO.setTotalPhotos(photoService.getPhotosCountByGenre(genre.getId()));

            dtos.add(genreDTO);
        }
        model.setGenreDTOs(dtos);

        model.setTotalPhotos(photoService.getPhotosCount());

        return model;
    }
}
