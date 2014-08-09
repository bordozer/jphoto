define( ["backbone", "jquery", "underscore"
		, "text!modules/admin/user/restriction-history/templates/user-lock-history-template.html"
		, "text!modules/admin/user/restriction-history/templates/user-lock-history-entry-template.html"
		], function ( Backbone, $, _, userLockHistoryTemplate, userLockHistoryEntryTemplate ) {

	'use strict';

	var UserLockHistoryView = Backbone.View.extend( {

		historyTemplate:_.template( userLockHistoryTemplate ),
		historyEntryTemplate:_.template( userLockHistoryEntryTemplate ),

		initialize: function() {
			this.listenTo( this.model, "request", this.renderHistory );
			this.listenTo( this.model, "add", this.renderHistoryEntry );

			this.model.fetch( {cache: false} );
		},

		renderHistory:function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.historyTemplate( modelJSON ) );
		},

		renderHistoryEntry:function ( historyEntry ) {
			var modelJSON = historyEntry.toJSON();
			this.$el.append( this.historyEntryTemplate( modelJSON ) );
		}
	} );

	return { UserLockHistoryView:UserLockHistoryView };
} );
