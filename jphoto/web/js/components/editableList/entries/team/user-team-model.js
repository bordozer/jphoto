define( [ "components/editableList/editable-list-model" ], function ( EditableListModel ) {

	var UserTeamMemberModel = EditableListModel.EditableListEntryModel.extend( {

		defaults: function() {
			return {
				teamMemberTypeId: 1
			}
		}
	});

	var UserTeamModel = EditableListModel.EditableListModel.extend( {

		model: UserTeamMemberModel,

		initialize: function ( options ) {
			this.url = Backbone.JPhoto.url( "/rest/users/" + options.userId + "/team/" );

			this.userTeamMemberTypes = options.userTeamMemberTypes;
			this.selectedUserTeamMemberIds = options.selectedUserTeamMemberIds;
		}
	});

	return { UserTeamModel:UserTeamModel };
});