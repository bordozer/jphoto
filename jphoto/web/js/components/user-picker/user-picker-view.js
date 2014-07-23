define( ["backbone", "jquery", "underscore"
		, "components/user-picker/user-picker-model"
		, "text!components/user-picker/template/template.html"
		], function ( Backbone, $, _, Model, Template ) {

	'use strict';

	var UserPickerView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			"keydown .filter-by-name": "onFilterKeyDown"
		},

		initialize: function() {
//			this.listenTo( this.model, "sync", this.renderEntry );
//			this.model.fetch( { cache: false } );
		},

		renderPicker: function () {
			this.$el.html( this.template() );
		},

		filterKeyDown: function () {
			console.log( 'filter' );
		},

		onFilterKeyDown: function ( evt ) {
//			evt.preventDefault();
//			evt.stopImmediatePropagation();

			this.filterKeyDown();
		}
	});

	return { UserPickerView: UserPickerView };
});