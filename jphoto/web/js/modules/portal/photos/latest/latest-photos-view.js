define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/photos/latest/templates/latest-photos-template.html'
		], function ( Backbone, $, _, template ) {

	'use strict';

	var translator = Backbone.JPhoto.translateAll( {
		theLatestPhotos: 'Portal page: The latest uploaded photos'
	} );

	var PortalPageLatestPhotosView = Backbone.View.extend( {

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

			this.applySmoothScrolling();

			return this.$el;
		},

		applySmoothScrolling: function() {
//			this.$( ".latest-photos-container" ).;
		}
} );

	return { PortalPageLatestPhotosView: PortalPageLatestPhotosView };
} );