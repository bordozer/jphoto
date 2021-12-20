define( ["backbone", "jquery", "underscore", "mass_checker"
		, "modules/admin/restriction/restriction-history/restriction-history-model"
		, "modules/admin/restriction/restriction-history/restriction-history-view"
		, "text!modules/admin/restriction/list/templates/restriction-list-filter.html"
		, "components/user-picker/user-picker-model"
		, "components/user-picker/user-picker-view"
		], function ( Backbone, $, _, mass_checker, HistoryModel, HistoryView, template, UserPickerModel, UserPickerView ) {

	'use strict';

	var RestrictionListFilterView = Backbone.View.extend( {

		template:_.template( template ),

		events: {
			"click .apply-filter-restriction-button" : "onApplyClick"
		},

		initialize: function() {

			this.initTranslations();

			this.listenTo( this.model, "sync", this.renderSearchResult );

//			this.renderSearchForm();
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

			modelJSON.translations = this.translations;

			this.$el.html( this.template( modelJSON ) );

			var massSelector = mass_checker.getMassChecker();
			massSelector.registerSelected( "restriction-type-user", Backbone.JPhoto.imageFolder() );
			massSelector.registerSelected( "restriction-type-photo", Backbone.JPhoto.imageFolder() );
			massSelector.registerUnselected( "restriction-status", Backbone.JPhoto.imageFolder() );

			this.renderUserPicker();
		},

		renderUserPicker: function() {
			var userPickerContainer = this.$( ".user-picker-container" );

			var userPickerModel = new UserPickerModel.UserPickerModel( { controlName: "userId", initialUserId: 0 } );
			var userPickerView = new UserPickerView.UserPickerView( { model: userPickerModel, el: userPickerContainer, callbackFunction: this.onUserPickerSelect } );
		},

		onUserPickerSelect: function() {

		},

		renderSearchResult: function() {

			this.renderSearchForm();

			var searchResultEntryDTOs = this.model.get( 'searchResultEntryDTOs' );

			var $searchResultContainer = this.$( ".search-result-container" );
			$searchResultContainer.html( '' );

			_.each( searchResultEntryDTOs, function( entryModel ) {

				var model = new HistoryModel.RestrictionHistoryEntryModel( entryModel );

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

			if ( this.$( ".restriction-type-id:checked" ).length == 0 || this.$( ".restriction-status:checked" ).length == 0 ) {
				showUIMessage_Warning( Backbone.JPhoto.ajaxService().translate( 'Restriction filter form: Check at least one Type and one Status' ) );
				return;
			}

			var data = [];
			this.$( ".restriction-type-id:checked" ).each( function () {
				data.push( { name: 'selectedRestrictionTypeIds', value: this.value } );
			} );

			this.$( ".restriction-status:checked" ).each( function () {
				data.push( { name: 'restrictionStatusIds', value: this.value } );
			} );

			data.push( { name: 'userId', value: this.$( "[name='userId']" ).val() } );

			this.clearSearchResult();

			this.model.fetch( { data: data,  cache: false } );
		}
	} );

	var RestrictionHistoryEntryModel = Backbone.Model.extend( {

	});

	return { RestrictionListFilterView:RestrictionListFilterView };
});
