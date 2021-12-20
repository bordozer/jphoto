define( ["backbone"], function ( Backbone ) {

	var PortalPageActivityStreamModel = Backbone.Model.extend( {

		initialize:function ( options ) {
			this.url = Backbone.JPhoto.url( "rest/portal-page/activity-stream/" );
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { PortalPageActivityStreamModel: PortalPageActivityStreamModel };
} );
