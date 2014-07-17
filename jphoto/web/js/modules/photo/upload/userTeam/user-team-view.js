define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/upload/userTeam/templates/user-team-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-member-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-member-edit-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-footer-template.html"
		], function ( Backbone, $, _, userTeamTemplate, userTeamMemberTemplate, userTeamMemberEditTemplate, userTeamFooter ) {

	'use strict';

	var UserTeamView = Backbone.View.extend( {

		userTeamTemplate: _.template( userTeamTemplate )
		, userTeamMemberTemplate: _.template( userTeamMemberTemplate )

		, initialize: function() {
			this.listenTo( this.model, "add", this.addTeamMember );
			this.model.fetch({ cache: false });
		}

		, render: function() {

			console.log( 'User team rendering' );

			var modelJSON = this.model.toJSON();

			this.$el.html( this.userTeamTemplate( modelJSON ) );
			return this;
		}

		, addTeamMember: function( teamMember ) {
			var modelJSON = teamMember.toJSON();

			console.log( 'teamMember: ', teamMember );
			console.log( 'modelJSON: ', modelJSON );

			this.$el.append( this.userTeamMemberTemplate( modelJSON ) );
		}
	});

	return { UserTeamView: UserTeamView};
});