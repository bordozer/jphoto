define( ["backbone"], function ( Backbone ) {

	var VotingArea = Backbone.Model.extend( {

										  } );

	var VotingAreas = Backbone.Collection.extend( {
													 model:VotingArea,
													 url:"/jphoto/json/members/8526/card/genreRankVoting/"
												 } );

	return {VotingArea:VotingArea, VotingAreas:VotingAreas};
} );
