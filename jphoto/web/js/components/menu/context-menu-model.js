define( ["backbone"], function ( Backbone ) {

	var ContextMenuModel = Backbone.Model.extend( {

		idAttribute: 'entryId'

		, initialize:function ( options ) {
			this.url = options.baseUrl + "/json/menu/" + options.entryMenuTypeId + "/" + options.entryId + "/";
		}

	});

	return { ContextMenuModel:ContextMenuModel };
} );