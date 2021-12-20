<%@ page contentType="text/javascript" %>

    <%@ taglib prefix="eco" uri="http://taglibs" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <c:set var="baseUrl" value="${eco:baseUrl()}" />

    var require = {

        baseUrl: '${baseUrl}/js',

        config: {
            'core_jphoto': {
                baseUrl: '${baseUrl}'
                , imageFolder: '${eco:imageFolderURL()}'
            }
        },

        paths: {
            jquery: "lib/jquery/jquery-1.10.2"
            , jquery_ui: "lib/jquery/jquery-ui-1.10.4"
            , underscore: "lib/front-end/underscore"
            , backbone: "lib/front-end/backbone"
            , text: "lib/front-end/text"

            , bootstrap: "lib/bootstrap-3.3.2-dist/js/bootstrap.min"

            <%--, jsonrpc: "lib/jsonrpc"--%>

            , jquery_form: "lib/jquery/jquery.form" <%-- TODO: IS it used somewhere? --%>
            , jquery_progressbar: "lib/progressbar/jquery.progressbar"

            , context_menu: "lib/fg.menu/fg.menu"
            , spectrum: "lib/spectrum/spectrum"

            , noty: "lib/noty-2.2.2/jquery.noty.packaged"
            , noty_default: "lib/noty-2.2.2/themes/default"

            , jscal2: "lib/JSCal2-1.9/js/jscal2"
            , jscal2_unicode_letter: "lib/JSCal2-1.9/js/unicode-letter"
            , jscal2_lang: "lib/JSCal2-1.9/js/lang/en"

            , toastmessage: "lib/toastmessage/javascript/jquery.toastmessage"

            , ui_messages: "ui_messages"
            , mass_checker: "components/mass-checker"
            , photosight: "/admin/js/photosight.js"
            , core_jphoto: 'core/core.jphoto'
        },

        shim: {
            backbone: {
                deps: ['underscore', 'jquery'],
                exports: 'Backbone'
            },
            underscore: {
                exports: '_'
            },
            jquery: {
                exports: '$'
            },
            jquery_ui: {
                deps: ['jquery']
            },

            context_menu: {
                deps: ['jquery']
            },
            jquery_progressbar: {
                deps: ['jquery']
            },
            spectrum: {
                deps: ['jquery']
            },

            noty: {
                deps: ['jquery']
            },
            noty_default: {
                deps: ['noty']
            },

            jscal2: {
                deps: ['jquery']
            },
            jscal2_unicode_letter: {
                deps: ['jscal2']
            },
            jscal2_lang: {
                deps: ['jscal2']
            },

            ui_messages: {
                deps: ['jquery']
            },
            mass_checker: {
                deps: ['jquery']
            },
            photosight: {
                deps: ['jquery']
            },
            bootstrap: {
                deps: ['jquery']
            }
        },

        deps: ["jquery", "backbone", "underscore", "text", "core_jphoto", 'bootstrap'],

        callback: function ($, Backbone, _, text, core) {
        }
    };

