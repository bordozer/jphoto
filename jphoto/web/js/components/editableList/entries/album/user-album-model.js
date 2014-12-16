define( [ "components/editableList/editable-list-model" ], function ( EditableListModel ) {

	var UserAlbumModel = EditableListModel.EditableListEntryModel.extend( {
		defaults: _.extend( {}, EditableListModel.EditableListEntryModel.prototype.defaults, {} )
	} );

	var UserAlbumsModel = EditableListModel.EditableListModel.extend( {

		model: UserAlbumModel,

		initialize: function ( options ) {
			UserAlbumsModel.__super__.initialize.apply( this, arguments );

			this.url = Backbone.JPhoto.url( "rest/users/" + options.userId + "/albums/" );
		}
	} );

	return { UserAlbumsModel: UserAlbumsModel, UserAlbumModel: UserAlbumModel };
} );
