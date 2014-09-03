define( ["backbone", "jquery", "underscore", "mass_checker"
		, "components/editableList/editable-list-model"
		, "text!components/editableList/templates/header-template.html"
		, "text!components/editableList/templates/list-entry-template.html"
		, "text!components/editableList/templates/entry-info-template.html"
		, "text!components/editableList/templates/entry-edit-template.html"
		], function ( Backbone, $, _, mass_checker, Model, headerTemplate, listEntryTemplate, entryInfoTemplate, entryEditorTemplate ) {

	'use strict';

	var EntryListView = Backbone.View.extend( {

		userTeamHeaderTemplate:_.template( headerTemplate ),

		events: {
			"click .create-new-user-team-member-link": "onCreateNewEntry"
		},

		initialize: function( options ) {
			this.onEdit = options.onEdit;
			this.onDelete = options.onDelete;

			this.listenTo( this.model, "add", this.renderEntry );

			var model = this.model;
			var el = this.$el;
			this.model.fetch( { cache: false, success: function (  ) {
				_.each( model.selectedUserTeamMemberIds, function( memberId ) {
					$( '.user-team-member-checkbox-' + memberId, el ).attr( 'checked', 'checked' );
				} );
			} } );
		},

		renderHeader: function () {
			var modelJSON = this.model.toJSON();

			var translationsDTO = this.model[ 'translationDTO' ];
			modelJSON[ 'headerTitleCreateNewMemberButtonTitle' ] = translationsDTO[ 'headerTitleCreateNewMemberButtonTitle' ];

			this.$el.html( this.userTeamHeaderTemplate( modelJSON ) );
		},

		renderEntry: function ( teamMember ) {
			teamMember.set( { userTeamMemberTypes: this.model[ 'userTeamMemberTypes' ], translationDTO: this.model[ 'translationDTO' ] } );
			var view = this;

			var massSelectorCss = this.model.groupSelectionClass;
			var entryView = new EntryView( {
				model: teamMember
				, massSelectorCss: massSelectorCss
				, onEdit: view.onEdit
				, onDelete: view.onDelete
			} );

			this.$el.append( entryView.render().$el );

			// TODO: duoplicates!
			if( massSelectorCss != '' ) {
				var massSelector = mass_checker.getMassChecker();
				var css = massSelectorCss + teamMember.get( 'entryId' );
				massSelector.registerUnselected( css, Backbone.JPhoto.imageFolder() );
			}
		},

		createEntry: function() {

			var userTeamMemberTypes = this.model[ 'userTeamMemberTypes' ];
			var translationDTO = this.model[ 'translationDTO' ];

			var teamMember = new Model.EditableListEntryModel( {
				  userTeamMemberId: 0
				, userTeamMemberName: ''
				, checked: true
				, openEditor: true
				, userTeamMemberTypes: userTeamMemberTypes
				, translationDTO: translationDTO
			} );
			this.model.add( teamMember );
		},

		deleteEntry: function( teamMember ) {
			var teamMemberId = teamMember.get( 'entryId' );
			this.model.remove( teamMemberId );
			this.model.onDelete( teamMemberId );
		},

		onCreateNewEntry: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.createEntry();
		}
	});


	var EntryView = Backbone.View.extend({

		userTeamListEntryTemplate:_.template( listEntryTemplate ),
		userTeamMemberViewTemplate:_.template( entryInfoTemplate ),
		userTeamMemberEditorTemplate:_.template( entryEditorTemplate ),

		initialize: function( options ) {
			this.massSelectorCss = options.massSelectorCss;

			this.onEdit = options.onEdit;
			this.onDelete = options.onDelete;

			this.listenTo( this.model, "sync", this.render );
			this.listenTo( this.model, "change", this.render );

			this.listenTo( this.model, "destroy", this.removeView );
		},

		events: {
			"click .user-team-member-info": "onToggleInfo",
			"click .user-team-member-edit": "onToggleEditor",
			"click .user-team-member-delete": "onEntryDelete",
			"click .user-team-member-can-not-delete": "onCanNotDeleteClick",
			"change .user-team-member-checkbox": "onToggleCheckbox",

			"keydown .user-team-member-data": "onDataChange",
			"change .user-team-member-data-type": "onDataChange",

			"click .user-team-member-cancel": "onCloseEditorWithoutChanges",
			"click .user-team-member-save": "onSaveDataClick",
			"click .user-team-member-discard-changes": "onDiscardEditedData"
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			modelJSON[ 'translationsDTO' ] = this.model.get( 'translationDTO' );

			this.$el.html( "" );

			this.renderHeader( modelJSON );

			if ( this.model.get( 'openEditor' ) ) {
				this.$el.append( this.userTeamMemberEditorTemplate( modelJSON ) );
				this.$( '.user-team-member-name' ).focus();
				this.$( '.user-team-member-name' ).select();
			} else if ( this.model.get( 'openInfo' ) ) {
				this.$el.append( this.userTeamMemberViewTemplate( modelJSON ) );
			}

			// TODO: duoplicates!
			if( this.massSelectorCss != '' ) {
				var massSelector = mass_checker.getMassChecker();
				var css = this.massSelectorCss + this.model.get( 'entryId' );
				massSelector.registerUnselected( css, Backbone.JPhoto.imageFolder() );
			}

			return this;
		},

		renderHeader: function( modelJSON ) {
			var entryDiv = $( "<div class='user-team-list-entry'></div>" );

			if( this.massSelectorCss == '' ) {
				entryDiv.append( "<input type='checkbox' name='userTeamMemberIds' class='user-team-member-checkbox user-team-member-checkbox-" + modelJSON.entryId + "'"
				 + " value='" + modelJSON.entryId + "' " + ( modelJSON.checked ? "checked='checked'" : "" ) + " />" );
			}

			if( this.massSelectorCss != '' ) {
				entryDiv.append( "<img class='" + 'mass-selector-icon-' + this.massSelectorCss + modelJSON.entryId + "' width='16' height='16' /> " );
			}

			if( modelJSON.entryId > 0 && ! modelJSON.openEditor ) {
				entryDiv.append( "<a href='#' class='user-team-member-info' onclick='return false;' title='" + modelJSON.userTeamMemberNameTitle + "'>"
										+ modelJSON.userTeamMemberName + "</a> - " + modelJSON.teamMemberPhotosQty + " " + modelJSON.translationsDTO.listEntryPhotos );
			}

			if( modelJSON.entryId > 0 && modelJSON.openEditor ) {
				entryDiv.append( modelJSON.userTeamMemberName + " - " + modelJSON.teamMemberPhotosQty + ' ' + modelJSON.translationsDTO.listEntryPhotos );
			}

			if( modelJSON.entryId == 0 ) {
				entryDiv.append( modelJSON.translationsDTO.newEntryDefaultName );
			}

			this.$el.append( entryDiv );
		},

		toggleInfo: function() {
			var isOpenInfo = this.model.get( 'openInfo' );
			this.model.set( { openInfo: isOpenInfo == undefined || ! isOpenInfo, openEditor: false } );
		},

		toggleEditor: function() {
			var isOpenEditor = this.model.get( 'openEditor' );
			this.model.set( { openEditor: isOpenEditor == undefined || ! isOpenEditor } );
		},

		toggleCheckbox: function() {
			this.model.set( { checked: ! this.model.get( 'checked' ) }, { silent: true } );
		},

		doSaveData: function() {
			this.bindModel();
			this.save();
		},

		bindModel: function(  ) {
			this.model.set( { userTeamMemberName: this.$( '.user-team-member-name' ).val() }, { silent: true } );
			this.model.set( { teamMemberTypeId: this.$( 'input[name=teamMemberTypeId]:checked' ).val() }, { silent: true } );
		},

		save: function() {
			this.model.save()
				.done( _.bind( this.onSaveSuccess, this ) )
				.fail( _.bind( this.onSaveError, this ) );
		},

		onSaveSuccess: function() {
			this.closeEditor();
			showUIMessage_Notification( this.model.get( 'translationDTO' )[ 'dataSavedSuccessfully' ] );

			var massSelector = mass_checker.getMassChecker();
			massSelector.registerUnselected( this.massSelectorCss + this.model.get( 'entryId' ), Backbone.JPhoto.imageFolder() );

			if ( this.onEdit != undefined ) {
				this.onEdit( this.model ); // TODO: save and edit are the same
			}
		},

		onSaveError: function( response ) {
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

		onEntryDelete: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.deleteEntry();
		},

		onCanNotDeleteClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			showUIMessage_Information( this.model.get( 'userTeamMemberName' ) + ' ' + this.model.get( 'translationDTO' ).entryInfoIconTitleCanNotDelete );
		},

		onToggleCheckbox: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.toggleCheckbox();
		},

		onDataChange: function() {
			this.model.set( { hasUnsavedChanged: true }, { silent: true } );

			this.$( '.user-team-member-cancel' ).hide();

			this.$( '.user-team-member-save' ).show();
			this.$( '.user-team-member-discard-changes' ).show();
		},

		onCloseEditorWithoutChanges: function() {

			if ( this.model.get( 'entryId' ) > 0 ) {
				this.closeEditor(); // close editor of an existing team member
				return;
			}

			this.model.destroy(); // cancel creating of new team member, { silent: true } no instance on the server
		},

		removeView: function() {
			this.remove();
		},

		deleteEntry: function() {

			if ( this.model.get( 'teamMemberPhotosQty' ) > 0 ) {
				alert( this.model.get( 'translationDTO' )[ 'deleteEntryImpossible' ] );
				return;
			}

			if ( ! confirm( this.model.get( 'userTeamMemberName' ) + ': ' + this.model.get( 'translationDTO' )[ 'deleteEntryConfirmation' ] ) ) {
				return;
			}

			this.model.destroy();
			if ( this.onDelete != undefined ) {
				this.onDelete( this.model.get( 'entryId' ) );
			}
		},

		onSaveDataClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			if ( this.model.get( 'hasUnsavedChanged' ) && ! confirm( this.model.get( 'userTeamMemberName' ) + ': ' + this.model.get( 'translationDTO' )[ 'saveChangesConfirmation' ] ) ) {
				return;
			}

			this.doSaveData();
		},

		onDiscardEditedData: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			if ( this.model.get( 'hasUnsavedChanged' ) && ! confirm( this.model.get( 'userTeamMemberName' ) + ': ' + this.model.get( 'translationDTO' )[ 'discardChangesConfirmation' ] ) ) {
				return;
			}

			if ( this.model.get( 'entryId' ) > 0 ) {
				this.closeEditor(); // discard unsaved changes of an existing team member
				return;
			}

			this.model.destroy(); // discard unsaved changes of new team member
		},

		closeEditor: function() {
			this.model.set( { openEditor: false } );
		}
	});

	return { EntryListView: EntryListView };
});