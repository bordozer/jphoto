define( ["backbone", "jquery", "underscore", 'jquery_ui'
		, "components/user-picker/user-picker-model"
		, "text!components/user-picker/template/search-form-template.html"
		, "text!components/user-picker/template/search-result-template.html"
		, "text!components/user-picker/template/found-user-template.html"
		], function ( Backbone, $, _, ui, Model, SearchFormTemplate, SearchResultTemplate, FoundUserTemplate ) {

	'use strict';

	var UserPickerView = Backbone.View.extend( {

		searchFormTemplate:_.template( SearchFormTemplate ),
		invisibility: 'invisibility',
		callbackFunction: '',

		events: {
			"keyup .user-picker-filter": "onSearch"
			, "click .user-picker-filter": "onSearchFieldClick"
			, "click .user-picker-close": "onPickerClose"
		},

		initialize: function( options ) {
			this.callbackFunction = options.callbackFunction;

			this.listenTo( this.model, "sync", this.render );

			this.listenTo( this.model, "perform_search", this.performSearch );
			this.listenTo( this.model, "open_search_result", this.openPickerSearchResult );
			this.listenTo( this.model, "close_search_result", this.closePickerSearchResult );

			this.model.fetch( { cache: false } );
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			if( this.$( '.user-picker-filter' ).length == 0 ) {
				this.$el.html( this.searchFormTemplate( modelJSON ) );

				/*var userListContainer = this.$( ".search-result-container" );
				var invisibility = this.invisibility;
				var model = this.model;
				$( document ).click( function ( event ) {
					model.trigger( 'hide_search_result' );
					userListContainer.addClass( invisibility );
				});*/
			}

			this.renderUserList();
		},

		renderUserList: function() {

			var resultContainer = this.$( ".search-result-container" );

			if ( ! this.model.get( 'found' ) ) {
				this.closePickerSearchResult();
				return;
			}

			/*if ( ! this.model.searchResultExpanded ) {
				return;
			}*/

//			this.openPickerSearchResult();

			resultContainer.html( new UserListView( {
				model: this.model
				, callbackFunction: this.callbackFunction
			} ).render().$el );
		},

		openPickerSearchResult: function() {
			this.$( ".search-result-container" ).removeClass( this.invisibility );
		},

		closePickerSearchResult: function() {
			this.$( ".search-result-container" ).addClass( this.invisibility );
		},

		doSearch: function() {
			var searchString = this.$( '.user-picker-filter' ).val();
			if ( searchString.length >= 3 ) {
				this.performSearch();
			} else {
				this.model.closeSearchResult();
			}
		},

		performSearch: function() {
			var searchString = this.$( '.user-picker-filter' ).val();
			this.model.set( { searchString: searchString, userDTOs: [] } );
			this.model.save();
		},

		onSearchFieldClick: function() {
			this.$( ".search-result-container" ).toggleClass( this.invisibility );
			var searchResultExpanded = this.model.searchResultExpanded;

			if ( searchResultExpanded ) {
				this.model.closeSearchResult();
				return;
			}

			if ( this.model.isSearchPerformed() ) {
				this.model.openSearchResult();
				return;
			}

			this.performSearch();
		},

		onSearch: function () {
			this.doSearch();
		},

		onPickerClose: function () {
			this.model.closeSearchResult();
		}
	});

	var UserListView = Backbone.View.extend( {

		searchResultTemplate:_.template( SearchResultTemplate ),
		foundUserTemplate:_.template( FoundUserTemplate ),

		callbackFunction: '',

		events: {
			"click .user-picker-found-entry": "onFoundUserClick"
		},

		initialize: function( options ) {
			this.callbackFunction = options.callbackFunction;
			this.render();
		},

		render: function () {

			var modelJSON = this.model.toJSON();
			var searchResultContainer = $( this.searchResultTemplate( modelJSON ) );

			var userTemplate = this.foundUserTemplate;

			var foundUsers = modelJSON[ 'userDTOs' ];
			_.each( foundUsers, function( user ) {
				searchResultContainer.append( userTemplate( user ) );
			});

			this.$el.html( searchResultContainer );

			return this;
		},

		onFoundUserClick: function( evt ) {
			var modelJSON = this.model.toJSON();

			var userId = evt.target.id;

			this.callbackFunction( userId );
		}
	});

	return { UserPickerView: UserPickerView };
});