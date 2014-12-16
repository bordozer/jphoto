define( ["backbone", "jquery", "underscore", "mass_checker"
		, "components/editableList/entries/team/user-team-model"
		, "text!components/editableList/templates/header-template.html"
		], function ( Backbone, $, _, mass_checker, Model, headerTemplate ) {

	'use strict';

	var EntryListView = Backbone.View.extend( {

		userTeamHeaderTemplate:_.template( headerTemplate ),

		events: {
			"click .create-new-user-team-member-link": "onCreateNewEntry"
		},

		initialize: function( options ) {
			this.entryCompositeView = options.entryCompositeView;

			this.listenTo( this.model, "add", this.renderEntry );

			var model = this.model;
			var el = this.$el;
			this.model.fetch( { cache: false, success: function (  ) {
				_.each( model.selectedIds, function( memberId ) {
					$( '.user-team-member-checkbox-' + memberId, el ).attr( 'checked', 'checked' );
				} );
			} } );
		},

		renderHeader: function () {
			var modelJSON = this.model.toJSON();

			var translationsDTO = this.model[ 'translationDTO' ];
			modelJSON[ 'headerTitleCreateNewMemberButtonTitle' ] = translationsDTO[ 'headerTitleCreateNewMemberButtonTitle' ];

			this.$el.html( this.userTeamHeaderTemplate( modelJSON ) );
		},

		renderEntry: function ( entry ) {

			this.$el.append( this.entryCompositeView.renderEntry( entry ).$el );

			var massSelectorCss = this.model.groupSelectionClass;
			if( massSelectorCss != '' ) {
				var massSelector = mass_checker.getMassChecker();
				var css = massSelectorCss + entry.get( 'entryId' );
				massSelector.registerUnselected( css, Backbone.JPhoto.imageFolder() );
			}
		},

		createEntry: function() {
			this.$el.append( this.entryCompositeView.renderNewEntryForm().$el );
		},

		deleteEntry: function( entry ) {
			this.entryCompositeView.deleteEntry( entry );
		},

		onCreateNewEntry: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.createEntry();
		}
	});

	return { EntryListView: EntryListView };
});