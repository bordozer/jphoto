define( ["modules/photo/upload/userTeam/user-team-model"
		, "modules/photo/upload/userTeam/user-team-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, baseUrl, container ) {

		var entriesModel = new Model.EntriesModel( { photoId: photoId, baseUrl: baseUrl } );

		var entryListView = new View.EntryListView( { model: entriesModel, el: container } );
		entryListView.render();
	}

	return init;

} );