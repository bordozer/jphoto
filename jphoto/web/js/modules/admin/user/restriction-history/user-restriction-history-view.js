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

//			this.model.on( "deactivate_entry", this.deleteRestrictionHistoryEntry, historyEntry );
//			this.listenTo( this.model, "deactivate_entry", this.deleteRestrictionHistoryEntry, historyEntry );
//			this.on( "deactivate_entry", this.deleteRestrictionHistoryEntry, historyEntry )
		},

		deleteRestrictionHistoryEntry: function( historyEntry ) {
//			console.log( historyEntry );
//			console.log( this.model );
//			this.model.remove( historyEntry );
		},

		onDeleteClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.trigger( 'deactivate_entry' );

//			var entryId = $( evt.target ).attr( 'id' );
//			this.deleteRestrictionHistoryEntry( entryId );
		}
	} );

	return { UserLockHistoryView:UserLockHistoryView };
} );
