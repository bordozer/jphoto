define( ["backbone", "jquery", "underscore"
		, "text!components/time-range/templates/rangeTemplate.html"
		, "text!components/time-range/templates/timePeriodTemplate.html"
		, "text!components/time-range/templates/dateRangeTemplate.html"]
		, function ( Backbone, $, _, rangeTemplate, timePeriodTemplate, dateRangeTemplate ) {

	'use strict';

	var RangeView = Backbone.View.extend( {

		template:_.template( rangeTemplate ),

		events: {
			"change .lock-period-type" : "onTogglePeriodType"
//			, "click .date-range-action-button" : "onButtonClick"
		},

		initialize: function() {
			this.timePeriodView = new TimePeriodView( { model: this.model, el: this.$el } );
			this.dateRangeView = new DateRangeView( { model: this.model, el: this.$el } );

			this.timePeriodView.on( "recalculate", this.model.recalculateDateRange, this.model );
			this.dateRangeView.on( "recalculate", this.model.recalculateTimePeriod, this.model );
		},

		render: function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.template( modelJSON ) );

			$( "input[name='periodType'][value='" + this.model.get( 'rangeType' ) + "']" ).attr( "checked", true );

			this.$el.append( this.getView().render() );

			return this.$el;
		},

		getView: function() {
			return this.model.get( 'rangeType' ) == 1 ? this.timePeriodView : this.dateRangeView;
		},

		togglePeriodType:function( value ) {
			this.model.set( { rangeType: value } );
			this.render();
		},

		onTogglePeriodType:function ( evt ) {
			evt.preventDefault();
			this.togglePeriodType( evt.target.value );
		}
	} );



	var TimePeriodView = Backbone.View.extend( {

		template:_.template( timePeriodTemplate ),

		events: {
			"change .time-period-value" : "onTimePeriodValueChange"
			, "change .time-period-unit" : "onTimePeriodUnitChange"
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			$( '#range-div', this.$el ).html( this.template( modelJSON ) );
			this.seTimeUnit();
		},

		seTimeUnit:function() {
			$( "input[name='time-period-unit-radio'][value='" + this.model.get( 'timeUnit' ) + "']" ).attr( "checked", true );
		},

		onTimePeriodValueChange: function( evt ) {
			evt.preventDefault();

			this.model.set( { timePeriod: evt.target.value } );

			this.trigger( 'recalculate' );
		},

		onTimePeriodUnitChange: function( evt ) {
			evt.preventDefault();

			this.model.set( { timeUnit: $( ".time-period-unit:checked" ).val() } );

			this.trigger( 'recalculate' );
		}
	} );



	var DateRangeView = Backbone.View.extend( {

		template:_.template( dateRangeTemplate ),

		events: {
			"change .date-range-date-from" : "onDateRangeFromValueChange"
			, "change .date-range-date-to" : "onDateRangeToValueChange"
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			$( '#range-div', this.$el).html( this.template( modelJSON ) );
		},

		onDateRangeFromValueChange: function( evt ) {
			evt.preventDefault();

			this.model.set( { dateFrom: evt.target.value } );

			this.trigger( 'recalculate' );
		},

		onDateRangeToValueChange: function( evt ) {
			evt.preventDefault();

			this.model.set( { dateTo: evt.target.value } );

			this.trigger( 'recalculate' );
		}
	} );

	return { TimePeriodView:TimePeriodView, DateRangeView:DateRangeView, RangeView:RangeView };
} );
