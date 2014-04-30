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
			"click .appraise-button": "onAppraise",
			"click .appraise-button-max": "onAppraiseMax"
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
			console.log( 'category is changed: ', $( ':selected', control ).val(), ', class: ', $( control ).attr( 'class' ) );
		},

		doAppraiseThePhoto: function() {
			this.bindModel();
			this.saveAppraisal();
		},

		doAppraiseThePhotoWithMaxMarks: function() {
			this.bindModel();
			this.saveAppraisal();
		},

		bindModel: function() {
			var model = this.model;

//			console.log( model.get( 'photoAppraisalForm' )[ 'appraisalSections' ] );

			_.each( model.get( 'photoAppraisalForm' )[ 'appraisalSections' ], function( section ) {

				var number = section[ 'number' ];
				var categoryId = this.$( '.photo-appraisal-category-' + number ).val();
				var mark = this.$( '.photo-appraisal-mark-' + number ).val();

				section[ 'selectedCategoryId' ] = categoryId;
				section[ 'selectedMark' ] = mark;

//				console.log( number, ': ', categoryId, '', mark );
			}, this );
		},

		saveAppraisal: function() {
			this.model.save()
				.done( _.bind( this.onAppraisalSave, this ) )
				.fail( _.bind( this.onAppraisalSaveError, this ) );
		},

		onAppraisalSave: function( response ){
			showUIMessage_Notification( 'Your photo appraisal has been saved successfully' );
		},

		onAppraisalSaveError: function( response ){

			if ( response.status === 422 || response.status === 500 ) {
				var errorText = '';
				var errors = response[ 'responseJSON' ];
				for ( var i = 0; i < errors.length; i++ ) {
					errorText += errors[ i ][ 'defaultMessage' ] + "\n";
				}

				showUIMessage_Error( errorText );
			}
		},

		onCategoryChange: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();
			this.categoryChange( evt.target );
		},

		onAppraise: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();
			this.doAppraiseThePhoto();
		},

		onAppraiseMax: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();
			this.doAppraiseThePhotoWithMaxMarks();
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
			this.listenTo( this.model, "onPhotoAppraisal", this.render );
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
			this.$el.html( view.render().$el );
			return this;
		}
	});

	return { PhotoAppraisalCompositeView: PhotoAppraisalCompositeView, PhotoAppraisalFormView: PhotoAppraisalFormView, PhotoAppraisalResultView: PhotoAppraisalResultView };
});