define( ["backbone"], function ( Backbone ) {

	var PortalPageModel = Backbone.Model.extend( {

		initialize:function ( options ) {

			this.dateOptions = options.dateOptions;

			this.url = Backbone.JPhoto.url( "rest/portal-page/" );
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { PortalPageModel: PortalPageModel };
} );