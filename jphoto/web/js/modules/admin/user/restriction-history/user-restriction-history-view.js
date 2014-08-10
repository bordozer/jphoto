define( ["backbone", "jquery", "underscore"
		, "text!modules/admin/user/restriction-history/templates/user-restriction-history-template.html"
		, "text!modules/admin/user/restriction-history/templates/user-restriction-history-entry-template.html"
		], function ( Backbone, $, _, userLockHistoryTemplate, userLockHistoryEntryTemplate ) {

	'use strict';

	var UserLockHistoryView = Backbone.View.extend( {

		historyTemplate:_.template( userLockHistoryTemplate ),
		historyEntryTemplate:_.template( userLockHistoryEntryTemplate ),

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
		}
	} );

	return { UserLockHistoryView:UserLockHistoryView };
} );
