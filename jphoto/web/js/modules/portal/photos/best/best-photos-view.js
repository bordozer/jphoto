define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/photos/best/templates/best-photos-template.html'
			, '/js/lib/bxslider/jquery.bxslider.min.js'
		], function ( Backbone, $, _, template, bxslider ) {

	'use strict';

	var translator = Backbone.JPhoto.translateAll( {
		bestPhotos: 'Portal page: The photos of the day'
	} );

	var PortalPageBestPhotosView = Backbone.View.extend( {

		template:_.template( template ),

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

			this.applySlider();

			return this.$el;
		},

	 applySlider: function () {
		 // http://bxslider.com/options

		var slider = this.$( ".best-photos-container" ).bxSlider( {
			pagerCustom: '#bx-pager'
			, auto: true
			, mode: 'fade' /* 'horizontal', 'vertical', 'fade' */
//			, ticker: true
//  		, speed: 2000
//			, minSlides: 4
//			, maxSlides: 4
			, slideWidth: 500
			, pause: 5000
			, speed: 500
//			, slideMargin: 10
//  		, autoControls: true
//			, adaptiveHeight: true
//			, captions: true
//			, onSlideBefore: _onSlideBefore
		} );

		/*function _onSlideBefore( $slideElement, oldIndex, newIndex ) {
			var width = $slideElement.data( 'width' );
			slider.reloadSlider( {
				slideWidth: width
			});
		 }*/
	}
} );

	return { PortalPageBestPhotosView: PortalPageBestPhotosView };
} );