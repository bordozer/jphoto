define( ["backbone"], function ( Backbone ) {

	var RangeModel = Backbone.Model.extend( {

		initialize: function() {
//			this.listenTo( this, "change", this.recalculate );
			this.recalculateTimePeriod();
			this.recalculateDateRange();
		},

		defaultOffset: function() {
			return 1;
		},

		defaults: function() {

			return {
				rangeType: 1 /* 1 - time period, 2 - date range */

				, timePeriod: this.defaultOffset()
				, timeUnit: 2 /* 1 is HOURS, 2 is DAYS, 3 is MONTH, 4 is YEAR */

				, dateFrom: new Date()
				, dateTo: new Date( new Date().getTime() + this.defaultOffset() * this.millisecondsPerDay() )
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

			var timePeriod = Math.round( ( dateTo.getTime() - dateFrom.getTime() ) / this.millisecondsPerDay() );
			if ( this.get( 'timeUnit' ) == 1 && this.get( 'timePeriod' ) < 24 ) {
				this.set( { timePeriod: 1, timeUnit: 2 }, { silent: true } );
			} else {
				this.set( { timePeriod: timePeriod }, { silent: true } );
			}

//			console.log( 'RECALCULATE: timePeriod: ', timePeriod, 'timeUnit: ', this.get( 'timeUnit' ) );
		},

		recalculateDateRange: function() {

//			var _now = new Date();

			var dateFrom = new Date();
//			dateFrom.setYear( _now.getYear() );
//			dateFrom.setMonth( _now.getMonth() );
//			dateFrom.setDate( _now.getDay() );
			dateFrom.setHours( 0 );
			dateFrom.setMinutes( 0 );
			dateFrom.setSeconds( 0 );
			dateFrom.setMilliseconds( 0 );

			var delta = this.get( 'timePeriod' );

			if ( this.get( 'timeUnit' ) == 1 && this.get( 'timePeriod' ) < 24 ) {
				delta = 1;
			}

			var dateTo = new Date( dateFrom.getTime() + delta * this.millisecondsPerDay() );

//			console.log( 'RECALCULATE: dateFrom: ', dateFrom, ' dateTo: ', dateTo );

			var dateFromFormatted = dateFrom.format( 'yyyy-mm-dd' );
			var dateToFormatted = dateTo.format( 'yyyy-mm-dd' );

			this.set( { dateFrom: dateFrom, dateTo: dateTo, dateFromFormatted: dateFromFormatted, dateToFormatted: dateToFormatted }, { "silent": true } );
		},

		millisecondsPerDay: function() {
			return 1000 * 60 * 60 * 24;
		}
	 } );

	return { RangeModel:RangeModel };
} );