<%@ page contentType="text/javascript" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>

var require = {

	baseUrl: '${eco:baseUrl()}/js',

	paths: {
		jquery: "lib/jquery/jquery-1.10.2"
		, dialog: "lib/jquery/jquery-ui-1.10.4"
		, underscore: "lib/front-end/underscore"
		, backbone: "lib/front-end/backbone"
		, text: "lib/front-end/text"
		, context_menu: "lib/fg.menu/fg.menu"
		, superfish: "lib/superfish/js/superfish"
		, lightbox: "lib/lightbox/lightbox"
		, noty: "lib/noty-2.2.2/js/noty/packaged/jquery.noty.packaged"
		, noty_default: "lib/noty-2.2.2/js/noty/themes/default"
	},

	shim: {
		'backbone': {
			deps: ['underscore', 'jquery', 'context_menu', 'superfish', 'dialog', 'lightbox', 'noty_default'],
			exports: 'Backbone'
		},
		underscore: {
			exports: '_'
		},
		jquery: {
			exports: '$'
		},
		noty: {
			deps: [ 'jquery' ]
		},
		noty_default: {
			deps: [ 'noty' ]
		},
		lightbox: {
			deps: [ 'jquery' ]
		}
	},

	deps: ["jquery", "backbone", "underscore", "text"],

	callback: function ( $, Backbone, _, text ) {
	}

};

