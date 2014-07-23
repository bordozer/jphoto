define( ["backbone"], function ( Backbone ) {

	var UserPickerModel = Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = options.baseUrl + "/rest/users/";
		}
	});

	return { UserPickerModel:UserPickerModel };
} );