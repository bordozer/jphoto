define( ["modules/admin/jobs/photosImport/remoteSiteCategories/remote-site-categories-model"
		, "modules/admin/jobs/photosImport/remoteSiteCategories/remote-site-categories-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( importSourceId, baseUrl, container ) {

		var remoteSiteCategoriesModel = new Model.RemoteSiteCategoriesModel( { importSourceId: importSourceId, baseUrl:baseUrl } );

		var remoteSiteCategoriesView = new View.RemoteSiteCategoriesView( { model: remoteSiteCategoriesModel, el: container } );
	}

	return init;

} );