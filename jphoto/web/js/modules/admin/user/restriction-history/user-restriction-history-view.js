define( ["backbone", "jquery", "underscore"
		, "text!modules/admin/user/restriction-history/templates/user-restriction-history-template.html"
		, "text!modules/admin/user/restriction-history/templates/user-restriction-history-entry-template.html"
		], function ( Backbone, $, _, userLockHistoryTemplate, userLockHistoryEntryTemplate ) {

	'use strict';

	var UserLockHistoryView = Backbone.View.extend( {

		historyTemplate:_.template( userLockHistoryTemplate ),
		historyEntryTemplate:_.template( userLockHistoryEntryTemplate ),

		events: {
			"click .delete-restriction" : "onDeleteClick"
		},

		initialize: function() {
			this.listenTo( this.model, "request", this.renderHistory );
			this.listenTo( this.model, "add", this.renderHistoryEntry );
			this.listenTo( this.model, "delete", this.renderHistoryEntry );

			this.model.fetch( {cache: false} );

			this.translations = this.model.translations;
		},

		renderHistory:function () {
			this.$el.html( "" );
		},

		renderHistoryEntry:function ( historyEntry ) {
			var modelJSON = historyEntry.toJSON();
			modelJSON.translations = this.translations;

			this.$el.append( this.historyEntryTemplate( modelJSON ) );
		},

		deleteRestrictionHistoryEntry: function( entryId ) {
			this.model.remove( entryId );
		},

		onDeleteClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			var entryId = $( evt.target ).attr( 'id' );
			this.deleteRestrictionHistoryEntry( entryId );
		}
	} );

	return { UserLockHistoryView:UserLockHistoryView };
} );
