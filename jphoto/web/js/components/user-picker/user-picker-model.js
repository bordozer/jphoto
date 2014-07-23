define( ["backbone"], function ( Backbone ) {

	var UserModel = Backbone.Model.extend( {

		idAttribute: 'userId'
	} );

	var UserPickerModel = Backbone.Collection.extend( {

		model: UserModel,

		initialize: function ( options ) {
			this.url = options.baseUrl + "/rest/users/";
		}
	});

	return { UserPickerModel:UserPickerModel };
} );