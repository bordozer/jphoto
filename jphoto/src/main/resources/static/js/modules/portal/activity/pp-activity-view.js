define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/activity/templates/pp-activity-template.html'
		], function ( Backbone, $, _, template ) {

	'use strict';

	var translator = Backbone.JPhoto.translateAll( {
		title: 'Portal page: Last activity'
	} );

	var PortalPageActivityStreamView = Backbone.View.extend( {

		template:_.template( template ),

		initialize: function() {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.template( {
				 model: modelJSON
				 , activityStreamEntryDTOs: modelJSON.activityStreamEntryDTOs
				 , translator: translator
			 } ) );

			return this.$el;
		}
} );

	return { PortalPageActivityStreamView: PortalPageActivityStreamView };
} );