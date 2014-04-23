<%@page contentType="text/javascript" %>
<%@ page import="org.jabsorb.JSONRPCBridge"%>
<%@ page import="ui.context.ApplicationContextHelper"%>
<%@ page import="core.services.translator.TranslatorService"%>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "translatorService", ApplicationContextHelper.<TranslatorService>getBean( TranslatorService.BEAN_NAME ) );
%>

var jsonRPC;
define( 'jsonrpc', function( jsonrpc ) {
	jsonRPC = new JSONRpcClient( "${eco:baseUrl()}/JSON-RPC" );
} );

function reloadTranslations() {

	if ( !confirm( "${eco:translate('Reload translations?')}" ) ) {
		return;
	}

	jsonRPC.translatorService.reloadTranslationsAjax();

	notifySuccessMessage( "${eco:translate('Translations have been reloaded')}" );
}
