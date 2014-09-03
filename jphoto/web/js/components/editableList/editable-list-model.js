define( ["backbone"], function ( Backbone ) {

	var EditableListEntryModel = Backbone.Model.extend( {

		idAttribute: 'entryId',

		openInfo: false
		, openEditor: false
		, hasUnsavedChanged: false
		, translationDTO: []

		, defaults: function() {
			return {
				openEditor: false
				, teamMemberTypeId: 1
			};
		}
	});

	var EditableListModel = Backbone.Collection.extend( {

		model: EditableListEntryModel,
		userTeamMemberTypes: [],
		translationDTO: [],

		initialize: function ( options ) {
			this.url = Backbone.JPhoto.url( "/rest/users/" + options.userId + "/team/" );
			this.translationDTO = options.translationDTO;
			this.groupSelectionClass = options.groupSelectionClass;

			this.userTeamMemberTypes = options.userTeamMemberTypes;
			this.selectedUserTeamMemberIds = options.selectedUserTeamMemberIds;
		},

		refresh: function() {
			this.fetch( { reset: true, cache: false } );
		}
	});

	return { EditableListModel:EditableListModel, EditableListEntryModel: EditableListEntryModel };
} );