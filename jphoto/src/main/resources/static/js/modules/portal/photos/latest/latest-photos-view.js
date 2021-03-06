define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/photos/latest/templates/latest-photos-template.html'
			, '/js/lib/slick-master/src/slick.min.js'
		], function ( Backbone, $, _, template, slick ) {

	'use strict';

	var translator = Backbone.JPhoto.translateAll( {
		theLatestPhotos: 'Portal page: The latest uploaded photos'
	} );

	var PortalPageLatestPhotosView = Backbone.View.extend( {

		template:_.template( template ),

		slidesToShow: 8,

		initialize: function() {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.template( {
				 model: modelJSON
				 , photoDTOs: modelJSON.latestPhotosDTOs
				, translator: translator
			 } ) );

			this.applySmoothScrolling();

			return this.$el;
		},

		applySmoothScrolling: function() {

			this.$( '.latest-photos-container' ).slick( {
				infinite: true
				, slidesToShow: this._slidesToShow()
				, slidesToScroll: 3
				, variableWidth: false
				, dots: false
				, autoplay: true
				, autoplaySpeed: 5000
			});
		},

		_slidesToShow: function() {
			var photos = this.model.get( 'latestPhotosDTOs' ).length;
			return photos > this.slidesToShow ? this.slidesToShow : photos;
		}
} );

	return { PortalPageLatestPhotosView: PortalPageLatestPhotosView };
} );