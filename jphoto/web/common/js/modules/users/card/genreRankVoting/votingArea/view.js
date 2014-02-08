define( ["backbone", "jquery", "underscore", "text!users/card/genreRankVoting/votingArea/templates/icons.html", "text!users/card/genreRankVoting/votingArea/templates/votes.html"], function ( Backbone, $, _, iconsTemplate, votesTemplate ) {

	var VotingAreaView = Backbone.View.extend( {
												   iconsTemplate:_.template( iconsTemplate ),
//												   votesTemplate:_.template( votesTemplate ),
												   initialize:function () {
													   this.listenTo( this.model, "add", this.renderVotingArea );
													   this.model.fetch();
												   },

												   renderVotingArea:function ( votingAreaModel ) {
													   var json = votingAreaModel.toJSON();

													   var rendered = this.iconsTemplate( json );
													   var selector = ".user-genre-rank-voting-" + json.userId + "-" + json.genreId; // + ' td';
													   console.log( 'selector: ', selector );
													   this.$( selector ).html( $( rendered ) );
												   }
											   } );

	return { VotingAreaView:VotingAreaView };
} );
