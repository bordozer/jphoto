<%@ page contentType="text/javascript" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>

var require = {

	baseUrl: '${eco:baseUrl()}/js',

	paths: {
		jquery: "lib/jquery/jquery-1.7.2"
		, underscore: "lib/front-end/underscore"
		, backbone: "lib/front-end/backbone"
		, text: "lib/front-end/text"
	},

	shim: {
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
	},

	deps: ["jquery", "backbone", "underscore", "text"],

	callback: function ( $, Backbone, _, text ) {
	}

};

