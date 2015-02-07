define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/templates/portal-page-template.html'
		], function ( Backbone, $, _, template ) {

	'use strict';

	var PortalPageView = Backbone.View.extend( {

		template:_.template( template ),

		initialize: function() {

		},

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.template( modelJSON ) );
		}
} );

	return { PortalPageView: PortalPageView };
} );