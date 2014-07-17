define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/upload/userTeam/templates/user-team-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-footer-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-list-entry-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-member-info-template.html"
		, "text!modules/photo/upload/userTeam/templates/user-team-member-edit-template.html"
		], function ( Backbone, $, _, userTeamHeaderTemplate, userTeamFooterTemplate, userTeamListEntryTemplate, userTeamMemberInfoTemplate, userTeamMemberEditorTemplate ) {

	'use strict';

	var UserTeamView = Backbone.View.extend( {

		userTeamHeaderTemplate:_.template( userTeamHeaderTemplate ),
		userTeamFooterTemplate:_.template( userTeamFooterTemplate ),

		events: {
			"click .create-new-user-team-member-link": "onCreateNewTeamMember"
		},

		initialize: function() {
			this.listenTo( this.model, "request", this.renderUserTeamHeader );
			this.listenTo( this.model, "add", this.renderUserTeamListEntry );
			this.listenTo( this.model, "sync", this.renderUserTeamFooter );

			this.listenTo( this.model, "event:create_new_user_team_member", this.createNewUserTeamMember );

			this.model.fetch( {cache: false} );
		},

		renderUserTeamHeader:function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.userTeamHeaderTemplate( modelJSON ) );
		},

		renderUserTeamListEntry:function ( teamMember ) {
			var userTeamListEntryView = new UserTeamListEntryView( {
				model: teamMember
			} );
			this.$el.append( userTeamListEntryView.render().$el );
		},

		renderUserTeamFooter:function () {
			var modelJSON = this.model.toJSON();
			this.$el.append( this.userTeamFooterTemplate( modelJSON ) );
		},

		createNewTeamMember: function() {
			this.model.trigger( 'event:create_new_user_team_member' );
		},

		onCreateNewTeamMember: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.createNewTeamMember();
		},

		createNewUserTeamMember: function() {
			console.log( 'create new user team member' );
		}
	});


	var UserTeamListEntryView = Backbone.View.extend({

		userTeamListEntryTemplate:_.template( userTeamListEntryTemplate ),
		userTeamMemberViewTemplate:_.template( userTeamMemberInfoTemplate ),
		userTeamMemberEditorTemplate:_.template( userTeamMemberEditorTemplate ),

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
			this.listenTo( this.model, "change", this.render );
		},

		events: {
			"click .user-team-member-name": "onToggleUserTeamMemberInfo"
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			if ( this.model.get( 'openInfo' ) ) {
				this.$el.append( this.userTeamMemberViewTemplate( modelJSON ) );
			} else if ( this.model.get( 'openEditor' ) ) {
				this.$el.html( this.userTeamMemberEditorTemplate( modelJSON ) );
			} else {
				this.$el.html( this.userTeamListEntryTemplate( modelJSON ) );
			}

			return this;
		},

		toggleUserTeamMemberInfo: function() {
			var isOpenInfo = this.model.get( 'openInfo' );
			this.model.set( { openInfo: isOpenInfo == undefined || ! isOpenInfo } );
		},

		onToggleUserTeamMemberInfo: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.toggleUserTeamMemberInfo();
		}
	});

	/*var UserTeamMemberInfoView = Backbone.View.extend({

		userTeamMemberViewTemplate:_.template( userTeamMemberInfoTemplate ),

		initialize: function() {

		},

		render: function() {

			this.model.set( {  } );

			var modelJSON = this.model.toJSON();
			this.$el.html( this.userTeamMemberViewTemplate( modelJSON ) );
			console.log( this.$el.html() );
//			return this;
		}
	});*/



	/*var UserTeamMemberEditorView = Backbone.View.extend( {

		userTeamMemberEditorTemplate:_.template( userTeamMemberEditorTemplate ),

		initialize: function() {

		},

		render: function() {
			this.$el.append( this.userTeamMemberEditorTemplate( modelJSON ) );
		}
	});*/




	/*var CompositeUserTeamMemberView = Backbone.View.extend({

		initialize: function() {
			this.model.fetch( { cache: false } );
		},

		render: function() {
			if ( ! this.model.isOpenForEdit() ) {
				return this.renderView( new UserTeamMemberView( {
					model: this.model
				}));
			} else {
				return this.renderView( new UserTeamMemberEditorView( {
					model: this.model
				}));
			}
		},

		renderView: function( view ) {
			this.$el.html( view.render().$el );
			return this;
		}
	});*/



	return { UserTeamView: UserTeamView };
});