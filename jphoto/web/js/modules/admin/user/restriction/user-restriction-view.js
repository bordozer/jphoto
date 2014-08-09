define( ["backbone", "jquery", "underscore"
		 , "components/time-range/time-range-model"
		 , "components/time-range/time-range-view"
		, "text!modules/admin/user/restriction/templates/user-restriction-template.html"
		]
		, function ( Backbone, $, _, TimeBoxModel, TimeBoxView, template ) {

	'use strict';

	var UserRestrictionView = Backbone.View.extend( {

		template:_.template( template ),

		initialize: function() {
			this.render();
		},

		render: function() {

			var timeBox = $( "<div></div>" );

			var timeBoxModel = new TimeBoxModel.RangeModel( { translations: this.model.get( 'translations' ) } );

			var timeBoxView = new TimeBoxView.RangeView( { model: timeBoxModel, el: timeBox } );
			this.$el.html( timeBoxView.render() );

			var modelJSON = this.model.toJSON();
			this.$el.append( this.template( modelJSON ) );
		}
	});

	return { UserRestrictionView:UserRestrictionView };
} );