define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/genres/templates/pp-genres-template.html'
		], function ( Backbone, $, _, template ) {

	'use strict';

	var translator = Backbone.JPhoto.translateAll( {
		genresTitle: 'Portal page: categories'
		, totalPhotos: 'Portal page: totalPhotos'
	} );

	var PortalPageGenresView = Backbone.View.extend( {

		template:_.template( template ),

		initialize: function() {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.template( {
				 model: modelJSON
				 , genreDTOs: modelJSON.genreDTOs
				 , totalPhotos: modelJSON.totalPhotos
				 , translator: translator
			 } ) );

			return this.$el;
		}
} );

	return { PortalPageGenresView: PortalPageGenresView };
} );