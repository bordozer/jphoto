<%@ page contentType="text/javascript" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>

var require = {

	baseUrl: '${eco:baseUrl()}/js',

	paths: {
		jquery: "lib/jquery/jquery-1.10.2"
		, jquery_ui: "lib/jquery/jquery-ui-1.10.4"
		, underscore: "lib/front-end/underscore"
		, backbone: "lib/front-end/backbone"
		, text: "lib/front-end/text"

		, jsonrpc: "lib/jsonrpc"

		, jquery_alerts: "lib/jalert/jquery.alerts" <%-- TODO: IS it used somewhere? --%>
		, jquery_form: "lib/jquery/jquery.form" <%-- TODO: IS it used somewhere? --%>

		, context_menu: "lib/fg.menu/fg.menu"

		, superfish: "lib/superfish/js/superfish"
		, superfish_hoverIntent: "lib/superfish/js/hoverIntent"

		, lightbox: "lib/lightbox/lightbox"
		, noty: "lib/noty-2.2.2/js/noty/packaged/jquery.noty.packaged"
		, noty_default: "lib/noty-2.2.2/js/noty/themes/default"

		, jscal2: "lib/JSCal2-1.9/js/jscal2"
		, jscal2_unicode_letter: "lib/JSCal2-1.9/js/unicode-letter"
		, jscal2_lang: "lib/JSCal2-1.9/js/lang/en"

		, ui_messages: "ui_messages"
		, toastmessage: "lib/toastmessage/javascript/jquery.toastmessage"
	},

	shim: {
		'backbone': {
			deps: [ 'underscore', 'jquery' ],
			exports: 'Backbone'
		},
		underscore: {
			exports: '_'
		},
		jquery: {
			exports: '$'
		},
		jquery_alerts: {
			deps: [ 'jquery' ]
		},
		jquery_ui: {
			deps: [ 'jquery' ]
		},

		lightbox: {
			deps: [ 'jquery' ]
		},
		context_menu: {
			deps: [ 'jquery' ]
		},
		superfish: {
			deps: [ 'jquery' ]
		},
		superfish_hoverIntent: {
			deps: [ 'superfish' ]
		},
		noty: {
			deps: [ 'jquery' ]
		},
		noty_default: {
			deps: [ 'noty' ]
		},
		jscal2: {
			deps: [ 'jquery' ]
		},
		jscal2_unicode_letter: {
			deps: [ 'jscal2' ]
		},
		jscal2_lang: {
			deps: [ 'jscal2' ]
		}
	},

	deps: ["jquery", "backbone", "underscore", "text"],

	callback: function ( $, Backbone, _, text ) {
	}

};

