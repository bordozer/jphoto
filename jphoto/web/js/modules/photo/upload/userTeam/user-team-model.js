define( ["backbone"], function ( Backbone ) {

	var EntryModel = Backbone.Model.extend( {

		idAttribute: 'userTeamMemberId',

		openInfo: false
		, openEditor: false
		, hasUnsavedChanged: false

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

		initialize: function ( options ) {
			this.url = options.baseUrl + "/rest/photos/" + options.photoId + "/team/";
			this.userTeamMemberTypes = options.userTeamMemberTypes;
		},

		refresh: function() {
			this.fetch( { reset: true, cache: false } );
		}
	});

	return { EntriesModel:EntriesModel, EntryModel: EntryModel };
} );