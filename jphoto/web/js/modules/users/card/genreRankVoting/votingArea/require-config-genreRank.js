require( ["users/card/genreRankVoting/votingArea/view", "users/card/genreRankVoting/votingArea/model", "jquery"], function ( View, Model, $ ) {

	var model = new Model.VotingAreas();

	var view = new View.VotingAreaView( {model: model} );
	//		$( "body" ).append( view.$el ); // TODO

	//		console.log( model );
} );