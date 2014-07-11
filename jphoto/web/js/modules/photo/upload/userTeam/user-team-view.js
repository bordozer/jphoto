define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/upload/userTeam/templates/user-team-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-member-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-member-edit-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-footer-template.html"
		], function ( Backbone, $, _, userTeamTemplate, userTeamMemberTemplate, userTeamMemberEditTemplate, userTeamFooter ) {

	'use strict';

	var UserTeamView = Backbone.View.extend( {

		userTeamTemplate:_.template( userTeamTemplate ),
		userTeamMemberTemplate:_.template( userTeamMemberTemplate ),
		userTeamMemberEditTemplate:_.template( userTeamMemberEditTemplate ),
		userTeamFooter:_.template( userTeamFooter ),

		events: {
			"click .create-new-user-team-member-link": "onCreateNewTeamMember"
		},

		initialize: function() {
			this.listenTo( this.model, "request", this.renderUserTeam );
			this.listenTo( this.model, "add", this.renderUserTeamMember );
			this.listenTo( this.model, "sync", this.renderFooter );

			this.model.fetch( {cache: false} );
		},

		renderUserTeam:function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.userTeamTemplate( modelJSON ) );
		},

		renderUserTeamMember:function ( teamMember ) {
			var modelJSON = teamMember.toJSON();
			this.$el.append( this.userTeamMemberTemplate( modelJSON ) );
		},

		renderFooter:function () {
			var modelJSON = this.model.toJSON();
			this.$el.append( this.userTeamFooter( modelJSON ) );
		},

		createNewTeamMember: function() {
//			this.trigger( 'create-new-user' );
			console.log( 'Create new user' );
		},

		onCreateNewTeamMember: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.createNewTeamMember();
		}
	});

	return { UserTeamView: UserTeamView };
});

//http://fahad19.tumblr.com/post/28158699664/afterrender-callback-in-backbone-js-views