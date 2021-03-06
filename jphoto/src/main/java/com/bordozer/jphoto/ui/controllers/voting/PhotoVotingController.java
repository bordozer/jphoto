package com.bordozer.jphoto.ui.controllers.voting;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserPhotoVote;
import com.bordozer.jphoto.core.services.entry.VotingCategoryService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.photo.PhotoVotingService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping("photos/{photoId}")
public class PhotoVotingController {

    private static final String PHOTO_VOTING_VIEW = "voting/PhotoVoting";

    @Autowired
    private PhotoService photoService;

    @Autowired
    private VotingCategoryService votingCategoryService;

    @Autowired
    private PhotoVotingService photoVotingService;

    @Autowired
    private PhotoVotingValidator photoVotingValidator;

    @Autowired
    private UserRankService userRankService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private ConfigurationService configurationService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(photoVotingValidator);
    }

    @ModelAttribute("photoVotingModel")
    public PhotoVotingModel prepareModel(final @PathVariable("photoId") int photoId, final HttpServletRequest request) {
        final Photo photo = photoService.load(photoId);

        final PhotoVotingModel model = new PhotoVotingModel();
        model.setPhoto(photo);

        final User loggedUser = EnvironmentContext.getCurrentUser();
        model.setUserPhotoVotes(getUserPhotoVotes(loggedUser, photo, request));
        return model;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/voting/")
    public String photoVoting(final @Valid @ModelAttribute("photoVotingModel") PhotoVotingModel model, final BindingResult result) {

        model.setBindingResult(result);

        final Photo photo = model.getPhoto();
        final User loggedUser = EnvironmentContext.getCurrentUser();
        model.setShowPhotoVotingForm(securityService.userCanVoteForPhoto(loggedUser, photo));

        if (result.hasErrors()) {
            resetModel(model, photo);

            return PHOTO_VOTING_VIEW;
        }

        final List<UserPhotoVote> userPhotoVotes = model.getUserPhotoVotes();
        if (userPhotoVotes.isEmpty()) {
            return PHOTO_VOTING_VIEW;
        }

        final Date votingTime = userPhotoVotes.get(0).getVotingTime(); // TODO: this time is duplicated in userPhotoVote!
        if (!photoVotingService.saveUserPhotoVoting(EnvironmentContext.getCurrentUser(), photo, votingTime, userPhotoVotes)) {
            result.reject(translatorService.translate("Voting error", EnvironmentContext.getLanguage()), translatorService.translate("Error saving data to DB", EnvironmentContext.getLanguage()));

            resetModel(model, photo);
        }

        return PHOTO_VOTING_VIEW;
    }

    private void resetModel(final PhotoVotingModel model, final Photo photo) {
        model.setUserPhotoVotes(Collections.<UserPhotoVote>emptyList());

        final User loggedUser = EnvironmentContext.getCurrentUser();
        model.setVotingUserMinAccessibleMarkForGenre(getMinMarkInGenre(loggedUser, photo));
        model.setVotingUserMaxAccessibleMarkForGenre(getMaxMarkInGenre(loggedUser, photo));
    }

    private int getMaxMarkInGenre(final User user, final Photo photo) {
        return userRankService.getUserHighestPositiveMarkInGenre(user.getId(), photo.getGenreId());
    }

    private int getMinMarkInGenre(final User user, final Photo photo) {
        return userRankService.getUserLowestNegativeMarkInGenre(user.getId(), photo.getGenreId());
    }

    private List<UserPhotoVote> getUserPhotoVotes(final User user, final Photo photo, final HttpServletRequest request) {
        final List<UserPhotoVote> userVotes = newArrayList();

        final int categoriesCount = configurationService.getInt(ConfigurationKey.PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT);

        for (int i = 1; i <= categoriesCount; i++) {
            final UserPhotoVote userVote = getUserPhotoVote(user, photo, request, i);
            if (userVote != null) {
                userVotes.add(userVote);
            }
        }

        return userVotes;
    }

    private UserPhotoVote getUserPhotoVote(final User user, final Photo photo, final HttpServletRequest request, final int number) {

        final String categoryParam = request.getParameter(String.format("votingCategory%s", number));

        if (categoryParam == null) {
            return null;
        }

        final PhotoVotingCategory photoVotingCategory = votingCategoryService.load(Integer.parseInt(categoryParam));
        if (photoVotingCategory != null) {
            final int mark = Integer.parseInt(request.getParameter(String.format("%s%s", PhotoVotingModel.VOTING_CATEGORY_MARK_CONTROL, number)));

            final UserPhotoVote userPhotoVote = new UserPhotoVote(user, photo, photoVotingCategory);
            userPhotoVote.setMark(mark);
            userPhotoVote.setMaxAccessibleMark(getMaxMarkInGenre(user, photo));
            userPhotoVote.setVotingTime(dateUtilsService.getCurrentTime());

            return userPhotoVote;
        }

        return null;
    }

}
