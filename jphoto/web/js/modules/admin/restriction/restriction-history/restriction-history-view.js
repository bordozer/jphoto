define( ["backbone", "jquery", "underscore"
		, "text!modules/admin/restriction/restriction-history/templates/restriction-history-entry-template.html"
		], function ( Backbone, $, _, restrictionHistoryEntryTemplate ) {

	'use strict';

	var RestrictionHistoryView = Backbone.View.extend( {

		initialize: function() {
			this.$el.html( "" );

			this.listenTo( this.model, "sync", this.render );
		},

		render: function () {
			var container = this.$el;
			container.html( '' );

			this.model.each( function ( historyEntry ) {
				var entryContainer = $( "<div></div>" );

				var entryView = new RestrictionHistoryEntryView( {
					model: historyEntry
					, el: entryContainer
				} );
				entryView.render();
				container.append( entryContainer );
			});
		}
	} );

	var RestrictionHistoryEntryView = Backbone.View.extend( {

		historyEntryTemplate:_.template( restrictionHistoryEntryTemplate ),

		events: {
			"click .cancel-restriction" : "onCancelClick"
			, "click .delete-restriction" : "onDeleteClick"
		},

		initialize: function( options ) {
			var historyEntryTranslations = {
				restrictionDuration: 'Restriction history: Restriction duration'
				, expiresAfter: 'Restriction history: Expires after'
				, createdBy: 'Restriction history: Created by'
				, restrictedAtTime: 'Restriction history: restricted at time'
				, cancel: 'Restriction history: cancel restriction'
				, cancelTitle: 'Restriction history: cancel title'
				, deleteRestriction: 'Restriction history: delete restriction'
				, deleteTitle: 'Restriction history: delete title'
				, cancelledBy: 'Restriction history: cancelled by'
				, cancelledAtTime: 'Restriction history: cancelled at time'
				, wasRestrictedTitle: 'Restriction history: was restricted title'
				, cancelConfirmation: 'Restriction history: cancel confirmation'
				, deleteConfirmation: 'Restriction history: was delete confirmation'
			};

			this.historyEntryTranslations = Backbone.JPhoto.ajaxService().translateAll( historyEntryTranslations );
		},

		render: function () {
			var modelJSON = this.model.toJSON();
			modelJSON.historyEntryTranslations = this.historyEntryTranslations;

			this.$el.html( this.historyEntryTemplate( modelJSON ) );

			return this;
		},

		onCancelClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			if ( ! confirm( this.historyEntryTranslations.cancelConfirmation ) ) {
				return;
			}

			this.model.save();
		},

		onDeleteClick: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			if ( ! confirm( this.historyEntryTranslations.deleteConfirmation ) ) {
				return;
			}

			this.model.destroy();
			this.remove();
		}
	});

	return { RestrictionHistoryView:RestrictionHistoryView, RestrictionHistoryEntryView: RestrictionHistoryEntryView };
} );
