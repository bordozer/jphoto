define( [ "components/editableList/editable-list-model" ], function ( EditableListModel ) {

	var UserTeamMemberModel = EditableListModel.EditableListEntryModel.extend( {
		defaults: _.extend( {}, EditableListModel.EditableListEntryModel.prototype.defaults, { teamMemberTypeId: 1 } )
	} );

	var UserTeamModel = EditableListModel.EditableListModel.extend( {

		model: UserTeamMemberModel,

		initialize: function ( options ) {
			UserTeamModel.__super__.initialize.apply( this, arguments );

			this.url = Backbone.JPhoto.url( "/rest/users/" + options.userId + "/team/" );

			this.userTeamMemberTypes = options.userTeamMemberTypes;
		}
	} );

	return { UserTeamModel: UserTeamModel, UserTeamMemberModel: UserTeamMemberModel };
} );