define( ["backbone", "jquery", "underscore", "mass_checker"
		, "modules/admin/restriction/restriction-history/restriction-history-view"
		, "text!modules/admin/restriction/list/templates/restriction-list-filter.html"
		, "components/user-picker/user-picker-model"
		, "components/user-picker/user-picker-view"
		], function ( Backbone, $, _, mass_checker, HistoryView, template, UserPickerModel, UserPickerView ) {

	'use strict';

	var RestrictionListFilterView = Backbone.View.extend( {

		template:_.template( template ),

		events: {
			"click .apply-filter-restriction-button" : "onApplyClick"
		},

		initialize: function() {
			this.listenTo( this.model, "sync", this.renderSearchResult );

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

			var massSelector = mass_checker.getMassChecker();
			massSelector.registerUnselected( "restriction-type", "/images" );
			massSelector.registerUnselected( "restriction-status", "/images" );

			this.renderUserPicker();
		},

		renderUserPicker: function() {
			var userPickerContainer = this.$( ".user-picker-container" );

			var userPickerModel = new UserPickerModel.UserPickerModel( { controlName: "userPicker", initialUserId: 0, baseUrl: this.model.baseUrl } );
			var userPickerView = new UserPickerView.UserPickerView( { model: userPickerModel, el: userPickerContainer, callbackFunction: this.onUserPickerSelect } );
		},

		onUserPickerSelect: function() {

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