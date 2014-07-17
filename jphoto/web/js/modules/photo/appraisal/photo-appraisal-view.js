define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/appraisal/templates/photo-appraisal-form.html"
		, "text!modules/photo/appraisal/templates/photo-appraisal-results.html"
		, "text!modules/photo/appraisal/templates/photo-appraisal-inaccessible.html"
		], function ( Backbone, $, _,  appraisalFormTemplate, appraisalResultTemplate, appraisalInaccessibleTemplate ) {

	'use strict';

	var PhotoAppraisalFormView = Backbone.View.extend( {

		appraisalFormTemplate:_.template( appraisalFormTemplate ),

		events: {
			"change .photo-appraisal-category-select": "onCategoryChange",
			"click .custom-marks-button": "onCustomMarksButtonClick",
			"click .excellent-button": "onExcellentButtonClick",
			"click .good-button": "onGoodButtonClick"
		},

		initialize: function() {
			this.render();
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.appraisalFormTemplate( modelJSON ) );
			return this;
		},

		categoryChange: function( control ) {
//			console.log( 'category is changed: ', $( ':selected', control ).val(), ', class: ', $( control ).attr( 'class' ) );
		},

		doAppraiseThePhoto: function() {
			this.bindModel();
			this.saveAppraisal();
		},

		appriseThePhoto_Excellent: function() {
			this.bindModel( this.model.get( 'photoAppraisalForm' )[ 'userHighestPositiveMarkInGenre' ] );
			this.saveAppraisal();
		},

		appriseThePhoto_Good: function() {
			this.bindModel( this.model.get( 'photoAppraisalForm' )[ 'goodButtonMark' ] );
			this.saveAppraisal();
		},

		bindModel: function( setMark ) {
			var model = this.model;

			_.each( model.get( 'photoAppraisalForm' )[ 'appraisalSections' ], function( section ) {

				var number = section[ 'number' ];
				var categoryId = this.$( '.photo-appraisal-category-' + number ).val();
				var mark = setMark != undefined ? setMark : this.$( '.photo-appraisal-mark-' + number ).val();

				section[ 'selectedCategoryId' ] = categoryId;
				section[ 'selectedMark' ] = mark;

			}, this );
		},

		saveAppraisal: function() {
			this.model.save()
				.done( _.bind( this.onAppraisalSave, this ) )
				.fail( _.bind( this.onAppraisalSaveError, this ) );
		},

		onAppraisalSave: function( response ){
			showUIMessage_Notification( this.model.get( 'appraisalSaveCallbackMessage' ) );
		},

		onAppraisalSaveError: function( response ) {

			if ( response.status === 422 || response.status === 500 ) {
				var errorText = '';
				var errors = response[ 'responseJSON' ];
				for ( var i = 0; i < errors.length; i++ ) {
					errorText += errors[ i ][ 'defaultMessage' ] + "\n";
				}

				showUIMessage_Error( errorText );

				this.reloadForUser( this.model.get( 'currentUserId' ) );
			}
		},

		reloadForUser: function( userId ) {
			this.model.set( { userId: userId } );
			this.model.refresh();
		},

		onCategoryChange: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();
			this.categoryChange( evt.target );
		},

		onCustomMarksButtonClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();
			this.doAppraiseThePhoto();
		},

		onExcellentButtonClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();
			this.appriseThePhoto_Excellent();
		},

		onGoodButtonClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();
			this.appriseThePhoto_Good();
		}
	});

	var PhotoAppraisalResultView = Backbone.View.extend( {

		appraisalResultTemplate:_.template( appraisalResultTemplate ),

		initialize: function() {
			this.render();
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.appraisalResultTemplate( modelJSON ) );
			return this;
		}
	});

	var PhotoAppraisalInaccessibleView = Backbone.View.extend( {

		appraisalInaccessibleTemplate:_.template( appraisalInaccessibleTemplate ),

		initialize: function() {
			this.render();
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.appraisalInaccessibleTemplate( modelJSON ) );
			return this;
		}
	});

	var PhotoAppraisalCompositeView = Backbone.View.extend( {

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
//			this.listenTo( this.model, "onPhotoAppraisal", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			if ( ! modelJSON[ 'userCanAppraiseThePhoto' ] ) {
				this.renderView( new PhotoAppraisalInaccessibleView( {
					model: this.model
				}) );
				return;
			}

			if ( modelJSON[ 'userHasAlreadyAppraisedPhoto' ] ) {
				this.renderView( new PhotoAppraisalResultView( {
					model: this.model
				}) );
				return;
			}

			this.renderView( new PhotoAppraisalFormView( {
				model: this.model
			}) );
		},

		renderView: function( view ) {

			var container = $( '<div></div>' );

			var header = $( "<div class='block-background block-border' style='padding: 7px; border-radius: 7px 7px 0 0'></div>" );
			header.html( "<div class='separatorInfo base-font-color'>" + this.model.get( 'appraisalBlockTitle' ) + "</div>" );
			container.html( header );

			var body = $( "<div class='floatleft block-background block-border' style='width: 418px; height: auto;'></div>" );
			body.html( view.render().$el );

			container.append( body );

			this.$el.html( container );

			return this;
		}
	});

	return { PhotoAppraisalCompositeView: PhotoAppraisalCompositeView, PhotoAppraisalFormView: PhotoAppraisalFormView, PhotoAppraisalResultView: PhotoAppraisalResultView };
});