define( ["backbone", "jquery", "underscore"
		, "modules/admin/restriction/restriction-history/restriction-history-view"
		, "text!modules/admin/restriction/list/templates/restriction-list-filter.html"
		], function ( Backbone, $, _, HistoryView, template ) {

	'use strict';

	var RestrictionListFilterView = Backbone.View.extend( {

		template:_.template( template ),

		events: {
			"click .apply-filter-restriction-button" : "onApplyClick"
		},

		initialize: function() {
			this.listenTo( this.model, "sync", this.renderSearchResult );
//			this.listenTo( this.model, "add", this.renderSearchResult );

			this.renderSearchForm();
			this.clearSearchResult();

			this.model.fetch( { cache: false } );
		},

		renderSearchForm: function() {
			var modelJSON = this.model.toJSON();

			modelJSON.restrictionTypes = this.model.restrictionTypes;
			modelJSON.restrictionStatuses = this.model.restrictionStatuses;
			modelJSON.translations = this.model.translations;

			this.$el.html( this.template( modelJSON ) );
		},

		renderSearchResult: function() {
			var historyEntryTranslations = this.model.historyEntryTranslations;

			var $searchResultContainer = this.$( ".search-result-container" );

			this.model.each( function( entryModel ) {
				var restrictionHistoryEntryView = new HistoryView.RestrictionHistoryEntryView( {
					model: entryModel
					, historyEntryTranslations: historyEntryTranslations
				} );

				$searchResultContainer.append( restrictionHistoryEntryView.render().$el );
			});
		},

		clearSearchResult: function() {
			this.$( ".search-result-container" ).html( '' );
		},

		onApplyClick: function ( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			var selectedRestrictionTypeIds = [];
			$( ".restriction-type:checked" ).each( function () {
				selectedRestrictionTypeIds.push( this.value );
			} );

			this.clearSearchResult();

			this.model.fetch( { cache: false } );
		}
	} );

	return { RestrictionListFilterView:RestrictionListFilterView };
});