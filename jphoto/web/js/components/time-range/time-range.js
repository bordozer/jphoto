define( ["components/time-range/time-range-model"
		 	, "components/time-range/time-range-view"
			, "jquery"], function ( Model, View, $ ) {

	function init( userId, userName, ajaxService, container ) {

		var rangeModel = new Model.RangeModel();

		var timePeriodView = new View.TimePeriodView( { model: rangeModel, el: container } );
		var dateRangeView = new View.DateRangeView( { model: rangeModel, el: container } );

		rangeModel.set( "userId", userId );
		rangeModel.set( "userName", userName );
		rangeModel.set( "ajaxService", ajaxService );
		rangeModel.set( "timePeriodView", timePeriodView );
		rangeModel.set( "dateRangeView", dateRangeView );

		var rangeView = new View.RangeView( { model: rangeModel, el: container } );
		rangeView.render();
	}

	return init;

} );