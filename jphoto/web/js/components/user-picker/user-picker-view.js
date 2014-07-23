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
		searchResultContainer: '.search-result-container',

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

			var forceCloseSearchResult = false;
			if( this.$( '.user-picker-filter' ).length == 0 ) {
				this.$el.html( this.searchFormTemplate( modelJSON ) );
				forceCloseSearchResult = true;

				/*var model = this.model;
				$( 'body' ).click( function ( evt ) {
					evt.preventDefault();
					evt.stopImmediatePropagation();

					model.closeSearchResult();
				});*/
			}

			this.renderUserList( forceCloseSearchResult );
		},

		renderUserList: function( forceCloseSearchResult ) {

			var modelJSON = this.model.toJSON();
			var isFound = modelJSON[ 'userDTOs' ].length > 0;

			if ( ! isFound ) {
//				this.closePickerSearchResult();
				this.nothingFound();
				return;
			}

			if ( ! forceCloseSearchResult ) {
				this.model.openSearchResult();
			}

			var resultContainer = this.$( this.searchResultContainer );
			resultContainer.html( new UserListView( {
				model: this.model
				, callbackFunction: this.callbackFunction
			} ).render().$el );
		},

		openPickerSearchResult: function() {
			this.$( this.searchResultContainer ).removeClass( this.invisibility );
		},

		closePickerSearchResult: function() {
			this.$( this.searchResultContainer ).addClass( this.invisibility );
		},

		doSearch: function() {
			if ( this.getSearchValue().length >= 3 ) {
				this.performSearch();
			} else {
				this.model.closeSearchResult();
			}
		},

		performSearch: function() {
			this.showSpinningWheel();

			this.model.set( { searchString: this.getSearchValue(), userDTOs: [] } );
			this.model.save();
		},

		nothingFound: function() {
			this.model.openSearchResult();
			this.$( this.searchResultContainer ).html( 'Nothing found' )
		},

		showSpinningWheel: function() {
			var wheel = this.model.get( 'baseUrl' ) + "/images/progress.gif";
			this.$( this.searchResultContainer ).html( "<img src='" + wheel +"' width='16' height='16'>" );
			this.model.openSearchResult();
		},

		getSearchValue: function() {
			return this.$( '.user-picker-filter' ).val();
		},

		onSearchFieldClick: function() {
			this.$( this.searchResultContainer ).toggleClass( this.invisibility );
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

			this.callbackFunction( { userId: userId } );
		}
	});

	return { UserPickerView: UserPickerView };
});