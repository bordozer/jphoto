<%@ page contentType="text/javascript" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>

var require = {

	baseUrl: '${eco:baseUrl()}/js',

	paths: {
		jquery: "lib/jquery/jquery-1.7.2"
		, underscore: "lib/front-end/underscore"
		, backbone: "lib/front-end/backbone"
		, text: "lib/front-end/text"
		, context_menu: "lib/fg.menu/fg.menu"
		, superfish: "lib/superfish/js/superfish"
		, dialog: "lib/jquery/jquery-ui-1.8.21.custom.min"
	},

	shim: {
		'backbone': {
			deps: ['underscore', 'jquery', 'context_menu', 'superfish', 'dialog'],
			exports: 'Backbone'
		},
		underscore: {
			exports: '_'
		},
		jquery: {
			exports: '$'
		}
	},

	deps: ["jquery", "backbone", "underscore", "text"],

	callback: function ( $, Backbone, _, text ) {
	}

};

