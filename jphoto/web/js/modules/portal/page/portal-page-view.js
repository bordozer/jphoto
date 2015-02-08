define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/page/templates/portal-page-template.html'
			, 'modules/portal/photos/latest/latest-photos-model'
			, 'modules/portal/photos/latest/latest-photos-view'
			, 'modules/portal/photos/best/best-photos-model'
			, 'modules/portal/photos/best/best-photos-view'
		], function ( Backbone, $, _, template
		, LatestPhotosModel, LatestPhotosView
		, BestPhotosModel, BestPhotosView
		) {

	'use strict';

	var translator = Backbone.JPhoto.translateAll( {
		photoGenres: 'Portal page: Photo genres'
		, theBestWeeksAuthors: 'Portal page: The best weeks authors'
		, theBestMonthAuthors: 'Portal page: The best month authors'
		, activityStream: 'Portal page: Activity stream'
	} );

	var PortalPageView = Backbone.View.extend( {

		template:_.template( template ),

		initialize: function() {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.template( {
				 model: modelJSON
				, translator: translator
			 } ) );

			this.renderLatestPhotos();

			this.renderBestPhotos();
		},

		renderLatestPhotos: function() {
			var container = this.$( '.latest-photos-container' );

			var model = new LatestPhotosModel.PortalPageLatestPhotosModel();
			var view = new LatestPhotosView.PortalPageLatestPhotosView( { model: model, el: container } );

			container.html( view.$el );
		},

		renderBestPhotos: function() {
			var container = this.$( '.best-photos-container' );

			var model = new BestPhotosModel.PortalPageBestPhotosModel();
			var view = new BestPhotosView.PortalPageBestPhotosView( { model: model, el: container } );

			container.html( view.$el );
		}
} );

	return { PortalPageView: PortalPageView };
} );