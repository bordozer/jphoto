define( ["components/time-range/time-range-model"
		 	, "components/time-range/time-range-view"
			, "jquery"], function ( Model, View, $ ) {

	function init( translations, callback, container ) {

		var rangeModel = new Model.RangeModel( { callback: callback, translations: translations } );

		var rangeView = new View.RangeView( { model: rangeModel, el: container } );
		rangeView.render();
	}

	return init;

} );