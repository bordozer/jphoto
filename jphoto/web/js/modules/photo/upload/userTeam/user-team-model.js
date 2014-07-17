define( ["backbone"], function ( Backbone ) {

	var EntryModel = Backbone.Model.extend( {

		idAttribute: 'userTeamMemberId',

		openInfo: false,
		openEditor: false,
		hasUnsavedChanged: false
	});

	var EntriesModel = Backbone.Collection.extend( {

		model: EntryModel,

		initialize: function ( options ) {
			this.url = options.baseUrl + "/rest/users/" + options.userId + "/team/";
		},

		refresh: function() {
			this.fetch( { reset: true, cache: false } );
		}
	});

	return { EntriesModel:EntriesModel, EntryModel: EntryModel };
} );