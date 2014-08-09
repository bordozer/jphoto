define( ["backbone"], function ( Backbone ) {

	var UserRestrictionModel = Backbone.Model.extend( {

		idAttribute: 'userId',

		defaults: function() {
			return {
				restrictionTypeId: 1
			}
		},

		initialize: function( options ) {
			this.translations = options.translations;
			this.restrictionTypes = options.restrictionTypes;
		}
	});

	return { UserRestrictionModel:UserRestrictionModel };
});