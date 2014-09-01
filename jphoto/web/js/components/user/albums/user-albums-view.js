define( ["backbone", "jquery", "underscore", "mass_checker"
		, "components/user/albums/user-albums-model"
		, "text!components/user/albums/templates/header-template.html"
		, "text!components/user/albums/templates/album-info-template.html"
		, "text!components/user/albums/templates/album-edit-template.html"
		], function ( Backbone, $, _, mass_checker, Model, headerTemplate, entryInfoTemplate, entryEditorTemplate ) {

	'use strict';

	var AlbumListView = Backbone.View.extend( {

		headerTemplate:_.template( headerTemplate ),

		events: {
			"click .create-new-user-album-link": "onCreateNewAlbum"
		},

		initialize: function( options ) {

			this.onEdit = options.onEdit;
			this.onDelete = options.onDelete;

			this.listenTo( this.model, "add", this.renderEntry );

			var model = this.model;
			var el = this.$el;
			this.model.fetch( { cache: false, success: function (  ) {
				_.each( model.selectedAlbumIds, function( memberId ) {
					$( '.user-album-checkbox-' + memberId, el ).attr( 'checked', 'checked' );
				} );
			} } );
		},

		renderHeader: function () {
			var modelJSON = this.model.toJSON();

			var translationsDTO = this.model[ 'translationDTO' ];
			modelJSON[ 'headerTitleCreateNewAlbumButtonTitle' ] = translationsDTO[ 'headerTitleCreateNewAlbumButtonTitle' ];

			this.$el.html( this.headerTemplate( modelJSON ) );
		},

		renderEntry: function ( album ) {
			album.set( { translationDTO: this.model[ 'translationDTO' ] } );
			var view = this;

			var massSelectorCss = this.model.groupSelectionClass;
			var albumView = new AlbumView( {
				model: album
				, massSelectorCss: massSelectorCss
				, onEdit: view.onEdit
				, onDelete: view.onDelete
			} );

			this.$el.append( albumView.render().$el );

			// TODO: duplicates!
			if( massSelectorCss != '' ) {
				var massSelector = mass_checker.getMassChecker();
				var css = massSelectorCss + album.get( 'userAlbumId' );
				massSelector.registerUnselected( css, Backbone.JPhoto.imageFolder() );
			}
		},

		createAlbum: function() {

			var model = this.model;
			var translationDTO = this.model[ 'translationDTO' ];

			var albumModel = new Model.AlbumModel( {
				  userAlbumId: 0
				, userId: model.userId
				, albumName: ''
				, checked: true
				, openEditor: true
				, translationDTO: translationDTO
			} );
			console.log( albumModel );
			this.model.add( albumModel );
		},

		deleteEntry: function( album ) {
			var albumId = album.get( 'userAlbumId' );
			this.model.remove( albumId );
			this.model.onDelete( albumId );
		},

		onCreateNewAlbum: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.createAlbum();
		}
	});


	var AlbumView = Backbone.View.extend({

		infoViewTemplate:_.template( entryInfoTemplate ),
		editorTemplate:_.template( entryEditorTemplate ),

		initialize: function( options ) {
			this.massSelectorCss = options.massSelectorCss;

			this.onEdit = options.onEdit;
			this.onDelete = options.onDelete;

			this.listenTo( this.model, "sync", this.render );
			this.listenTo( this.model, "change", this.render );

			this.listenTo( this.model, "destroy", this.removeView );
		},

		events: {
			"click .user-album-info": "onToggleInfo",
			"click .user-album-edit": "onToggleEditor",
			"click .user-album-delete": "onEntryDelete",
			"click .user-album-can-not-delete": "onCanNotDeleteClick",
			"change .user-album-checkbox": "onToggleCheckbox",

			"keydown .user-album-data": "onDataChange",

			"click .user-album-cancel-editing": "onCloseEditorWithoutChanges",
			"click .user-album-save": "onSaveDataClick",
			"click .user-album-discard-changes": "onDiscardEditedData"
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			modelJSON[ 'translationsDTO' ] = this.model.get( 'translationDTO' );

			this.$el.html( "" );

			this.renderHeader( modelJSON );

			if ( this.model.get( 'openEditor' ) ) {
				this.$el.append( this.editorTemplate( modelJSON ) );
				this.$( '.user-album-name' ).focus();
				this.$( '.user-album-name' ).select();
			} else if ( this.model.get( 'openInfo' ) ) {
				this.$el.append( this.infoViewTemplate( modelJSON ) );
			}

			// TODO: duplicates!
			if( this.massSelectorCss != '' ) {
				var massSelector = mass_checker.getMassChecker();
				var css = this.massSelectorCss + this.model.get( 'userAlbumId' );
				massSelector.registerUnselected( css, Backbone.JPhoto.imageFolder() );
			}

			return this;
		},

		renderHeader: function( modelJSON ) {
			var entryDiv = $( "<div class='user-album-list-entry'></div>" );

			if( this.massSelectorCss == '' ) {
				entryDiv.append( "<input type='checkbox' name='userAlbumsIds' class='user-album-checkbox user-album-checkbox-" + modelJSON.userAlbumId + "'"
				 + " value='" + modelJSON.userAlbumId + "' " + ( modelJSON.checked ? "checked='checked'" : "" ) + " />" );
			}

			if( this.massSelectorCss != '' ) {
				entryDiv.append( "<img class='" + 'mass-selector-icon-' + this.massSelectorCss + modelJSON.userAlbumId + "' width='16' height='16' /> " );
			}

			if( modelJSON.userAlbumId > 0 && ! modelJSON.openEditor ) {
				entryDiv.append( "<a href='#' class='user-album-info' onclick='return false;' title='" + modelJSON.albumName + "'>" + modelJSON.albumName + "</a> - "
										 + modelJSON.albumPhotosQty + ' ' + modelJSON.translationsDTO.listEntryPhotos );
			}

			if( modelJSON.userAlbumId > 0 && modelJSON.openEditor ) {
				entryDiv.append( modelJSON.albumName + " - " + modelJSON.albumPhotosQty + ' ' + modelJSON.translationsDTO.listEntryPhotos );
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
			this.model.set( { albumName: this.$( '.user-album-name' ).val() }, { silent: true } );
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
			massSelector.registerUnselected( this.massSelectorCss + this.model.get( 'userAlbumId' ), Backbone.JPhoto.imageFolder() );

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

			showUIMessage_Information( this.model.get( 'albumName' ) + ' ' + this.model.get( 'translationDTO' ).entryInfoIconTitleCanNotDelete );
		},

		onToggleCheckbox: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.toggleCheckbox();
		},

		onDataChange: function() {
			this.model.set( { hasUnsavedChanged: true }, { silent: true } );

			this.$( '.user-album-cancel-editing' ).hide();

			this.$( '.user-album-save' ).show();
			this.$( '.user-album-discard-changes' ).show();
		},

		onCloseEditorWithoutChanges: function() {

			if ( this.model.get( 'userAlbumId' ) > 0 ) {
				this.closeEditor(); // close editor of an existing album
				return;
			}

			this.model.destroy(); // cancel creating of new album, { silent: true } no instance on the server
		},

		removeView: function() {
			this.remove();
		},

		deleteEntry: function() {

			if ( this.model.get( 'albumPhotosQty' ) > 0 ) {
				alert( this.model.get( 'translationDTO' )[ 'deleteEntryImpossible' ] );
				return;
			}

			if ( ! confirm( this.model.get( 'albumName' ) + ': ' + this.model.get( 'translationDTO' )[ 'deleteEntryConfirmation' ] ) ) {
				return;
			}

			this.model.destroy();
			if ( this.onDelete != undefined ) {
				this.onDelete( this.model.get( 'userAlbumId' ) );
			}
		},

		onSaveDataClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			if ( this.model.get( 'hasUnsavedChanged' ) && ! confirm( this.model.get( 'albumName' ) + ': ' + this.model.get( 'translationDTO' )[ 'saveChangesConfirmation' ] ) ) {
				return;
			}

			this.doSaveData();
		},

		onDiscardEditedData: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			if ( this.model.get( 'hasUnsavedChanged' ) && ! confirm( this.model.get( 'albumName' ) + ': ' + this.model.get( 'translationDTO' )[ 'discardChangesConfirmation' ] ) ) {
				return;
			}

			if ( this.model.get( 'userAlbumId' ) > 0 ) {
				this.closeEditor(); // discard unsaved changes of an existing album
				return;
			}

			this.model.destroy(); // discard unsaved changes of new album
		},

		closeEditor: function() {
			this.model.set( { openEditor: false } );
		}
	});

	return { AlbumListView: AlbumListView };
});