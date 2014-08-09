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
			, "click .date-range-action-button" : "onButtonClick"
		},

		initialize: function() {
			this.timePeriodView = new TimePeriodView( { model: this.model, el: this.$el } );
			this.dateRangeView = new DateRangeView( { model: this.model, el: this.$el } );

			this.listenTo( this.model, "change", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.template( modelJSON ) );

			$( "input[name='periodType'][value='" + this.model.get( 'rangeType' ) + "']" ).attr( "checked", true );

			this.$el.append( this.getView().render() );
		},

		getView: function() {
			return this.model.get( 'rangeType' ) == 1 ? this.timePeriodView : this.dateRangeView;
		},

		togglePeriodType:function( value ) {
			this.model.set( {rangeType: value} );
		},

		onTogglePeriodType:function ( evt ) {
			evt.preventDefault();
			this.togglePeriodType( evt.target.value );
		},

		onButtonClick: function() {
			var model = {
				rangeType: this.model.get( 'rangeType' )
				, timePeriod: this.model.get( 'timePeriod' )
				, timeUnit: this.model.get( 'timeUnit' )
				, dateFrom: this.model.get( 'dateFrom' )
				, dateTo: this.model.get( 'dateTo' )
			};
			eval ( this.model.get( 'callback' )( model ) );
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
			$( '#range-div', this.$el).html( this.template( modelJSON ) );
			this.seTimeUnit();
		},

		seTimeUnit:function() {
			$( "input[name='time-period-unit-radio'][value='" + this.model.get( 'timeUnit' ) + "']" ).attr( "checked", true );
		},

		onTimePeriodValueChange: function( evt ) {
			evt.preventDefault();
			this.model.set( { timePeriod: evt.target.value } );
		},

		onTimePeriodUnitChange: function( evt ) {
			evt.preventDefault();
			this.model.set( { timeUnit: $( ".time-period-unit:checked" ).val() } );
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
		},

		onDateRangeToValueChange: function( evt ) {
			evt.preventDefault();
			this.model.set( { dateTo: evt.target.value } );
		}
	} );

	return { TimePeriodView:TimePeriodView, DateRangeView:DateRangeView, RangeView:RangeView };
} );
