package com.bordozer.jphoto.rest.users.rankInGenreVoting;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.UserRankInGenreVotingValidationResult;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RequestMapping("/rest//members/{userId}/card/genreRankVoting")
@Controller
public class MemberGenreRankVotingController {

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private UserRankService userRankService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @RequestMapping(method = RequestMethod.GET, value = "/", produces = "application/json")
    @ResponseBody
    public List<UserCardVotingAreaModel> userCardVotingAreas(final @PathVariable("userId") int userId) {

        final User voter = EnvironmentContext.getCurrentUser();
        final int voterId = voter.getId();

        final User user = userService.load(userId);

        final List<UserCardVotingAreaModel> result = newArrayList();

        final List<Genre> genres = genreService.loadAll();
        for (final Genre genre : genres) {
            final int qty = photoService.getPhotosCountByUserAndGenre(userId, genre.getId());
            if (qty > 0) {

                final UserCardVotingAreaModel votingAreaModel = new UserCardVotingAreaModel();

                votingAreaModel.setUserId(userId);
                votingAreaModel.setGenreId(genre.getId());
                votingAreaModel.setVoterId(voterId);
                votingAreaModel.setVoterRankInGenreVotingPoints(userRankService.getUserRankInGenreVotingPoints(voterId, genre.getId()));

                final UserRankInGenreVotingValidationResult validationResult = securityService.getUserRankInGenreVotingValidationResult(user, voter, genre, dateUtilsService.getCurrentTime(), voter.getLanguage());
                votingAreaModel.setUiVotingIsInaccessible(validationResult.isUiVotingIsInaccessible());
                votingAreaModel.setValidationMessage(validationResult.getValidationMessage());

                final int userRankInGenre = userRankService.getUserRankInGenre(user.getId(), genre.getId());

                final boolean isVotedForThisRank = userRankService.isUserVotedLastTimeForThisRankInGenre(voterId, user.getId(), genre.getId(), userRankInGenre);
                //				final boolean isHavingEnoughPhotosInGenre = userRankService.isUserHavingEnoughPhotosInGenre( user.getId(), genre.getId() );

                votingAreaModel.setVotedForThisRank(isVotedForThisRank);
                if (isVotedForThisRank) {
                    votingAreaModel.setUserLastVotingResult(userRankService.setUserLastVotingResult(voterId, user.getId(), genre.getId()));
                }

                result.add(votingAreaModel);
            }
        }

        return result;
    }
}
