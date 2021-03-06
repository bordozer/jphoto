package com.bordozer.jphoto.ui.controllers.marks.list;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserPhotoVote;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.photo.PhotoVotingService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.PhotoUIService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsPhotoService;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.newHashSet;

@Controller
@RequestMapping(value = "photos/{photoId}/marks/")
public class PhotoMarkListController {

    private static final String MODEL_NAME = "photoMarkListModel";
    private static final String VIEW = "marks/list/MarkList";

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoUIService photoUIService;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private PhotoVotingService photoVotingService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BreadcrumbsPhotoService breadcrumbsPhotoService;

    @Autowired
    private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

    @ModelAttribute(MODEL_NAME)
    public PhotoMarkListModel prepareModel(final @PathVariable("photoId") String _photoId) {

        securityService.assertPhotoExists(_photoId);

        final PhotoMarkListModel model = new PhotoMarkListModel();

        final int photoId = NumberUtils.convertToInt(_photoId);

        final Photo photo = photoService.load(photoId);
        model.setPhoto(photo);

        final User photoAuthor = userService.load(photo.getUserId());
        model.setPhotoAuthor(photoAuthor);

        final List<UserPhotoVote> photoVotes = photoVotingService.getPhotoVotes(photo);

        model.setVotingCategories(getVotingCategories(photoVotes));
        model.setUserVotesMap(getUserVotesMap(photoVotes));
        model.setMarksByCategoriesMap(getMarksByCategories(photoVotes));
        model.setPhotoPreviewWrapper(photoUIService.getPhotoPreviewWrapper(photo, EnvironmentContext.getCurrentUser()));

        final Genre genre = genreService.load(photo.getGenreId());
        model.setGenre(genre);

        model.setPhotoPreviewImgUrl(userPhotoFilePathUtilsService.getPhotoPreviewUrl(photo));

        model.setPageTitleData(breadcrumbsPhotoService.getPhotoAppraisementBreadcrumbs(photo, EnvironmentContext.getCurrentUser()));

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String commentsToUser(final @ModelAttribute(MODEL_NAME) PhotoMarkListModel model) {
        return VIEW;
    }

    private Set<PhotoVotingCategory> getVotingCategories(final List<UserPhotoVote> photoVotes) {
        final Set<PhotoVotingCategory> votingCategories = newHashSet();

        for (final UserPhotoVote photoVote : photoVotes) {
            final PhotoVotingCategory votingCategory = photoVote.getPhotoVotingCategory();
            if (!votingCategories.contains(votingCategory)) {
                votingCategories.add(votingCategory);
            }
        }

        return votingCategories;
    }

    private Map<User, Map<PhotoVotingCategory, UserPhotoVote>> getUserVotesMap(final List<UserPhotoVote> photoVotes) {
        final Map<User, Map<PhotoVotingCategory, UserPhotoVote>> userVotesMap = newLinkedHashMap();

        for (final UserPhotoVote photoVote : photoVotes) {
            final User user = photoVote.getUser();

            boolean isContainsUser = userVotesMap.containsKey(user);
            final Map<PhotoVotingCategory, UserPhotoVote> userVoteForCategoryMap;
            if (isContainsUser) {
                userVoteForCategoryMap = userVotesMap.get(user);
            } else {
                userVoteForCategoryMap = newLinkedHashMap();
            }

            if (userVoteForCategoryMap.containsKey(photoVote.getPhotoVotingCategory())) {
                photoVote.setMark(userVoteForCategoryMap.get(photoVote.getPhotoVotingCategory()).getMark() + photoVote.getMark());
                photoVote.setMaxAccessibleMark(userVoteForCategoryMap.get(photoVote.getPhotoVotingCategory()).getMaxAccessibleMark() + photoVote.getMaxAccessibleMark());
            }
            userVoteForCategoryMap.put(photoVote.getPhotoVotingCategory(), photoVote);

            if (!isContainsUser) {
                userVotesMap.put(user, userVoteForCategoryMap);
            }
        }

        return userVotesMap;
    }

    private Map<PhotoVotingCategory, Integer> getMarksByCategories(final List<UserPhotoVote> photoVotes) {
        final Map<PhotoVotingCategory, Integer> totalMarksMap = newLinkedHashMap();

        for (final UserPhotoVote photoVote : photoVotes) {
            final PhotoVotingCategory votingCategory = photoVote.getPhotoVotingCategory();
            if (!totalMarksMap.containsKey(votingCategory)) {
                totalMarksMap.put(votingCategory, 0);
            }

            final int marks = totalMarksMap.get(votingCategory);
            totalMarksMap.put(votingCategory, marks + photoVote.getMark());
        }
        return totalMarksMap;
    }
}
