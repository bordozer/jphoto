define( ["backbone"], function ( Backbone ) {

	var PortalPageBestPhotosModel = Backbone.Model.extend( {

		initialize:function ( options ) {
			this.url = Backbone.JPhoto.url( "rest/portal-page/photos/best/" );
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { PortalPageBestPhotosModel: PortalPageBestPhotosModel };
} );