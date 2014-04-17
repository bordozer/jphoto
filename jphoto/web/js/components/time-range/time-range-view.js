define( ["backbone", "jquery", "underscore", "text!components/time-range/templates/timePeriodTemplate.html", "text!components/time-range/templates/dateRangeTemplate.html"]
		, function ( Backbone, $, _, timePeriodTemplate, dateRangeTemplate ) {

	'use strict';

	var RangeView = Backbone.View.extend( {

		initialize: function(){
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var view = this.model.get( 'rangeType' ) == 1 ? this.model.get( "timePeriodView" ) : this.model.get( "dateRangeView" );
			this.$el.html( view.render() );
		}
	} );



	var TimePeriodView = Backbone.View.extend( {
		template:_.template( timePeriodTemplate ),

		initialize: function(){
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.template( modelJSON ) );
		}
	} );



	var DateRangeView = Backbone.View.extend( {
		template:_.template( dateRangeTemplate ),

		render:function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.template( modelJSON ) );
		}
	} );

	return { TimePeriodView:TimePeriodView, DateRangeView:DateRangeView, RangeView:RangeView };
} );
