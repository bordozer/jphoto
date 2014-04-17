define( ["modules/admin/user/lock/user-lock-history-model"
		, "modules/admin/user/lock/user-lock-history-view"
		, "jquery"], function ( Model, UserLockHistoryView, $ ) {

	function init( userId, baseUrl, container ) {

		var model = new Model.UserLockHistoryModel( {userId:userId, baseUrl:baseUrl} );
		model.fetch( {cache: false} );

		var view = new UserLockHistoryView.UserLockHistoryView( { model: model, el: container } );
	}

	return init;

} );