define( ["modules/photo/upload/userTeam/user-team-model"
		, "modules/photo/upload/userTeam/user-team-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( userId, container, userTeamMemberTypes, selectedUserTeamMemberIds, translationDTO ) {

		var entriesModel = new Model.EntriesModel( { userId: userId, userTeamMemberTypes: userTeamMemberTypes, selectedUserTeamMemberIds: selectedUserTeamMemberIds, translationDTO: translationDTO } );

		var entryListView = new View.EntryListView( { model: entriesModel, el: container } );
		entryListView.renderHeader();
		entryListView.render();
	}

	return init;
} );