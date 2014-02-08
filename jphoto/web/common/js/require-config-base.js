/* TODO: jphoto in the path is configurable context. Get it from properties */
requirejs.config({
	baseUrl: '/jphoto/common/js/modules',
	paths: {
		jquery: "/jphoto/common/js/lib/jquery/jquery-1.7.2"
		, underscore: "/jphoto/common/js/lib/front-end/underscore"
		, backbone: "/jphoto/common/js/lib/front-end/backbone"
		, text: "/jphoto/common/js/lib/front-end/text"
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
