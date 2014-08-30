define( ["modules/users/team/user-team-model"
		, "modules/users/team/user-team-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( options ) {

		var entriesModel = new Model.EntriesModel( {
				userId: options.userId
				, userTeamMemberTypes: options.userTeamMemberTypes
				, selectedUserTeamMemberIds: options.selectedUserTeamMemberIds
				, groupSelectionClass: options.groupSelectionClass
				, translationDTO: options.translationDTO
			} );

		var entryListView = new View.EntryListView( {
				model: entriesModel
				, el: options.container
				, onEdit: options.onEdit
				, onDelete: options.onDelete
		} );
		entryListView.renderHeader();
		entryListView.render();
	}

	return init;
} );