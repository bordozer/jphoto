define( ["modules/photo/upload/user-team/user-team-model"
		, "modules/photo/upload/user-team/user-team-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( userId, baseUrl, container ) {

		var userTeamModel = new Model.UserTeamModel( { userId: userId, baseUrl: baseUrl } );
//		userTeamModel.set( { isGroupOperationEnabled: isGroupOperationEnabled, baseUrl: baseUrl, photoListId: photoListId } );
		userTeamModel.fetch( { cache: false } );

		var userTeamView = new View.UserTeamView( { model: userTeamModel, el: container } );
	}

	return init;

} );