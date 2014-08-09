define( ["modules/admin/user/restriction/user-restriction-model"
		 	, "modules/admin/user/restriction/user-restriction-view"
			, "jquery"], function ( Model, View, $ ) {

	function init( userId, restrictionTypes, translations, container ) {

		var userRestrictionModel = new Model.UserRestrictionModel( { userId: userId, restrictionTypes: restrictionTypes, translations: translations } );

		var userRestrictionView = new View.UserRestrictionView( { model: userRestrictionModel, el: container } );
		userRestrictionView.render();
	}

	return init;

} );