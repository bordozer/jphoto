package json.users.rankInGenreVoting;

import core.context.EnvironmentContext;
import core.general.genre.Genre;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.user.UserRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RequestMapping( "/members/{userId}/card/genreRankVoting" )
@Controller
public class UserGenreRankVotingController {

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private GenreService genreService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public List<UserCardVotingAreaModel> userCardVotingAreas( final @PathVariable( "userId" ) int userId ) {

		final User currentUser = EnvironmentContext.getCurrentUser();
		final int voterId = currentUser.getId();

		final List<UserCardVotingAreaModel> result = newArrayList();

		final List<Genre> genres = genreService.loadAll();
		for ( final Genre genre : genres ) {
			final int qty = photoService.getPhotoQtyByUserAndGenre( userId, genre.getId() );
			if ( qty > 0 ) {
				final UserCardVotingAreaModel votingAreaModel = new UserCardVotingAreaModel();
				votingAreaModel.setUserId( userId );
				votingAreaModel.setGenreId( genre.getId() );
				votingAreaModel.setVoterId( voterId );
				votingAreaModel.setVoterRankInGenreVotingPoints( userRankService.getUserRankInGenreVotingPoints( voterId, genre.getId() ) );

				result.add( votingAreaModel );
			}
		}

		return result;
	}
}
