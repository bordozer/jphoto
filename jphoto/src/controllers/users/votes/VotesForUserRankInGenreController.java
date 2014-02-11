package controllers.users.votes;

import core.context.EnvironmentContext;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserGenreRankHistoryEntry;
import core.general.user.UserRankPhotoVote;
import core.services.entry.GenreService;
import core.services.security.SecurityService;
import core.services.user.UserRankService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import utils.NumberUtils;
import core.services.pageTitle.PageTitleUserUtilsService;
import utils.TranslatorUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( "/members/{userId}/category/{genreId}/votes" )
public class VotesForUserRankInGenreController {

	public static final String MODEL_NAME = "votesForUserRankInGenreModel";
	private static final String VIEW = "users/votes/VotesForUserRankInGenre";

	@Autowired
	private UserService userService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PageTitleUserUtilsService pageTitleUserUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@ModelAttribute( MODEL_NAME )
	public VotesForUserRankInGenreModel prepareModel( final @PathVariable( "userId" ) String _userId, final @PathVariable( "genreId" ) int genreId ) {

		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		final VotesForUserRankInGenreModel model = new VotesForUserRankInGenreModel();

		model.setUser( userService.load( userId ) );
		model.setGenre( genreService.load( genreId ) );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showVotes( final @ModelAttribute( MODEL_NAME ) VotesForUserRankInGenreModel model ) {

		final User user = model.getUser();

		securityService.assertUserCanSeeUserRankVoteHistory( user, EnvironmentContext.getCurrentUser() );

		final Genre genre = model.getGenre();


		final List<UserRankPhotoVote> usersWhoVotedForUserRankInGenre = userRankService.getUsersWhoVotedForUserRankInGenre( user.getId(), genre.getId() );

		final List<UserGenreRankViewEntry> userGenreRankViewEntries = getUserGenreRankViewEntries( user, genre, usersWhoVotedForUserRankInGenre );

		model.setUserGenreRankViewEntries( userGenreRankViewEntries );

		int sumPoints = 0;
		for ( final UserRankPhotoVote userRankPhotoVote : usersWhoVotedForUserRankInGenre ) {
			sumPoints += userRankPhotoVote.getVotePoints();
		}
		model.setSumPoints( sumPoints );

		model.setRanksInGenrePointsMap( userRankService.getUserGenreRankPointsMap() );

		model.setPageTitleData( pageTitleUserUtilsService.getVotesForUserRankInGenreData( user, genre ) );

		return VIEW;
	}

	private List<UserGenreRankViewEntry> getUserGenreRankViewEntries( final User user, final Genre genre, final List<UserRankPhotoVote> usersWhoVotedForUserRankInGenre ) {

		final List<UserGenreRankHistoryEntry> userGenreRankHistoryEntries = userRankService.getUserGenreRankHistoryEntries( user.getId(), genre.getId() );

		if ( usersWhoVotedForUserRankInGenre.size() == 0 ) {
			return newArrayList();
		}

		final UserRankPhotoVote lastVoteForUserRank = usersWhoVotedForUserRankInGenre.get( 0 );

		Date exVoteTime = lastVoteForUserRank.getVoteTime();

		final List<UserGenreRankViewEntry> userGenreRankViewEntries = newArrayList();

		for ( final UserRankPhotoVote userRankPhotoVote : usersWhoVotedForUserRankInGenre ) {

			final Date voteTime = userRankPhotoVote.getVoteTime();

			final Iterator<UserGenreRankHistoryEntry> iterator = userGenreRankHistoryEntries.iterator();
			while( iterator.hasNext() ) {

				final UserGenreRankHistoryEntry userGenreRankHistoryEntry = iterator.next();
				final Date rankAssignTime = userGenreRankHistoryEntry.getAssignTime();

				if ( rankAssignTime.getTime() > exVoteTime.getTime() ) {
					final int rank = userGenreRankHistoryEntry.getRank();

					final UserGenreRankViewEntry viewHistoryEntry = new UserGenreRankViewEntry();
					viewHistoryEntry.setColumn1( TranslatorUtils.translate( "New status: $1", rank ) );
					viewHistoryEntry.setColumn2( String.valueOf( rank ) );
					viewHistoryEntry.setColumn3( dateUtilsService.formatDateTimeShort( rankAssignTime ) );
					viewHistoryEntry.setStatusChangeEntry( true );

					viewHistoryEntry.setUserRankWhenPhotoWasUploadedIconContainer( userRankService.getUserRankIconContainer( user, genre, rank ) );

					userGenreRankViewEntries.add( viewHistoryEntry );

					iterator.remove();

					break;
				}
			}

			final UserGenreRankViewEntry viewEntry = new UserGenreRankViewEntry();
			viewEntry.setColumn1( entityLinkUtilsService.getUserCardLink( userRankPhotoVote.getVoter() ) );
			viewEntry.setColumn2( String.format( "%s%d", ( userRankPhotoVote.getVotePoints() > 0 ? "+" : "" ), userRankPhotoVote.getVotePoints() ) );

			viewEntry.setColumn3( dateUtilsService.formatDateTimeShort( voteTime ) );

			userGenreRankViewEntries.add( viewEntry );

			exVoteTime = voteTime;
		}

		return userGenreRankViewEntries;
	}
}
