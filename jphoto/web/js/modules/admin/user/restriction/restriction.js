define( ["modules/admin/user/restriction/restriction-model"
		 	, "modules/admin/user/restriction/restriction-view"
			, "jquery"], function ( Model, View, $ ) {

	function init( userId, restrictionTypes, translations, container, ajaxService ) {

		var userRestrictionModel = new Model.UserRestrictionModel( { userId: userId, restrictionTypes: restrictionTypes, translations: translations, ajaxService: ajaxService } );

		var userRestrictionView = new View.UserRestrictionView( { model: userRestrictionModel, el: container } );
		userRestrictionView.render();
	}

	return init;

} );