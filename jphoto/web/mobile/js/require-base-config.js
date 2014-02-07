requirejs.config({
	baseUrl: '/jphoto/mobile/js/modules',
	paths: {
		jquery: "../../../common/js/lib/jquery/jquery-1.7.2"
		, underscore: "../lib/underscore"
		, backbone: "../lib/backbone"
	},
	shim: { /* config for lib those are not defined af modules */
		'backbone': {
			deps: ['underscore', 'jquery'],
			exports: 'Backbone'
		},
		underscore: {
			exports: '_'
		},
		jquery: {
			exports: '$'
		}
	}
});
