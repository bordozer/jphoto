define( ["backbone"], function ( Backbone ) {

	var TimePeriodModel = Backbone.Model.extend( {

		defaults: function() {
			return {
				timePeriod: 2
				, timeUnit: 3
			};
		},

		save: function() {
			var userId = this.get( 'userId' );
			var ajaxService = this.get( 'ajaxService' );

			ajaxService.lockUser( userId, new Date(), new Date() );
		}
	 } );

	var DateRangeModel = Backbone.Model.extend( {

		defaults: function() {
			return {
				dateFrom: 1111
				, dateTo: 2222
			};
		},

		save: function() {
			var userId = this.get( 'userId' );
			var ajaxService = this.get( 'ajaxService' );

			ajaxService.lockUser( userId, new Date(), new Date() );
		}
	 } );

	var RangeModel = Backbone.Model.extend( {

		defaults: function() {
			return {
				rangeType: 1
			};
		}
	 } );

	return { RangeModel:RangeModel, TimePeriodModel:TimePeriodModel, DateRangeModel:DateRangeModel };
} );