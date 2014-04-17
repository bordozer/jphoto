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
		},

		initialize: function(){
			this.listenTo( this.model, "change", this.render );
		},

		render:function () {
			console.log( 'render: ', this.model );
			var modelJSON = this.model.toJSON();
			this.$el.html( this.template( modelJSON ) );

			var view = this.model.get( 'rangeType' ) == 1 ? this.model.get( "timePeriodView" ) : this.model.get( "dateRangeView" );
			this.$el.append( view.render() );
		},

		togglePeriodType:function( value ) {
			this.model.set( {rangeType: value} );
		},

		onTogglePeriodType:function ( evt ) {
			evt.preventDefault();
			this.togglePeriodType( evt.target.value );
		}
	} );



	var TimePeriodView = Backbone.View.extend( {
		template:_.template( timePeriodTemplate ),

		initialize: function(){
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			$( '#range-div', this.$el).html( this.template( modelJSON ) );
		}
	} );



	var DateRangeView = Backbone.View.extend( {
		template:_.template( dateRangeTemplate ),

		render:function () {
			var modelJSON = this.model.toJSON();
			$( '#range-div', this.$el).html( this.template( modelJSON ) );
		}
	} );

	return { TimePeriodView:TimePeriodView, DateRangeView:DateRangeView, RangeView:RangeView };
} );
