require( ["../../../../require-config-base"], function ( config ) {
	require( ["admin/user/lock/view", "admin/user/lock/model", "jquery"], function ( View, Model, $ ) {

		var model = new Model.UserLockAreaModel();

		var view = new View.UserLockAreaView( {model:model} );

		$( "body" ).append( view.$el );
	} );
} );
