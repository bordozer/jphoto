define( ["modules/admin/user/restriction-history/user-restriction-history-model"
		, "modules/admin/user/restriction-history/user-restriction-history-view"
		, "jquery"], function ( Model, UserLockHistoryView, $ ) {

	function init( userId, baseUrl, container ) {

		var userLockHistoryModel = new Model.UserLockHistoryModel( {userId:userId, baseUrl:baseUrl} );
//		userLockHistoryModel.fetch( {cache: false} );

		var userLockHistoryView = new UserLockHistoryView.UserLockHistoryView( { model: userLockHistoryModel, el: container } );

//		var userLockHistoryEntryModel = new Model.UserLockHistoryEntryModel();
//		var userLockHistoryEntryView = new UserLockHistoryView.UserLockHistoryEntryView( { model: userLockHistoryEntryModel, el: container } );
	}

	return init;

} );