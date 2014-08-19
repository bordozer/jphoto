define( ["modules/admin/restriction/list/restriction-list-model"
		, "modules/admin/restriction/list/restriction-list-filter-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( container ) {

		var restrictionListModel = new Model.RestrictionListModel();

		var restrictionListFilterView = new View.RestrictionListFilterView( { model: restrictionListModel, el: container } );
	}

	return init;

} );