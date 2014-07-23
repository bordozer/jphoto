define( ["backbone"], function ( Backbone ) {

	var UserPickerModel = Backbone.Model.extend( {

		callback: ''

		, defaults: function() {
			return {
				searchString: ''
				, found: false
			};
		},

		initialize: function ( options ) {
			this.url = options.baseUrl + "/rest/users/?searchString=" + options.searchString;
			this.callback = options.callback;
//			this.set( { searchString: options.searchString } );
		}
	});

	return { UserPickerModel:UserPickerModel };
} );