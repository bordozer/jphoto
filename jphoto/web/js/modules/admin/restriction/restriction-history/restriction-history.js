define( ["modules/admin/restriction/restriction-history/restriction-history-model"
		, "modules/admin/restriction/restriction-history/restriction-history-view"
		, "jquery"], function ( Model, UserLockHistoryView, $ ) {

	function init( userId, translations, baseUrl, container ) {

		var userLockHistoryModel = new Model.UserLockHistoryModel( {userId:userId, translations: translations, baseUrl:baseUrl} );

		var userLockHistoryView = new UserLockHistoryView.UserLockHistoryView( { model: userLockHistoryModel, el: container } );
	}

	return init;

} );