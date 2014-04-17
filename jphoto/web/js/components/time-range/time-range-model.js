define( ["backbone"], function ( Backbone ) {

	var RangeModel = Backbone.Model.extend( {

		defaults: function() {
			return {
				rangeType: 1

				, timePeriod: 2
				, timeUnit: 3

				, dateFrom: 1111
				, dateTo: 2222
			};
		},

		save: function() {
			var userId = this.get( 'userId' );
			var ajaxService = this.get( 'ajaxService' );

			ajaxService.lockUser( userId, new Date(), new Date() );
		}
	 } );

	return { RangeModel:RangeModel };
} );