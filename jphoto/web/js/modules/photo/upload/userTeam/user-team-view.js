define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/upload/userTeam/templates/user-team-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-member-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-member-edit-template.html"
		], function ( Backbone, $, _, userTeamTemplate, userTeamMemberTemplate, userTeamMemberEditTemplate ) {

	'use strict';

	var UserTeamView = Backbone.View.extend( {

		userTeamTemplate:_.template( userTeamTemplate ),
		userTeamMemberTemplate:_.template( userTeamMemberTemplate ),
		userTeamMemberEditTemplate:_.template( userTeamMemberEditTemplate ),

		initialize: function() {
			this.listenTo( this.model, "request", this.renderUserTeam );
			this.listenTo( this.model, "add", this.renderUserTeamMember );

			this.model.fetch( {cache: false} );
		},

		renderUserTeam:function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.userTeamTemplate( modelJSON ) );
		},

		renderUserTeamMember:function ( teamMember ) {
			var modelJSON = teamMember.toJSON();
			this.$el.append( this.userTeamMemberTemplate( modelJSON ) );
		}
	});

	return { UserTeamView: UserTeamView };
});