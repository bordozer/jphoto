define( ["backbone"], function ( Backbone ) {

	var EntryModel = Backbone.Model.extend( {

		idAttribute: 'userAlbumId',

		openInfo: false
		, openEditor: false
		, hasUnsavedChanged: false
		, translationDTO: []

		, defaults: function() {
			return {
				openEditor: false
			};
		}
	});

	var EntriesModel = Backbone.Collection.extend( {

		model: EntryModel,
		userTeamMemberTypes: [],
		translationDTO: [],

		initialize: function ( options ) {
			this.url = Backbone.JPhoto.url( "rest/users/" + options.userId + "/albums/" );
			this.selectedAlbumIds = options.selectedAlbumIds;
			this.groupSelectionClass = options.groupSelectionClass;
			this.translationDTO = options.translationDTO;
		},

		refresh: function() {
			this.fetch( { reset: true, cache: false } );
		}
	});

	return { EntriesModel:EntriesModel, EntryModel: EntryModel };
} );