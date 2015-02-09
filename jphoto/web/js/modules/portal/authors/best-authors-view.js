define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/authors/templates/best-authors-template.html'
			, '/js/lib/bxslider/jquery.bxslider.min.js'
		], function ( Backbone, $, _, template, bxslider ) {

	'use strict';

	var PortalPageBestAuthorsView = Backbone.View.extend( {

		template:_.template( template ),

		initialize: function() {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.template( {
				 model: modelJSON
				 , authorDTOs: modelJSON.authorDTOs
				 , title: modelJSON.title
			 } ) );

			return this.$el;
		}
} );

	return { PortalPageBestAuthorsView: PortalPageBestAuthorsView };
} );