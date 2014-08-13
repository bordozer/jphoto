define( ["modules/photo/upload/userTeam/user-team-model"
		, "modules/photo/upload/userTeam/user-team-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, container, userTeamMemberTypes, translationDTO ) {

		var entriesModel = new Model.EntriesModel( { photoId: photoId, userTeamMemberTypes: userTeamMemberTypes, translationDTO: translationDTO } );

		var entryListView = new View.EntryListView( { model: entriesModel, el: container } );
		entryListView.renderHeader();
		entryListView.render();
	}

	return init;

} );