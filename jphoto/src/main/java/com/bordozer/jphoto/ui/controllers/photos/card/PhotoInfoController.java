package com.bordozer.jphoto.ui.controllers.photos.card;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.PhotoUIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("photos/{photoId}")
public class PhotoInfoController {

    private static final String PHOTO_INFO_VIEW = "photos/PhotoInfo";

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoUIService photoUIService;

    @Autowired
    private UserRankService userRankService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @RequestMapping(method = RequestMethod.GET, value = "/info/")
    public String getPhotoInfo(final @PathVariable("photoId") int photoId, final @ModelAttribute("photoInfoModel") PhotoInfoModel model) {
        final Photo photo = photoService.load(photoId);
        model.setPhotoInfo(photoUIService.getPhotoInfo(photo, EnvironmentContext.getCurrentUser()));
        model.setVotingModel(userRankService.getVotingModel(photo.getUserId(), photo.getGenreId(), EnvironmentContext.getCurrentUser(), dateUtilsService.getCurrentTime()));

        return PHOTO_INFO_VIEW;
    }
}
