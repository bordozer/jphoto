define( ["backbone"], function ( Backbone ) {

	var RangeModel = Backbone.Model.extend( {

		initialize: function() {
			this.listenTo( this, "change", this.recalculate );
			this.recalculateDateRange();
		},

		defaultOffset: function() {
			return 1;
		},

		defaults: function() {

			return {
				rangeType: 1

				, timePeriod: this.defaultOffset()
				, timeUnit: 2 /* 1 is HOURS, 2 is DAYS, 3 is MONTH, 4 is YEAR */

				, dateFrom: new Date()
				, dateTo: new Date( new Date().getTime() + this.defaultOffset() * 1000 * 60 * 60 * 24 )
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
			var dateFrom = new Date( this.get( 'dateFrom' ) );
			var dateTo = new Date( this.get( 'dateTo' ) );

			var millisecondsPerDay = 1000 * 60 * 60 * 24;
			if ( this.get( 'timeUnit' ) == 1 && this.get( 'timePeriod' ) < 24 ) {
				this.set( { timeUnit: 1 } )
			}
			var timePeriod = Math.round( ( dateTo.getTime() - dateFrom.getTime() ) / millisecondsPerDay );

//			console.log( 'RECALCULATE: timePeriod: ', timePeriod );

			this.set( { timePeriod: timePeriod, timeUnit: 2 }, { "silent": true } );
		},

		recalculateDateRange: function() {
			var dateFrom = new Date();
			var dateTo = new Date( dateFrom.getTime() + ( this.get( 'timePeriod' ) ) * 1000 * 60 * 60 * 24 );

//			console.log( 'RECALCULATE: dateFrom: ', dateFrom, ' dateTo: ', dateTo, ' timePeriod: ' );

			var dateFromFormatted = dateFrom.format( 'yyyy-mm-dd' );
			var dateToFormatted = dateTo.format( 'yyyy-mm-dd' );

			this.set( { dateFrom: dateFrom, dateTo: dateTo, dateFromFormatted: dateFromFormatted, dateToFormatted: dateToFormatted }, { "silent": true } );
		}/*,

		save: function() {
			var userId = this.get( 'userId' );
			var ajaxService = this.get( 'ajaxService' );

			ajaxService.lockUser( userId, new Date(), new Date() );
		}*/

	 } );

	return { RangeModel:RangeModel };
} );