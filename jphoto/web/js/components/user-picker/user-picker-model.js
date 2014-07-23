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
			this.url = options.baseUrl + "/rest/users/";
			this.callback = options.callback;
		}
	});

	return { UserPickerModel:UserPickerModel };
} );