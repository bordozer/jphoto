define( ["components/time-range/time-range-model"
		 	, "components/time-range/time-range-view"
			, "jquery"], function ( Model, View, $ ) {

	function init( userId, ajaxService, container ) {

		var timePeriodModel = new Model.TimePeriodModel();
		timePeriodModel.set( "userId", userId );
		timePeriodModel.set( "ajaxService", ajaxService );
		var timePeriodView = new View.TimePeriodView( { model: timePeriodModel, el: container } );

		var dateRangeModel = new Model.DateRangeModel();
		dateRangeModel.set( "userId", userId );
		dateRangeModel.set( "ajaxService", ajaxService );
		var dateRangeView = new View.DateRangeView( { model: dateRangeModel, el: container } );

		var rangeModel = new Model.RangeModel();
		rangeModel.set( "timePeriodView", timePeriodView );
		rangeModel.set( "dateRangeView", dateRangeView );

		var rangeView = new View.RangeView( { model: rangeModel, el: container } );
		rangeView.render();
	}

	return init;

} );