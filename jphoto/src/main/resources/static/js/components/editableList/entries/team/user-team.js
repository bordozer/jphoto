define( [ "jquery"
	, "components/editableList/entries/team/user-team-model"
	, "components/editableList/entries/team/user-team-view"
	, "components/editableList/editable-list-view"
], function ( $, Model, CompositeView, View ) {

	function init( options ) {

		var userTeamModel = new Model.UserTeamModel( {
			userId: options.userId
			, userTeamMemberTypes: options.userTeamMemberTypes
			, selectedIds: options.selectedIds

			, groupSelectionClass: options.groupSelectionClass
			, translationDTO: options.translationDTO
		});

		var userTeamCompositeView = new CompositeView.UserTeamCompositeView( {
			model: userTeamModel
			, onEdit: options.onEdit
			, onDelete: options.onDelete
		});

		var userTeamEntryListView = new View.EntryListView( {
			model: userTeamModel
			, el: options.container
			, entryCompositeView: userTeamCompositeView
		} );
		userTeamEntryListView.renderHeader();
	}

	return init;
});