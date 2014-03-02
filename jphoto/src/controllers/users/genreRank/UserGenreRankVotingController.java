package controllers.users.genreRank;

import core.context.EnvironmentContext;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.UserRankInGenreVoting;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.translator.TranslatorService;
import core.services.user.UserRankService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import utils.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping( "/voting/rank/voting" )
@Controller
public class UserGenreRankVotingController {

	private static final String VIEW = "users/genreRank/RankInGenreVotingSave";
	public static final String MODEL_NAME = "userGenreRankVotingModel";

	@Autowired
	private UserService userService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private UserGenreRankVotingValidator userGenreRankVotingValidator;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private TranslatorService translatorService;

	@InitBinder
	protected void initBinder( final WebDataBinder binder ) {
		binder.setValidator( userGenreRankVotingValidator );
	}

	@ModelAttribute( MODEL_NAME )
	public UserGenreRankVotingModel prepareModel( final HttpServletRequest request ) {

		final String _userId = request.getParameter( "userId" );
		final String _genreId = request.getParameter( "genreId" );
		final String _photoId = request.getParameter( "photoId" );
		final String _points = request.getParameter( "points" );

		int userId = NumberUtils.convertToInt( _userId );
		int genreId = NumberUtils.convertToInt( _genreId );
		int points = NumberUtils.convertToInt( _points );

		final UserGenreRankVotingModel model = new UserGenreRankVotingModel();

		if ( userId == 0 ) {
			// voting by photo
			final int photoId = NumberUtils.convertToInt( _photoId );
			final Photo photo = photoService.load( photoId );
			userId = photo.getUserId();
			genreId = photo.getGenreId();
			model.setPhoto( photo );
		}

		model.setVoter( EnvironmentContext.getCurrentUser() );
		model.setUser( userService.load( userId ) );
		model.setGenre( genreService.load( genreId ) );
		model.setVotingForRankIncreasing( points > 0 );

		model.setVotingModel( userRankService.getVotingModel( userId, genreId, EnvironmentContext.getCurrentUser() ) );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String votingByUserAndGenre( @Valid final @ModelAttribute( MODEL_NAME ) UserGenreRankVotingModel model, final BindingResult result ) {

		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return VIEW;
		}

		final boolean rankIncreasing = model.isVotingForRankIncreasing();
		final VotingModel votingModel = model.getVotingModel();

		final UserRankInGenreVoting voting = new UserRankInGenreVoting();
		voting.setUserId( model.getUser().getId() );
		voting.setVoterId( model.getVoter().getId() );
		voting.setGenreId( model.getGenre().getId() );
		voting.setUserRankWhenVoting( votingModel.getUserRankInGenre() );
		voting.setPhoto( model.getPhoto() );

		final int points = votingModel.getLoggedUserVotingPoints() * ( rankIncreasing ? 1 : -1 );
		voting.setPoints( points );

		voting.setVotingTime( dateUtilsService.getCurrentTime() );

		final User votingUser = EnvironmentContext.getCurrentUser();

		if ( !userRankService.saveVotingForUserRankInGenre( voting, votingUser ) ) {
			result.reject( translatorService.translate( "Voting error" ), translatorService.translate( "Error saving data to DB" ) );
			return VIEW;
		}

		model.setVotingModel( userRankService.getVotingModel( model.getUser().getId(), model.getGenre().getId(), votingUser ) );

		return VIEW;
	}
}
