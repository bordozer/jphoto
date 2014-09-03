define( ["components/editableList/editable-list-model"
		, "components/editableList/editable-list-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( options ) {

		var editableListModel = new Model.EditableListModel( {
				userId: options.userId
				, userTeamMemberTypes: options.userTeamMemberTypes
				, selectedUserTeamMemberIds: options.selectedUserTeamMemberIds
				, groupSelectionClass: options.groupSelectionClass
				, translationDTO: options.translationDTO
			} );

		var entryListView = new View.EntryListView( {
				model: editableListModel
				, el: options.container
				, onEdit: options.onEdit
				, onDelete: options.onDelete
		} );
		entryListView.renderHeader();
		entryListView.render();
	}

	return init;
} );