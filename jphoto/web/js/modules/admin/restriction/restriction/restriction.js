define( ["modules/admin/restriction/restriction/restriction-model"
		 	, "modules/admin/restriction/restriction/restriction-view"
			, "jquery"], function ( Model, View, $ ) {

	function init( entryId, restrictionEntryTypeId, restrictionTypes, translations, container ) {

		var userRestrictionModel = new Model.UserRestrictionModel( { entryId: entryId, restrictionEntryTypeId: restrictionEntryTypeId, restrictionTypes: restrictionTypes, translations: translations } );

		var restrictionView = new View.RestrictionView( { model: userRestrictionModel, el: container } );
		restrictionView.render();
	}

	return init;

} );