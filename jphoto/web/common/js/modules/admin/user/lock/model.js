define( ["backbone"], function ( Backbone ) {

	var UserLockAreaModel = Backbone.Model.extend( {
											url:"/jphoto/json/admin/members/8680/lock/"
										  } );

	return {UserLockAreaModel:UserLockAreaModel};
} );
