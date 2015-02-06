define( function ( require, exports, module ) {

	var Backbone = require('backbone');
	var _ = require('underscore');
	var ajaxRpc = require('jsonrpc');

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

	Backbone.JPhoto = new JPhoto( module.config() );
} );
