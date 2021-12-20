define( [ 'jquery'
			, 'modules/portal/activity/pp-activity-model'
			, 'modules/portal/activity/pp-activity-view'
		], function ( $, Model, View ) {

	function init( container ) {
		var model = new Model.PortalPageActivityStreamModel();
		var view = new View.PortalPageActivityStreamView( { model: model, el: container } );
	}

	return init;
} );