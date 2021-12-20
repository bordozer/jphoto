define( ["backbone"], function ( Backbone ) {

	var VotingArea = Backbone.Model.extend( {

										  } );

	var VotingAreas = Backbone.Collection.extend( {
													 model:VotingArea,
													 url:"/rest/members/" + userId_card + "/card/genreRankVoting/" /* TODO: pass context here */
												 } );

	return {VotingArea:VotingArea, VotingAreas:VotingAreas};
} );
