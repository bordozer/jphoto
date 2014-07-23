define( ["backbone", "jquery", "underscore"
		, "components/user-picker/user-picker-model"
		, "text!components/user-picker/template/template.html"
		], function ( Backbone, $, _, Model, Template ) {

	'use strict';

	var UserPickerView = Backbone.View.extend( {

		initialize: function() {
			this.listenTo( this.model, "add", this.renderEntry );

			this.model.fetch( { cache: false } );
		},

		render: function () {
			var div = $( "<div style='float: left; width: 100%; border: dashed;'></div>" );

			this.$el.html( div );
		},

		renderEntry: function ( user ) {
			var modelJSON = user.toJSON();
			console.log( modelJSON );

			var div = $( "<div style='float: left; width: 100%; border: dotted;'>" + user.get( 'userName' ) + "</div>" );

			this.$el.append( div );
		}
	});

	return { UserPickerView: UserPickerView };
});