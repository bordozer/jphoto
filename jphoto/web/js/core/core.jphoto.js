define( [ 'backbone', 'underscore' ], function ( Backbone, _ ) {

	var JPhoto = function ( options ) {
		this.options = _.extend( this.defaults, options );
	};

	_.extend( JPhoto.prototype, {

		defaults: {
			baseUrl: '/'
		},

		url: function ( path ) {
			return this.options.baseUrl + '/' +  path;
		},

		ajaxService: function() {
			return this.options.ajaxService;
		},

		imageFolder: function() {
			return this.options.imageFolder;
		},

		translate: function( nerd ) {
			return this.options.ajaxService.translate( nerd );
		}
	} );

	return function ( options ) {
		Backbone.JPhoto = new JPhoto( options );
	}
} );
