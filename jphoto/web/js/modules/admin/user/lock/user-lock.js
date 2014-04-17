define( ["modules/admin/user/lock/user-lock-model"
		 	, "modules/admin/user/lock/user-lock-view"
			, "jquery"], function ( Model, UserLockView, $ ) {

	function init( userId, baseUrl, container ) {

		var model = new Model.UserLockModel( {userId:userId, baseUrl:baseUrl} );
		model.fetch( {cache: false} );

		var view = new UserLockView.UserLockView( { model: model, el: container } );
	}

	return init;

} );