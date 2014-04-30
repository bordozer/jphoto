define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/appraisal/templates/photo-appraisal-form.html"
		, "text!modules/photo/appraisal/templates/photo-appraisal-results.html"
		], function ( Backbone, $, _,  appraisalFormTemplate, appraisalResultTemplate ) {

	'use strict';

	var PhotoAppraisalFormView = Backbone.View.extend( {

		appraisalFormTemplate:_.template( appraisalFormTemplate ),

		events: {
			"click .appraise-button": "onAppraise",
			"click .appraise-button-max": "onAppraiseMax"
		},

		initialize: function() {
			this.render();
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.appraisalFormTemplate( modelJSON[ 'photoAppraisalForm' ] ) );
			return this;
		},

		appraise: function() {
			alert( 'appraise' );
		},

		appraiseMax: function() {
			alert( 'appraise max' );
		},

		onAppraise: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();
			this.appraise();
		},

		onAppraiseMax: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();
			this.appraiseMax();
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

	var PhotoAppraisalCompositeView = Backbone.View.extend( {

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
			this.listenTo( this.model, "onPhotoAppraisal", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			if ( modelJSON[ 'userHasAlreadyAppraisedPhoto' ] ) {
				this.renderView( new PhotoAppraisalResultView( {
					model: this.model
				}) );
			} else {
				this.renderView( new PhotoAppraisalFormView( {
					model: this.model
				}) );
			}
		},

		renderView: function( view ) {
			this.$el.html( view.render().$el );
			return this;
		}
	});

	return { PhotoAppraisalCompositeView: PhotoAppraisalCompositeView, PhotoAppraisalFormView: PhotoAppraisalFormView, PhotoAppraisalResultView: PhotoAppraisalResultView };
});