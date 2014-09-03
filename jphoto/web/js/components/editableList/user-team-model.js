define( ["backbone"], function ( Backbone ) {

	var EntryModel = Backbone.Model.extend( {

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

	var EntriesModel = Backbone.Collection.extend( {

		model: EntryModel,
		userTeamMemberTypes: [],
		translationDTO: [],

		initialize: function ( options ) {
			this.url = Backbone.JPhoto.url( "/rest/users/" + options.userId + "/team/" );
			this.userTeamMemberTypes = options.userTeamMemberTypes;
			this.selectedUserTeamMemberIds = options.selectedUserTeamMemberIds;
			this.groupSelectionClass = options.groupSelectionClass;
			this.translationDTO = options.translationDTO;
		},

		refresh: function() {
			this.fetch( { reset: true, cache: false } );
		}
	});

	return { EntriesModel:EntriesModel, EntryModel: EntryModel };
} );