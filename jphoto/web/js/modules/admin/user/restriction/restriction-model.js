define( ["backbone"], function ( Backbone ) {

	var UserRestrictionModel = Backbone.Model.extend( {

		idAttribute: 'userId',

		initialize: function( options ) {
			this.userId = options.userId;
			this.translations = options.translations;
			this.restrictionTypes = options.restrictionTypes;
			this.ajaxService = options.ajaxService;
		},

		saveUserRestriction: function( data ) {
			if ( data.rangeType == 1 ) {
				this.ajaxService.restrictUserPeriod( this.userId, data.timePeriod, data.timeUnit, data.restrictionTypeIds );
			} else {
				this.ajaxService.restrictUserRange( this.userId, data.dateFrom, data.dateTo, data.restrictionTypeIds );
			}
			document.location.reload(); // TODO: find out hot to refresh restriction history without page reloading
		}
	});

	return { UserRestrictionModel:UserRestrictionModel };
});