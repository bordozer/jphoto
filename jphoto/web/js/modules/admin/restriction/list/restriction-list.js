define( ["modules/admin/restriction/list/restriction-list-model"
		, "modules/admin/restriction/list/restriction-list-filter-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( restrictionTypes, restrictionStatuses, translations, container ) {

		var restrictionListModel = new Model.RestrictionListModel( { restrictionTypes: restrictionTypes, restrictionStatuses: restrictionStatuses, translations: translations } );

		var restrictionListFilterView = new View.RestrictionListFilterView( { model: restrictionListModel, el: container } );
	}

	return init;

} );