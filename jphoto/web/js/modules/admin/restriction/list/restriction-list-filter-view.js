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
//			this.listenTo( this.model, "sync", this.renderSearchResult );

			this.renderSearchForm();

			this.model.fetch( { reset: true, cache: false } );
		},

		renderSearchForm: function() {
			var modelJSON = this.model.toJSON();

			modelJSON.restrictionTypes = this.model.restrictionTypes;
			modelJSON.translations = this.model.translations;

			this.$el.html( this.template( modelJSON ) );
		},

		renderSearchResult: function() {
			var model = this.model;
			var historyEntryTranslations = this.historyEntryTranslations;

			var $searchResultContainer = this.$( ".search-result-container" );
			$searchResultContainer.html( '' );

			_.each( this.model.get( 'searchResultEntryDTOs' ), function( searchResultEntryDTO ) {
				var entryContainer = $( "<div></div>" );
				var restrictionHistoryEntryView = new HistoryView.RestrictionHistoryEntryView( {
					model: searchResultEntryDTO
					, el: entryContainer
					, historyEntryTranslations: historyEntryTranslations
				} );

				$searchResultContainer.append( restrictionHistoryEntryView.render().$el );
			});
		},

		onApplyClick: function ( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			var selectedRestrictionTypeIds = [];
			$( ".restriction-type:checked" ).each( function () {
				selectedRestrictionTypeIds.push( this.value );
			} );

			this.model.save( { selectedRestrictionTypeIds: selectedRestrictionTypeIds } );

			/*var entryContainer = this.searchResultContainer;
			var model = this.model;
			var historyEntryTranslations = this.historyEntryTranslations;
			this.searchResultContainer.html( "" );

			var restrictionHistoryEntryView = new HistoryView.RestrictionHistoryEntryView( {
				model: model
				, el: entryContainer
				, historyEntryTranslations: historyEntryTranslations
			} );*/

			/*var selectedRestrictionTypeIds = [];
			$( ".restriction-type:checked" ).each( function () {
				selectedRestrictionTypeIds.push( this.value );
			} );

			var filterForm = { restrictionTypeIds: selectedRestrictionTypeIds };

			this.model.set( { filterForm: filterForm } );*/


//			this.model.fetch( { reset: true, cache: false } );
		}
	} );

	return { RestrictionListFilterView:RestrictionListFilterView };
});