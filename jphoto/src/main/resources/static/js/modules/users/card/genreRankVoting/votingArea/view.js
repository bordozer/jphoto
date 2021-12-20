define( ["backbone", "jquery", "underscore"
			, "text!users/card/genreRankVoting/votingArea/templates/icons.html"
			, "text!users/card/genreRankVoting/votingArea/templates/votes.html"
			, "text!users/card/genreRankVoting/votingArea/templates/canNotVote.html"], function ( Backbone, $, _, iconsTemplate, votesTemplate, canNotVoteTemplate ) {

	var VotingAreaView = Backbone.View.extend( {
												   iconsTemplate:_.template( iconsTemplate ),
												   votesTemplate:_.template( votesTemplate ),
												   canNotVoteTemplate:_.template( canNotVoteTemplate ),
												   initialize:function () {
													   this.listenTo( this.model, "add", this.renderVotingArea );
													   this.model.fetch();
												   },

												   renderVotingArea:function ( votingAreaModel ) {
													   var json = votingAreaModel.toJSON();

													   var rendered;

													   var isUiVoting_NA = json.uiVotingIsInaccessible;
													   if ( isUiVoting_NA ) {
														   console.log( json.genreId + ': NA = ' + isUiVoting_NA );
														   rendered = this.canNotVoteTemplate( json );
													   } else if ( json.votedForThisRank ) {
														   console.log( json.genreId + ': votedForThisRank = ' + json.votedForThisRank );
														   rendered = this.votesTemplate( json );
													   } else {
														   console.log( json.genreId + ': ICONS ' );
														   rendered = this.iconsTemplate( json );
													   }
													   $( ".user-genre-rank-voting-" + json.userId + "-" + json.genreId ).html( $( rendered ) );
												   }
											   } );

	return { VotingAreaView:VotingAreaView };
} );
