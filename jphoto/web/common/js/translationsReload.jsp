<%@page contentType="text/javascript" %>
<%@ page import="org.jabsorb.JSONRPCBridge"%>
<%@ page import="core.context.ApplicationContextHelper"%>
<%@ page import="core.services.translator.TranslatorService"%>

<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "translatorService", ApplicationContextHelper.<TranslatorService>getBean( TranslatorService.BEAN_NAME ) );
%>

var jsonRPC;
jQuery().ready( function () {
	jsonRPC = new JSONRpcClient( "${eco:baseUrl()}/JSON-RPC" );
} );

function reloadTranslations() {

	if ( !confirm( "${eco:translate('Reload translations?')}" ) ) {
		return;
	}

	jsonRPC.translatorService.reloadTranslationsAjax();

	notifySuccessMessage( "${eco:translate('Translations have been reloaded')}" );
}
