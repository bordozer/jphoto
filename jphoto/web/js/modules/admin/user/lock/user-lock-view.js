define( ["backbone", "jquery", "underscore", "text!modules/admin/user/lock/templates/lockAreaTemplate.html"], function ( Backbone, $, _, userLockTemplate ) {

	'use strict';

	var UserLockView = Backbone.View.extend( {

		template:_.template( userLockTemplate ),

		events: {
			"change .lock-period-type" : "onTogglePeriodType"
		},

		initialize: function(){
			_.bindAll( this, 'render', 'afterRender' );
			var _this = this;
			this.render = _.wrap( this.render, function( render ) {
				render();
				_this.afterRender();
				return _this;
			});
			_this.listenTo( this.model, "sync", this.render );
//			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.template( modelJSON ) );
		},

		afterRender: function () {
			console.log( 'afterRender' );
		},

		togglePeriodType:function( value ) {
//			this.model.get( 'rangeModel' ).set( 'rangeType', value ); // TODO: pass rangeModel here
		},

		onTogglePeriodType:function ( evt ) {
			evt.preventDefault();
			this.togglePeriodType( evt.target.value );
		}
	} );

	return { UserLockView:UserLockView };
} );
