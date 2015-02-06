<%@page contentType="text/javascript" %>
<%@ page import="org.jabsorb.JSONRPCBridge"%>
<%@ page import="ui.context.ApplicationContextHelper"%>
<%@ page import="core.services.translator.TranslatorService"%>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

define( [ 'jquery' ], function ( $ ) {

	return {

		reloadTranslations: function () {

			if ( !confirm( "${eco:translate('Reload translations?')}" ) ) {
				return;
			}

			jsonRPC.ajaxService.reloadTranslationsAjax();

			showUIMessage_Notification( "${eco:translate('Translations have been reloaded')}" );
		}
	}
});
