define( ["components/time-range/time-range-model"
		 	, "components/time-range/time-range-view"
			, "jquery"], function ( Model, View, $ ) {

	function init( container ) {

		var timePeriodModel = new Model.TimePeriodModel();
//		console.log( 'timePeriodModel: ', timePeriodModel );
		var timePeriodView = new View.TimePeriodView( { model: timePeriodModel, el: container } );

		var dateRangeModel = new Model.DateRangeModel();
//		console.log( 'dateRangeModel: ', dateRangeModel );
		var dateRangeView = new View.DateRangeView( { model: dateRangeModel, el: container } );

		var rangeModel = new Model.RangeModel();
		rangeModel.set( "timePeriodView", timePeriodView );
		rangeModel.set( "dateRangeView", dateRangeView );
//		console.log( 'rangeModel: ', rangeModel );
//		rangeModel.fetch( {cache:false} );

		var rangeView = new View.RangeView( {
			model: rangeModel
			, el: container
//			, timePeriodView: timePeriodView
//			, dateRangeView:dateRangeView
		} );
//		console.log( 'rangeView: ', rangeView );
		rangeView.render();
	}

	return init;

} );