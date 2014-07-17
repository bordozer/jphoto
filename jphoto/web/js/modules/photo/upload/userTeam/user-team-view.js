define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/upload/userTeam/templates/header-template.html"
		, "text!modules/photo/upload/userTeam/templates/footer-template.html"
		, "text!modules/photo/upload/userTeam/templates/list-entry-template.html"
		, "text!modules/photo/upload/userTeam/templates/entry-info-template.html"
		, "text!modules/photo/upload/userTeam/templates/entry-edit-template.html"
		], function ( Backbone, $, _, headerTemplate, footerTemplate, listEntryTemplate, entryInfoTemplate, entryEditorTemplate ) {

	'use strict';

	var UserTeamView = Backbone.View.extend( {

		userTeamHeaderTemplate:_.template( headerTemplate ),
		userTeamFooterTemplate:_.template( footerTemplate ),

		events: {
			"click .create-new-user-team-member-link": "onCreateNewEntry"
		},

		initialize: function() {
			this.listenTo( this.model, "request", this.renderHeader );
			this.listenTo( this.model, "add", this.renderEntry );
			this.listenTo( this.model, "sync", this.renderFooter );

			this.model.fetch( {cache: false} );
		},

		renderHeader:function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.userTeamHeaderTemplate( modelJSON ) );
		},

		renderEntry:function ( teamMember ) {

			var userTeamListEntryView = new UserTeamListEntryView( {
				model: teamMember
			} );
			userTeamListEntryView.on( "event:render_list", this.model.refresh, this.model );

			this.$el.append( userTeamListEntryView.render().$el );
		},

		renderFooter:function () {
			var modelJSON = this.model.toJSON();
			this.$el.append( this.userTeamFooterTemplate( modelJSON ) );
		},

		createEntry: function() {
			// TODO: create new
		},

		onCreateNewEntry: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.createEntry();
		}
	});


	var UserTeamListEntryView = Backbone.View.extend({

		userTeamListEntryTemplate:_.template( listEntryTemplate ),
		userTeamMemberViewTemplate:_.template( entryInfoTemplate ),
		userTeamMemberEditorTemplate:_.template( entryEditorTemplate ),

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
			this.listenTo( this.model, "change", this.render );
		},

		events: {
			"click .user-team-member-info": "onToggleInfo",
			"click .user-team-member-edit": "onToggleEditor",

			"keydown .user-team-member-data": "onDataChange",

			"click .user-team-member-close": "onCloseEditorWithoutChanges",
			"click .user-team-member-save": "onSaveDataClick",
			"click .user-team-member-discard-changes": "onDiscardEditedData"
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			var listEntryTemplate = this.userTeamListEntryTemplate( modelJSON );
			this.$el.html( listEntryTemplate );

			if ( this.model.get( 'openEditor' ) ) {
				this.$el.append( this.userTeamMemberEditorTemplate( modelJSON ) );
			} else if ( this.model.get( 'openInfo' ) ) {
				this.$el.append( this.userTeamMemberViewTemplate( modelJSON ) );
			}

			return this;
		},

		toggleInfo: function() {
			var isOpenInfo = this.model.get( 'openInfo' );
			this.model.set( { openInfo: isOpenInfo == undefined || ! isOpenInfo, openEditor: false } );
		},

		toggleEditor: function() {
			var isOpenEditor = this.model.get( 'openEditor' );
			this.model.set( { openEditor: isOpenEditor == undefined || ! isOpenEditor } );
		},

		doSaveData: function() {
			this.bindModel();
			this.save();
		},

		bindModel: function(  ) {
			this.model.set( { userTeamMemberName: this.$( '.user-team-member-name' ).val() } );
		},

		save: function() {
			this.model.save()
				.done( _.bind( this.onSaveSuccess, this ) )
				.fail( _.bind( this.onSaveError, this ) );
		},

		onSaveSuccess: function() {
			showUIMessage_Notification( "Team member changes has been saved successfully" );
			this.trigger( "event:render_list" );
		},

		onSaveError: function() {
			var errorText = '';
			var errors = response[ 'responseJSON' ];
			for ( var i = 0; i < errors.length; i++ ) {
				errorText += errors[ i ][ 'defaultMessage' ] + "\n";
			}

			showUIMessage_Error( errorText );
		},

		onToggleInfo: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.toggleInfo();
		},

		onToggleEditor: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.toggleEditor();
		},

		onDataChange: function() {
			this.model.set( { hasUnsavedChanged: true }, { silent: true } );

			this.$( '.user-team-member-close' ).hide();

			this.$( '.user-team-member-save' ).show();
			this.$( '.user-team-member-discard-changes' ).show();
		},

		onCloseEditorWithoutChanges: function() {
			this.closeEditor();
		},

		onSaveDataClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			if ( this.model.get( 'hasUnsavedChanged' ) && ! confirm( 'Save changes?' ) ) {
				return;
			}

			this.doSaveData();
		},

		onDiscardEditedData: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			if ( this.model.get( 'hasUnsavedChanged' ) && ! confirm( 'Discard changes?' ) ) {
				return;
			}

			this.closeEditor();
		},

		closeEditor: function() {
			this.model.set( { openEditor: false } );

			this.render();
		}
	});

	return { UserTeamView: UserTeamView };
});