define( ["components/editableList/entries/team/user-team-model"
		, "components/editableList/editable-list-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( options ) {

		var editableListModel = new Model.UserTeamModel( {
			userId: options.userId
			, userTeamMemberTypes: options.userTeamMemberTypes
			, selectedIds: options.selectedIds

			, groupSelectionClass: options.groupSelectionClass
			, translationDTO: options.translationDTO
		});
		console.log( editableListModel );

		var editableListView = new View.EntryListView( {
				model: editableListModel
				, el: options.container
				, onEdit: options.onEdit
				, onDelete: options.onDelete
		} );
		editableListView.renderHeader();
		editableListView.render();
	}

	return init;
});