define( ["backbone", "jquery", "underscore"
		, "modules/admin/restriction/restriction-history/restriction-history-view"
		, "text!modules/admin/restriction/list/templates/restriction-list-filter.html"
		], function ( Backbone, $, _, HistoryView, template ) {

	'use strict';

	var RestrictionListFilterView = Backbone.View.extend( {

		template:_.template( template ),

		events: {
			"click .apply-filter-restriction-button" : "onApplyClick"
		},

		initialize: function() {
			this.renderFilterForm();

			this.restrictionsContainer = this.$( ".search-result-container" ); //$( "<div></div>" );
//			this.$el.append( this.restrictionsContainer );

			new HistoryView.RestrictionHistoryView( { model: this.model, el: this.restrictionsContainer } );
		},

		renderFilterForm: function() {
			var modelJSON = this.model.toJSON();

			modelJSON.restrictionTypes = this.model.restrictionTypes;
			modelJSON.translations = this.model.translations;

			this.$el.html( this.template( modelJSON ) );
		},

		onApplyClick: function ( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.restrictionsContainer.html( "" );
			this.model.fetch( { reset: true, cache: false } );
		}
	} );

	return { RestrictionListFilterView:RestrictionListFilterView };
});