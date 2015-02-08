define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/page/templates/portal-page-template.html'
			, 'modules/portal/photos/latest/latest-photos'
		], function ( Backbone, $, _, template, latestPhotos ) {

	'use strict';

	var translator = Backbone.JPhoto.translateAll( {
		theLatestPhotos: 'Portal page: The latest uploaded photos'
		, photoOfTheDay: 'Portal page: The photos of the day'
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
		},

		renderLatestPhotos: function() {
			latestPhotos( this.$( '.latest-photos-container' ) );
		}
} );

	return { PortalPageView: PortalPageView };
} );