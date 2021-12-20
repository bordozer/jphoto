package com.bordozer.jphoto.ui.controllers.photos.card;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoImageLocationType;
import com.bordozer.jphoto.core.general.photo.PhotoInfo;
import com.bordozer.jphoto.core.general.photo.PhotoPreview;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoCommentArchService;
import com.bordozer.jphoto.core.services.photo.PhotoCommentService;
import com.bordozer.jphoto.core.services.photo.PhotoPreviewService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.photo.PhotoVotingService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.ImageFileUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.PhotoUIService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsPhotoService;
import com.bordozer.jphoto.ui.services.menu.entry.EntryMenuService;
import com.bordozer.jphoto.ui.services.security.SecurityUIService;
import com.bordozer.jphoto.utils.NumberUtils;
import com.bordozer.jphoto.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping(UrlUtilsServiceImpl.PHOTOS_URL)
public class PhotoCardController {

    private static final String PHOTO_CARD_MODEL = "photoCardModel";

    private static final String VIEW = "photos/card/PhotoCard";

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoUIService photoUIService;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private PhotoCommentService photoCommentService;

    @Autowired
    private PhotoCommentArchService photoCommentArchService;

    @Autowired
    private PhotoVotingService photoVotingService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private UserRankService userRankService;

    @Autowired
    private PhotoPreviewService photoPreviewService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private SecurityUIService securityUIService;

    @Autowired
    private BreadcrumbsPhotoService breadcrumbsPhotoService;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private EntryMenuService entryMenuService;

    @Autowired
    private ImageFileUtilsService imageFileUtilsService;

    private LogHelper log = new LogHelper();

    @ModelAttribute(PHOTO_CARD_MODEL)
    public PhotoCardModel prepareModel() {
        return new PhotoCardModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{photoId}/card/")
    public String photoCard(final @PathVariable("photoId") String _photoId, final @ModelAttribute(PHOTO_CARD_MODEL) PhotoCardModel model) {

        securityService.assertPhotoExists(_photoId);

        final int photoId = NumberUtils.convertToInt(_photoId);

        model.clear();

        final Photo photo = photoService.load(photoId);

        if (photo.getPhotoImageLocationType() == PhotoImageLocationType.FILE) {
            securityService.assertPhotoFileExists(photo);
        }

        securityUIService.assertUserWantSeeNudeContent(EnvironmentContext.getCurrentUser(), photo, urlUtilsService.getPhotoCardLink(photoId));

        model.setPhoto(photo);

        final User photoAuthor = userService.load(photo.getUserId());
        model.setUser(photoAuthor);

        final Genre genre = genreService.load(photo.getGenreId());
        model.setGenre(genre);

        model.setRootCommentsIds(photo.isArchived() ? photoCommentArchService.loadRootCommentsIds(photoId) : photoCommentService.loadRootCommentsIds(photoId));

        final User currentUser = EnvironmentContext.getCurrentUser();
        final int loggedUserId = currentUser.getId();

        model.setCommentDelay(photoCommentService.getUserDelayToNextComment(currentUser.getId()));
        model.setUserNextCommentTime(photoCommentService.getUserNextCommentTime(currentUser.getId()));
        model.setUsedDelayBetweenComments(photoCommentService.getUserDelayBetweenCommentsSec(currentUser));

        model.setUserPhotoVotes(photoVotingService.getUserVotesForPhoto(currentUser, photo));

        savePhotoPreviewIfNecessary(photo); // should be before photoInfo loading

        final PhotoInfo photoInfo = photoUIService.getPhotoInfo(photo, EnvironmentContext.getCurrentUser());
        model.setPhotoInfo(photoInfo);

        model.setMinCommentLength(configurationService.getInt(ConfigurationKey.COMMENTS_MIN_LENGTH));
        model.setMaxCommentLength(configurationService.getInt(ConfigurationKey.COMMENTS_MAX_LENGTH));

        model.setVotingUserMinAccessibleMarkForGenre(userRankService.getUserLowestNegativeMarkInGenre(loggedUserId, genre.getId()));
        model.setVotingUserMaxAccessibleMarkForGenre(userRankService.getUserHighestPositiveMarkInGenre(loggedUserId, genre.getId()));

        final Date currentTime = dateUtilsService.getCurrentTime();
        model.setCommentingValidationResult(securityService.validateUserCanCommentPhoto(currentUser, model.getPhoto(), currentTime, currentUser.getLanguage()));
        model.setVotingValidationResult(securityService.validateUserCanVoteForPhoto(currentUser, model.getPhoto(), currentTime, currentUser.getLanguage()));

        model.setVotingModel(userRankService.getVotingModel(photo.getUserId(), photo.getGenreId(), currentUser, currentTime));

        model.setPageTitleData(breadcrumbsPhotoService.getPhotoCardBreadcrumbs(photo, EnvironmentContext.getCurrentUser()));

        model.setEntryMenu(entryMenuService.getPhotoMenu(photo, currentUser));

        model.setShownDimension(imageFileUtilsService.resizePhotoImage(photo.getImageDimension()));

        return VIEW;
    }

    private void savePhotoPreviewIfNecessary(final Photo photo) {
        final User loggedUser = EnvironmentContext.getCurrentUser();
        final int loggedUserId = loggedUser.getId();

        if (UserUtils.isCurrentUserLoggedUser()) {
            boolean hasUserAlreadySeenThePhoto = photoPreviewService.hasUserAlreadySeenThisPhoto(photo.getId(), loggedUserId);
            if (!hasUserAlreadySeenThePhoto) {
                final PhotoPreview photoPreview = new PhotoPreview(photo, loggedUser);
                photoPreview.setPreviewTime(dateUtilsService.getCurrentTime());

                photoPreviewService.save(photoPreview);
            }
        }
    }
}
