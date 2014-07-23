define( ["backbone"], function ( Backbone ) {

	var UserPickerModel = Backbone.Model.extend( {

		defaults: function() {
			return {
				searchString: ''
				, searchPerformed: false
				, searchResultExpanded: false
			};
		},

		initialize: function ( options ) {
			this.url = options.baseUrl + "/rest/users/?searchString=" + options.searchString;
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