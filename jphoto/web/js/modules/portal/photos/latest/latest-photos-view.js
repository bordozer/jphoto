define( [ 'backbone', 'jquery', 'underscore', '/js/lib/smoothdiv/jquery.mousewheel.min.js', 'lib/smoothdiv/jquery.smoothDivScroll-1.2'
			, 'text!modules/portal/photos/latest/templates/latest-photos-template.html'
		], function ( Backbone, $, _, mousewheel, smoothDivScroll, template ) {

	'use strict';

	/*var translator = Backbone.JPhoto.translateAll( {
		photoOfTheDay: ''
	} );*/

	var PortalPageLatestPhotosView = Backbone.View.extend( {

		template:_.template( template ),

		initialize: function() {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {
			var modelJSON = this.model.toJSON();
//			console.log( modelJSON );

			this.$el.html( this.template( {
				 model: modelJSON
				 , photoDTOs: modelJSON.latestPhotosDTOs
//				, translator: translator
			 } ) );


			setTimeout( this.applySmoothScrolling, 1000 );
		},

		applySmoothScrolling: function() {
			this.$( ".latest-photos-container" ).smoothDivScroll( {
				mousewheelScrolling: false,
				manualContinuousScrolling: true,
				visibleHotSpotBackgrounds: "always",
				autoScrollingMode: "onstart"
			} );
		}
} );

	return { PortalPageLatestPhotosView: PortalPageLatestPhotosView };
} );