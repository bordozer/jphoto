define( ["backbone", "jquery", "underscore", 'jquery_ui'
		, "components/user-picker/user-picker-model"
		, "text!components/user-picker/template/search-form-template.html"
		, "text!components/user-picker/template/user-list-template.html"
		, "text!components/user-picker/template/user-template.html"
		], function ( Backbone, $, _, ui, Model, SearchFormTemplate, UserListTemplate, UserTemplate ) {

	'use strict';

	var UserPickerView = Backbone.View.extend( {

		searchFormTemplate:_.template( SearchFormTemplate ),
		invisibility: 'invisibility',

		events: {
			"keyup .user-picker-filter": "onSearch"
			, "click .user-picker-filter": "onPickerToggle"
			, "click .user-picker-close": "onPickerClose"
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

			var resultContainer = this.$( ".user-list-container" );

			if ( ! this.model.get( 'found' ) ) {
				this.closePicker();
				return;
			}

			this.openPicker();

			resultContainer.html( new UserListView( {
				model: this.model
			} ).render().$el );
		},

		openPicker: function() {
			this.$( ".user-list-container" ).removeClass( this.invisibility );
		},

		closePicker: function() {
			this.$( ".user-list-container" ).addClass( this.invisibility );
		},

		onPickerToggle: function() {
			this.$( ".user-list-container" ).toggleClass( this.invisibility )
		},

		doSearch: function() {
			var searchString = this.$( '.user-picker-filter' ).val();
			if ( searchString.length >= 3 ) {
				this.model.set( { searchString: searchString, userDTOs: [] } );
				this.model.save();

			} else {
				this.closePicker();
			}
		},

		onSearch: function () {
			this.doSearch();
		},

		onPickerClose: function () {
			this.closePicker();
		}
	});

	var UserListView = Backbone.View.extend( {

		userListTemplate:_.template( UserListTemplate ),
		userTemplate:_.template( UserTemplate ),

		events: {
			"click .user-picker-found-entry": "onFoundUserClick"
		},

		initialize: function() {
			this.render();
		},

		render: function () {

			var modelJSON = this.model.toJSON();
			var userListContainer = $( this.userListTemplate( modelJSON ) );

			var userTemplate = this.userTemplate;

			var foundUsers = modelJSON[ 'userDTOs' ];
			_.each( foundUsers, function( user ) {
				userListContainer.append( userTemplate( user ) );
			});

			this.$el.html( userListContainer );

			return this;
		},

		onFoundUserClick: function( evt ) {
			var userId = evt.target.id;
		}
	});

	return { UserPickerView: UserPickerView };
});