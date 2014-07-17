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
			"click .user-team-member-info": "onToggleUserTeamMemberInfo",
			"click .user-team-member-edit": "onToggleUserTeamMemberEdit",

			"click .save-user-team-member": "onSaveUserTeamMember",
			"click .cancel-user-team-member-changes": "onCancelUserTeamMember"
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			var listEntryTemplate = this.userTeamListEntryTemplate( modelJSON );
			this.$el.html( listEntryTemplate );

			if ( this.model.get( 'openInfo' ) ) {
				this.$el.append( this.userTeamMemberViewTemplate( modelJSON ) );
			} else if ( this.model.get( 'openEditor' ) ) {
				this.$el.append( this.userTeamMemberEditorTemplate( modelJSON ) );
			}

			return this;
		},

		toggleUserTeamMemberInfo: function() {
			var isOpenInfo = this.model.get( 'openInfo' );
			this.model.set( { openInfo: isOpenInfo == undefined || ! isOpenInfo, openEditor: false } );
		},

		toggleUserTeamMemberEdit: function() {
			var isOpenEditor = this.model.get( 'openEditor' );
			this.model.set( { openInfo: false, openEditor: isOpenEditor == undefined || ! isOpenEditor } );
		},

		saveUserTeamMember: function() {
			console.log( 'saving...' );
		},

		onToggleUserTeamMemberInfo: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.toggleUserTeamMemberInfo();
		},

		onToggleUserTeamMemberEdit: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.toggleUserTeamMemberEdit();
		},

		onSaveUserTeamMember: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			if ( confirm( 'Save changes?' ) ) {
				this.saveUserTeamMember();
			}

			this.model.set( { openInfo: false, openEditor: false } );

			this.render();
		},

		onCancelUserTeamMember: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.model.set( { openInfo: false, openEditor: false } );

			this.render();
		}
	});

	return { UserTeamView: UserTeamView };
});