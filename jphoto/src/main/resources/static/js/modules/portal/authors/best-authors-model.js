define( ["backbone"], function ( Backbone ) {

	var PortalPageBestAuthorsModel = Backbone.Model.extend( {

		initialize:function ( options ) {

			this.dateFrom = options.dateFrom;
			this.dateTo = options.dateTo;

			this.url = Backbone.JPhoto.url( "rest/portal-page/authors/best/" + this.dateFrom + '/' + this.dateTo + '/' );
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { PortalPageBestAuthorsModel: PortalPageBestAuthorsModel };
} );
