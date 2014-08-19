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

			this.initTranslations();

			this.listenTo( this.model, "sync", this.renderSearchResult );

			this.renderSearchForm();
			this.clearSearchResult();

			this.model.fetch( { cache: false } );
		},

		initTranslations: function() {
			var translations = {
				filterButtonTitle: 'Restriction filter form: Filter button title'
				, emptySearchResultText: 'Restriction filter form: Empty Search Result Text'
				, filterByUserRestrictionsTitle: 'Restriction filter form: Filter by user restrictions title'
				, filterByPhotoRestrictionsTitle: 'Restriction filter form: Filter by photo restrictions title'
				, filterByStatusTitle: 'Restriction filter form: Filter by status title'
			};

			this.translations = Backbone.JPhoto.ajaxService().translateAll( translations );
		},

		renderSearchForm: function() {
			var modelJSON = this.model.toJSON();

			modelJSON.restrictionTypesUser = this.model.restrictionTypesUser;
			modelJSON.restrictionTypesPhoto = this.model.restrictionTypesPhoto;
			modelJSON.restrictionStatuses = this.model.restrictionStatuses;
			modelJSON.translations = this.translations;

			this.$el.html( this.template( modelJSON ) );

			var massSelector = mass_checker.getMassChecker();
			massSelector.registerUnselected( "restriction-type-user", "/images" );
			massSelector.registerUnselected( "restriction-type-photo", "/images" );
			massSelector.registerUnselected( "restriction-status", "/images" );

			this.renderUserPicker();
		},

		renderUserPicker: function() {
			var userPickerContainer = this.$( ".user-picker-container" );

			var userPickerModel = new UserPickerModel.UserPickerModel( { controlName: "userPicker", initialUserId: 0 } );
			var userPickerView = new UserPickerView.UserPickerView( { model: userPickerModel, el: userPickerContainer, callbackFunction: this.onUserPickerSelect } );
		},

		onUserPickerSelect: function() {

		},

		renderSearchResult: function() {

			var searchResultEntryDTOs = this.model.get( 'searchResultEntryDTOs' );

			var $searchResultContainer = this.$( ".search-result-container" );
			$searchResultContainer.html( '' );

			_.each( searchResultEntryDTOs, function( entryModel ) {

				var model = new RestrictionHistoryEntryModel( entryModel );

				var restrictionHistoryEntryView = new HistoryView.RestrictionHistoryEntryView( {
					model: model
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
			$( ".restriction-type-id:checked" ).each( function () {
				selectedRestrictionTypeIds.push( { name: 'selectedRestrictionTypeIds', value: this.value } );
			} );

			this.clearSearchResult();

			this.model.fetch( { data: selectedRestrictionTypeIds,  cache: false } );
		}
	} );

	var RestrictionHistoryEntryModel = Backbone.Model.extend( {

	});

	return { RestrictionListFilterView:RestrictionListFilterView };
});