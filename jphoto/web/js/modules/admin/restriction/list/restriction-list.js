define( ["modules/admin/restriction/list/restriction-list-model"
		, "modules/admin/restriction/list/restriction-list-filter-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( restrictionTypes, translations, baseUrl, container ) {

		var restrictionListModel = new Model.RestrictionListModel( { restrictionTypes:restrictionTypes, translations: translations, baseUrl:baseUrl } );

		var restrictionListFilterView = new View.RestrictionListFilterView( { model: restrictionListModel, el: container } );
	}

	return init;

} );