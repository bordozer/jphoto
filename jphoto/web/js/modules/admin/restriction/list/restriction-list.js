define( ["modules/admin/restriction/list/restriction-list-model"
		, "modules/admin/restriction/list/restriction-list-filter-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( restrictionTypesUser, restrictionTypesPhoto, restrictionStatuses, container ) {

		var restrictionListModel = new Model.RestrictionListModel( {
			restrictionTypesUser: restrictionTypesUser
			, restrictionTypesPhoto: restrictionTypesPhoto
			, restrictionStatuses: restrictionStatuses
		} );

		var restrictionListFilterView = new View.RestrictionListFilterView( { model: restrictionListModel, el: container } );
	}

	return init;

} );