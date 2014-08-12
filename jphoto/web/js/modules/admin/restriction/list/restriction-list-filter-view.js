define( ["backbone", "jquery", "underscore"
		, "modules/admin/restriction/restriction-history/restriction-history-view"
		, "text!modules/admin/restriction/list/templates/restriction-list-filter.html"
		], function ( Backbone, $, _, HistoryView, template ) {

	'use strict';

	var RestrictionListFilterView = Backbone.View.extend( {

		template:_.template( template ),

		initialize: function() {

			var restrictionsContainer = $( "<div></div>" );

//			this.restrictionHistoryView = new HistoryView.RestrictionHistoryView( { model: this.model, el: restrictionsContainer } );
//			this.listenTo( this.model, "add", this.render );

			this.renderFilterForm();

			this.model.fetch( { cache: false } );
		},

		renderFilterForm: function() {
			var modelJSON = this.model.toJSON();

			modelJSON.translations = this.translations;
			modelJSON.restrictionTypes = this.model.get( 'restrictionTypes' );

			console.log( this.template( modelJSON ) );
			this.$el.html( this.template( modelJSON ) );
		}
	} );

	return { RestrictionListFilterView:RestrictionListFilterView };
});