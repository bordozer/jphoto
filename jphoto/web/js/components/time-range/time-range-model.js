define( ["backbone"], function ( Backbone ) {

	var TimePeriodModel = Backbone.Model.extend( {

		defaults: function() {
			return {
				timePeriod: 1
				, timeUnit: 2
			};
		}
	 } );

	var DateRangeModel = Backbone.Model.extend( {

		defaults: function() {
			return {
				dateFrom: 1111
				, dateTo: 2222
			};
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