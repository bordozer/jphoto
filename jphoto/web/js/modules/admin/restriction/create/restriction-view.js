define( ["backbone", "jquery", "underscore"
		 , "components/time-range/time-range-model"
		 , "components/time-range/time-range-view"
		, "text!modules/admin/restriction/create/templates/restriction-template.html"
		]
		, function ( Backbone, $, _, TimeBoxModel, TimeBoxView, template ) {

	'use strict';

	var RestrictionView = Backbone.View.extend( {

		template:_.template( template ),

		events: {
			"click .do-restriction-button" : "restrictButtonClick"
		},

		initialize: function() {
			this.timeBoxModel = new TimeBoxModel.RangeModel( { translations: this.model.get( 'translations' ) } );

			this.render();
		},

		render: function() {

			var timeBox = $( "<div></div>" );
			var timeBoxView = new TimeBoxView.RangeView( { model: this.timeBoxModel, el: timeBox } );
			this.$el.html( timeBoxView.render() );

			var modelJSON = this.model.toJSON();
			this.$el.append( this.template( modelJSON ) );
		},

		restrictButtonClick: function ( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			var matches = [];
			$( ".restriction-type:checked" ).each( function () {
				matches.push( this.value );
			} );

			var data = {
				rangeType: this.timeBoxModel.get( 'rangeType' )
				, timePeriod: this.timeBoxModel.get( 'timePeriod' )
				, timeUnit: this.timeBoxModel.get( 'timeUnit' )
				, dateFrom: this.timeBoxModel.get( 'dateFrom' )
				, dateTo: this.timeBoxModel.get( 'dateTo' )
				, restrictionTypeIds: matches
			};

			this.model.saveRestriction( data );
		}
	});

	return { RestrictionView:RestrictionView };
} );