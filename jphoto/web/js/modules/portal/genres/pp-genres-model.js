define( ["backbone"], function ( Backbone ) {

	var PortalPageGenresModel = Backbone.Model.extend( {

		initialize:function ( options ) {
			this.url = Backbone.JPhoto.url( "rest/portal-page/genres/" );
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { PortalPageGenresModel: PortalPageGenresModel };
} );