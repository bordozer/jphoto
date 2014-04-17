define( ["backbone", "jquery", "underscore", "text!modules/admin/user/lock/templates/lockAreaTemplate.html"], function ( Backbone, $, _, userLockTemplate ) {

	'use strict';

	var UserLockView = Backbone.View.extend( {

		template:_.template( userLockTemplate ),

		events: {
			"change .lock-period-type" : "onTogglePeriodType"
		},

		initialize: function(){
			this.listenTo( this.model, "sync", this.render );

			$( "input[name=lockPeriodType][value='1']" ).attr( 'checked', 'checked' );
//			this.togglePeriodType( 1 ); // TODO: read from model
		},

		render:function () {
			console.log( 'UserLockView rendering' );
			var modelJSON = this.model.toJSON();
			this.$el.html( this.template( modelJSON ) );
		},

		/*togglePeriodType:function( value ) {
//			$( "input[name=lockPeriodType][value=" + value + "]" ).attr( 'checked', 'checked' );
			if ( value == 1 ) {
				$( "#lockPeriodType_DateRange" ).hide();
				$( "#lockPeriodType_TimePeriod" ).show();
			} else {
				$( "#lockPeriodType_TimePeriod" ).hide();
				$( "#lockPeriodType_DateRange" ).show();
			}
		},*/

		onTogglePeriodType:function ( evt ) {
			evt.preventDefault();
//			console.log( 'onTogglePeriodType' );
//			this.togglePeriodType( evt.target.value );
		}
	} );

	return { UserLockView:UserLockView };
} );
