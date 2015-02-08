define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/photos/latest/templates/latest-photos-template.html'
			, '/js/lib/slick-master/slick.min.js'
		], function ( Backbone, $, _, template, slick ) {

	'use strict';

	var translator = Backbone.JPhoto.translateAll( {
		theLatestPhotos: 'Portal page: The latest uploaded photos'
	} );

	var PortalPageLatestPhotosView = Backbone.View.extend( {

		template:_.template( template ),

		/*events: {
			'click .slide-left': '_onSlideLeft'
			, 'click .slide-right': '_onSlideRight'
		},*/

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
//			this.$( ".latest-photos-container" ).
			$( '.latest-photos-container' ).slick( {
				infinite: false
				, slidesToShow: 3
				, slidesToScroll: 3
				, variableWidth: false
//				, dots: true
			});
		}/*,

		_onSlideLeft: function() {
			this.$( '.latest-photos-container' ).trigger( 'backward' );
		},

		_onSlideRight: function() {
			this.$( '.latest-photos-container' ).trigger( 'forward' );
		}*/
} );

	return { PortalPageLatestPhotosView: PortalPageLatestPhotosView };
} );