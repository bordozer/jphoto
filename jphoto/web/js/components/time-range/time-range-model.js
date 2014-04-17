define( ["backbone"], function ( Backbone ) {

	var RangeModel = Backbone.Model.extend( {

		initialize: function() {
			this.listenTo( this, "change", this.recalculate );
		},

		defaults: function() {
			return {
				rangeType: 1

				, timePeriod: 2
				, timeUnit: 3

				, dateFrom: '2014-04-01'
				, dateTo: '2014-04-21'
			};
		},

		recalculate: function() {
			if ( this.get( 'rangeType' ) == 1 ) {
				this.recalculateDateRange();
			} else {
				this.recalculateTimePeriod();
			}
		},

		recalculateTimePeriod: function() {
			console.log( 'recalculateTimePeriod: ', this );
		},

		recalculateDateRange: function() {
			console.log( 'recalculateDateRange: ', this );
		},

		save: function() {
			var userId = this.get( 'userId' );
			var ajaxService = this.get( 'ajaxService' );

			ajaxService.lockUser( userId, new Date(), new Date() );
		}
	 } );

	return { RangeModel:RangeModel };
} );