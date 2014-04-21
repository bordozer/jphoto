define( ["backbone"], function ( Backbone ) {

	/*var ContextMenuItemModel = Backbone.Model.extend( {

	});

	var ContextMenuItemsModel = Backbone.Collection.extend( {

		model: ContextMenuItemModel

		, initialize:function ( options ) {
			this.url = options.baseUrl + "/json/menu/" + options.entryMenuTypeId + "/" + options.entryId + "/items/";
		}
	});*/

	var ContextMenuModel = Backbone.Model.extend( {

		idAttribute: 'entryId'

		, initialize:function ( options ) {
			this.url = options.baseUrl + "/json/menu/" + options.entryMenuTypeId + "/" + options.entryId + "/";
		}
	});

	return { ContextMenuModel:ContextMenuModel };
} );