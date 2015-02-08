define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/templates/portal-page-template.html'
		], function ( Backbone, $, _, template ) {

	'use strict';

	var translator = Backbone.JPhoto.translateAll( {
		photoOfTheDay: 'Portal page: The photos of the day'
		, photoGenres: 'Portal page: Photo genres'
		, theBestWeeksAuthors: 'Portal page: The best weeks authors'
		, theBestMonthAuthors: 'Portal page: The best month authors'
		, activityStream: 'Portal page: Activity stream'
	} );

	var PortalPageView = Backbone.View.extend( {

		template:_.template( template ),

		initialize: function() {

		},

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.template( {
				 model: modelJSON
				, translator: translator
			 } ) );
		}
} );

	return { PortalPageView: PortalPageView };
} );