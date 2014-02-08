define( ["backbone"], function ( Backbone ) {

	var VotingArea = Backbone.Model.extend( {

										  } );

	var VotingAreas = Backbone.Collection.extend( {
													 model:VotingArea,
													 url:"/jphoto/json/members/" + userId_card + "/card/genreRankVoting/"
												 } );

	return {VotingArea:VotingArea, VotingAreas:VotingAreas};
} );
