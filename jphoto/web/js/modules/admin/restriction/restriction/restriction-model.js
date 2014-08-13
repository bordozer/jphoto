define( ["backbone"], function ( Backbone ) {

	var UserRestrictionModel = Backbone.Model.extend( {

		idAttribute: 'entryId',

		initialize: function( options ) {
			this.entryId = options.entryId;
			this.translations = options.translations;
			this.restrictionTypes = options.restrictionTypes;
			this.restrictionEntryTypeId = options.restrictionEntryTypeId; /* 1 - user, 2 - photo */
		},

		saveRestriction: function( data ) {

			if ( data.rangeType == 1 ) {
				Backbone.JPhoto.ajaxService().restrictEntryForPeriod( this.entryId, data.timePeriod, data.timeUnit, data.restrictionTypeIds );
			} else {
				var from = new Date( data.dateFrom ).getTime();
				var to = new Date( data.dateTo ).getTime();
				Backbone.JPhoto.ajaxService().restrictEntryForRange( this.entryId, from, to, data.restrictionTypeIds );
			}

			document.location.reload(); // TODO: find out hot to refresh restriction history without page reloading
		}
	});

	return { UserRestrictionModel: UserRestrictionModel };
});