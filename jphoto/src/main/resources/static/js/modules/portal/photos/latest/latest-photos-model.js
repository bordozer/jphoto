define( ["backbone"], function ( Backbone ) {

	var PortalPageLatestPhotosModel = Backbone.Model.extend( {

		initialize:function ( options ) {
			this.url = Backbone.JPhoto.url( "rest/portal-page/photos/latest/" );
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { PortalPageLatestPhotosModel: PortalPageLatestPhotosModel };
} );
