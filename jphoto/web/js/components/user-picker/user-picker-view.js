define( ["backbone", "jquery", "underscore", 'jquery_ui'
		, "components/user-picker/user-picker-model"
		, "text!components/user-picker/template/search-form-template.html"
		, "text!components/user-picker/template/user-list-template.html"
		, "text!components/user-picker/template/user-template.html"
		], function ( Backbone, $, _, ui, Model, SearchFormTemplate, UserListTemplate, UserTemplate ) {

	'use strict';

	var UserPickerView = Backbone.View.extend( {

		searchFormTemplate:_.template( SearchFormTemplate ),

		events: {
			"keyup.user-picker-filter": "onSearch"
		},

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
			this.model.fetch( { cache: false } );
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			if( this.$( '.user-picker-filter' ).length == 0 ) {
				this.$el.html( this.searchFormTemplate( modelJSON ) );
			}

			this.renderUserList();
		},

		renderUserList: function() {

//			var modelJSON = this.model.toJSON();

			var resultContainer = this.$( ".user-list-container" );
			if ( ! this.model.get( 'found' ) ) {
				this.clearResult();
				return;
			}

			resultContainer.html( new UserListView( {
				model: this.model
			} ).render().$el );
		},

		clearResult: function() {
			this.$( ".user-list-container" ).html( 'Nothing is found' );
		},

		doSearch: function() {
			var searchString = this.$( '.user-picker-filter' ).val();
			if ( searchString.length >= 3 ) {
				this.model.set( { searchString: searchString, userDTOs: [] } );
				this.model.save();
			} else {
				this.clearResult();
			}
		},

		onSearch: function () {
			this.doSearch();
		}
	});

	var UserListView = Backbone.View.extend( {

		userListTemplate:_.template( UserListTemplate ),
		userTemplate:_.template( UserTemplate ),

		initialize: function() {
			this.render();
		},

		render: function () {

			var modelJSON = this.model.toJSON();
			this.$el.html( this.userListTemplate( modelJSON ) );

			var el = this.$el;
			var userTemplate = this.userTemplate;

			var foundUsers = modelJSON[ 'userDTOs' ];
			_.each( foundUsers, function( user ) {
				el.append( userTemplate( user ) );
			});

			return this;
		}
	});

	return { UserPickerView: UserPickerView };
});