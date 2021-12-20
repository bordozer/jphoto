define( [ 'backbone', 'jquery', 'underscore'
			, 'text!modules/portal/page/templates/portal-page-template.html'
			, 'modules/portal/photos/latest/latest-photos-model'
			, 'modules/portal/photos/latest/latest-photos-view'
			, 'modules/portal/photos/best/best-photos-model'
			, 'modules/portal/photos/best/best-photos-view'
			, 'modules/portal/authors/best-authors-model'
			, 'modules/portal/authors/best-authors-view'
			, 'modules/portal/genres/pp-genres-model'
			, 'modules/portal/genres/pp-genres-view'
			, 'modules/portal/activity/pp-activity-model'
			, 'modules/portal/activity/pp-activity-view'
		], function ( Backbone, $, _, template
		, LatestPhotosModel, LatestPhotosView
		, BestPhotosModel, BestPhotosView
		, BestAuthorsModel, BestAuthorsView
		, GenresModel, GenresView
		, ActivityStreamModel, ActivityStreamView
		) {

	'use strict';

	var translator = Backbone.JPhoto.translateAll( {
		activityStream: 'Portal page: Activity stream'
	} );

	var PortalPageView = Backbone.View.extend( {

		template:_.template( template ),

		initialize: function( options ) {
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

			this.renderCategories();

			this.renderBestWeekAuthors();

			this.renderBestMonthAuthors();

			this.renderActivityStream();
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
		},

		renderCategories: function() {
			var container = this.$( '.genres-container' );

			var model = new GenresModel.PortalPageGenresModel();
			var view = new GenresView.PortalPageGenresView( { model: model, el: container } );

			container.html( view.$el );
		},

		renderBestWeekAuthors: function() {
			var container = this.$( '.best-week-authors-container' );

			var model = new BestAuthorsModel.PortalPageBestAuthorsModel( { dateFrom: this.model.get( 'weekBegin' ), dateTo: this.model.get( 'weekEnd' ) } );
			var view = new BestAuthorsView.PortalPageBestAuthorsView( { model: model, el: container } );

			container.html( view.$el );
		},

		renderBestMonthAuthors: function() {
			var container = this.$( '.best-month-authors-container' );

			var model = new BestAuthorsModel.PortalPageBestAuthorsModel( { dateFrom: this.model.get( 'monthBegin' ), dateTo: this.model.get( 'monthEnd' ) } );
			var view = new BestAuthorsView.PortalPageBestAuthorsView( { model: model, el: container } );

			container.html( view.$el );
		},

		renderActivityStream: function() {
			var container = this.$( '.activity-stream-container' );

			var model = new ActivityStreamModel.PortalPageActivityStreamModel();
			var view = new ActivityStreamView.PortalPageActivityStreamView( { model: model, el: container } );

			container.html( view.$el );
		}
} );

	return { PortalPageView: PortalPageView };
} );