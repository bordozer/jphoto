define( ["components/time-range/time-range-model"
		 	, "components/time-range/time-range-view"
			, "jquery"], function ( Model, View, $ ) {

	function init( translations, container ) {

		var rangeModel = new Model.RangeModel( { translations: translations } );

		var rangeView = new View.RangeView( { model: rangeModel, el: container } );
		rangeView.render();
	}

	return init;

} );