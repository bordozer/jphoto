/* TODO: jphoto in the path is configurable context. Get it from properties */
requirejs.config({
	baseUrl: '/common/js/modules', /* TODO: pass context here */
	paths: {
		jquery: "/common/js/lib/jquery/jquery-1.7.2" /* TODO: pass context here */
		, underscore: "/common/js/lib/front-end/underscore" /* TODO: pass context here */
		, backbone: "/common/js/lib/front-end/backbone" /* TODO: pass context here */
		, text: "/common/js/lib/front-end/text" /* TODO: pass context here */
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

require(["text"], function(){});
