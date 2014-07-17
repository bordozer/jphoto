define( ["modules/photo/upload/userTeam/user-team-model"
		, "modules/photo/upload/userTeam/user-team-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( userId, baseUrl, container ) {

		var userTeamModel = new Model.UserTeamModel( { userId: userId, baseUrl: baseUrl } );

		var userTeamView = new View.UserTeamView( { model: userTeamModel, el: container } );
		userTeamView.render();
	}

	return init;

} );