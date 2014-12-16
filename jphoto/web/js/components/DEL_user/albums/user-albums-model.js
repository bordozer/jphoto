define( ["backbone"], function ( Backbone ) {

	var AlbumModel = Backbone.Model.extend( {

		idAttribute: 'userAlbumId',

		openInfo: false
		, openEditor: false
		, hasUnsavedChanged: false
		, translationDTO: []

		, initialize: function ( options ) {
			this.userId = options.userId;
		}

		, defaults: function() {
			return {
				openEditor: false
			};
		}
	});

	var AlbumsModel = Backbone.Collection.extend( {

		model: AlbumModel,
		userTeamMemberTypes: [],
		translationDTO: [],

		initialize: function ( options ) {
			this.url = Backbone.JPhoto.url( "rest/users/" + options.userId + "/albums/" );
			this.userId = options.userId;
			this.selectedAlbumIds = options.selectedAlbumIds;
			this.groupSelectionClass = options.groupSelectionClass;
			this.translationDTO = options.translationDTO;
		},

		refresh: function() {
			this.fetch( { reset: true, cache: false } );
		}
	});

	return { AlbumsModel:AlbumsModel, AlbumModel: AlbumModel };
} );