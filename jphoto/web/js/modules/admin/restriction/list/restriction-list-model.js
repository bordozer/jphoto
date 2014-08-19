define( ["backbone"], function ( Backbone ) {

	var RestrictionListModel = Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = Backbone.JPhoto.url( "rest/admin/restrictions/search/" );
		}

	 } );

	return { RestrictionListModel: RestrictionListModel };
} );
