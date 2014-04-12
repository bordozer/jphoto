define( ["backbone"], function ( Backbone ) {

	var UserLockAreaModel = Backbone.Model.extend( {
											url:"/json/admin/members/8680/lock/" /* TODO: pass context here */
										  } );

	return {UserLockAreaModel:UserLockAreaModel};
} );
