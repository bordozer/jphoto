<%@page contentType="text/javascript" %>
    <%@ page import="com.bordozer.jphoto.core.services.translator.TranslatorService"%>
    <%@ page import="com.bordozer.jphoto.ui.context.ApplicationContextHelper"%>
    <%@ page import="org.jabsorb.JSONRPCBridge"%>

    <%@ taglib prefix="eco" uri="http://taglibs" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    define(['jquery'], function ($) {

        return {

            reloadTranslations: function () {

                if (!confirm("${eco:translate('Reload translations?')}")) {
                    return;
                }

                jsonRPC.ajaxService.reloadTranslationsAjax();

                showUIMessage_Notification("${eco:translate('Translations have been reloaded')}");
            }
        }
    });
