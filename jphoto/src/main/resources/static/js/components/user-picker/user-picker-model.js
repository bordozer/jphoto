define( ["backbone"], function ( Backbone ) {

	var UserPickerModel = Backbone.Model.extend( {

		controlName: ''
		, selectedUserId: 0

		, defaults: function() {
			return {
				searchString: ''
				, searchPerformed: false
				, searchResultExpanded: false
			};
		},

		initialize: function ( options ) {

			this.url = Backbone.JPhoto.url( "/rest/users/?userId=" + options.initialUserId );

			this.set( { controlName: options.controlName, selectedUserId: options.initialUserId } );
		},

		openSearchResult: function() {
			this.searchResultExpanded = true;
			this.trigger( 'open_search_result' );
		},

		closeSearchResult: function() {
			this.searchResultExpanded = false;
			this.trigger( 'close_search_result' );
		},

		isSearchResultExpanded: function() {
			return this.searchResultExpanded;
		},

		performSearch: function () {
			this.searchPerformed = true;
			this.trigger( 'perform_search' );
		},

		isSearchPerformed: function () {
			return this.searchPerformed;
		}
	});

	return { UserPickerModel:UserPickerModel };
} );
