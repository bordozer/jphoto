define( ["backbone", "jquery", "underscore"
		, "text!modules/admin/restriction/restriction-history/templates/restriction-history-entry-template.html"
		], function ( Backbone, $, _, userLockHistoryEntryTemplate ) {

	'use strict';

	var UserLockHistoryView = Backbone.View.extend( {

		initialize: function() {
			this.$el.html( "" );

			this.listenTo( this.model, "add", this.render );

			this.model.fetch( {cache: false} );

			this.translations = this.model.translations;
		},

		render: function ( historyEntry ) {

			var translations = this.model.translations;

			var el = $( "<div></div>" );

			var entryView = new UserLockHistoryEntryView( {
				model: historyEntry
				, el: el
				, translations: translations
			} );
			entryView.render();

			this.$el.append( el );
		}
	} );

	var UserLockHistoryEntryView = Backbone.View.extend( {

		historyEntryTemplate:_.template( userLockHistoryEntryTemplate ),

		events: {
			"click .cancel-restriction" : "onCancelClick"
			, "click .delete-restriction" : "onDeleteClick"
		},

		initialize: function( options ) {
			this.translations = options.translations;

			this.listenTo( this.model, "sync", this.render );
		},

		render: function () {
			var modelJSON = this.model.toJSON();
			modelJSON.translations = this.translations;

			this.$el.html( this.historyEntryTemplate( modelJSON ) );

			return this;
		},

		onCancelClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			if ( ! confirm( this.translations.cancelConfirmation ) ) {
				return;
			}

			this.model.save();
		},

		onDeleteClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			if ( ! confirm( this.translations.deleteConfirmation ) ) {
				return;
			}

			this.model.destroy();
			this.remove();
		}
	});

	return { UserLockHistoryView:UserLockHistoryView };
} );
