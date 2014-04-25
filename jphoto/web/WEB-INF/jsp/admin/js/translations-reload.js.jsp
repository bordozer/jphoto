<%@page contentType="text/javascript" %>
<%@ page import="org.jabsorb.JSONRPCBridge"%>
<%@ page import="ui.context.ApplicationContextHelper"%>
<%@ page import="core.services.translator.TranslatorService"%>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "translatorService", ApplicationContextHelper.<TranslatorService>getBean( TranslatorService.BEAN_NAME ) );
%>

define( [ 'jquery', 'jsonrpc' ], function ( $ ) {

	return {

		reloadTranslations: function () {

			var jsonRPC = new JSONRpcClient( "${eco:baseUrl()}/JSON-RPC" );

			if ( !confirm( "${eco:translate('Reload translations?')}" ) ) {
				return;
			}

			jsonRPC.translatorService.reloadTranslationsAjax();

			showUIMessage_Notification( "${eco:translate('Translations have been reloaded')}" );
		}
	}
});
