define( [ "jquery"
	, "components/editableList/entries/team/user-team-model"
	, "components/editableList/editable-list-view"
	, "components/editableList/entries/team/user-team-view"
], function ( $, Model, View, CompositeView ) {

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
		});

		var editableListView = new View.EntryListView( {
				model: userTeamModel
				, el: options.container
				, entryCompositeView: userTeamCompositeView
				, onEdit: options.onEdit
				, onDelete: options.onDelete
		} );
		editableListView.renderHeader();
		editableListView.render();
	}

	return init;
});