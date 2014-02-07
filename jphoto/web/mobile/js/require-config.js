requirejs.config({
	baseUrl: 'mobile/js/modules',
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
