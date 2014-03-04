<%@ page import="core.services.translator.Language" %>
<%@ page import="core.services.translator.TranslatorService" %>
<%@ page import="core.context.ApplicationContextHelper" %>
<%@ page import="org.jabsorb.JSONRPCBridge" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<jsp:useBean id="translatorModel" type="admin.controllers.translator.TranslatorModel" scope="request"/>

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "translatorService", ApplicationContextHelper.<TranslatorService>getBean( TranslatorService.BEAN_NAME ) );
%>

<c:set var="languages" value="<%=Language.values()%>"/>
<c:set var="languageNerd" value="<%=Language.NERD%>"/>

<tags:page pageModel="${translatorModel.pageModel}">
	
	<script type="text/javascript">
		var jsonRPC;
		jQuery().ready( function () {
			jsonRPC = new JSONRpcClient( "${eco:baseUrl()}/JSON-RPC" );
		} );
	</script>

	<style type="text/css">
		.selected {
			font-weight: bold;
		}

		.left-pane {
			float: left;
			width: 40%;
		}

		.right-pane{
			float: left;
			width: 60%;
		}
	</style>

	<c:set var="mode" value="${translatorModel.translationMode}"/>
	<c:set var="isTranslatedMode" value="${mode == 'TRANSLATED'}"/>
	<c:set var="isUntranslatedMode" value="${mode == 'UNTRANSLATED'}"/>

	<c:set var="translatorNerd" value="translatorNerd"/>
	<c:set var="translatorNerdAnchor" value="translatorNerdAnchor"/>

	<div class="floatleft">

		<div class="left-pane">

			<c:if test="${isTranslatedMode}"><span class='selected'></c:if>
			<a href="${eco:baseAdminUrlWithPrefix()}/translator/">${eco:translate('Translated')}</a>
			<c:if test="${isTranslatedMode}"></span></c:if>
			&nbsp;&nbsp;
			<c:if test="${isUntranslatedMode}"><span class='selected'></c:if>
			<a href="${eco:baseAdminUrlWithPrefix()}/translator/untranslated/">${eco:translate('Untranslated')}</a>
			<c:if test="${isUntranslatedMode}"></span></c:if>

			<br/>
			<br/>

			<c:forEach var="letter" items="${translatorModel.letters}">
				<c:set var="isSelected" value="${translatorModel.filterByLetter == letter}"/>
				<c:if test="${isSelected}"><span class='selected'></c:if>
				<a href="${eco:baseAdminUrlWithPrefix()}/translator/${translatorModel.urlPrefix}/${letter}/">${letter}</a>
				<c:if test="${isSelected}"></span></c:if>
				&nbsp;
			</c:forEach>

			<br/>
			<br/>

			<c:forEach var="entry" items="${translatorModel.translationsMap}">
				<c:set var="nerdKey" value="${entry.key}"/>
				<c:set var="translationData" value="${entry.value}"/>

				<a href="#${translatorNerdAnchor}" onclick="placeToTranslatorForm( '${eco:escapeJavaScript(nerdKey.nerd)}' );">${nerdKey.nerd}</a>
				<br/>
				<c:forEach var="translations" items="${translationData.translations}">
					${translations.language}: ${translations.value}
					<br/>
				</c:forEach>
				<br/>
			</c:forEach>
		</div>

		<div class="right-pane">
			<c:if test="${isUntranslatedMode}">

				<script type="text/javascript">
					function placeToTranslatorForm( nerd ) {
						$( "#${translatorNerd}" ).html( nerd );

						var translationDTO = jsonRPC.translatorService.getTranslationAjax( nerd );
						console.log( translationDTO );

						/*$.ajax( {
									type:'GET',
									url:'${eco:baseUrlWithPrefix()}/photo/comment/' + commentId + "/",
									success:function ( response ) {
										var translationDTO = jsonRPC.translatorService.getTranslationAjax( nerd );
										console.log( translationDTO );
									},
									error:function () {
										showErrorMessage( "${eco:translate('Error loading translations')}" );
									}
								} );*/
					}
				</script>

				<table:table width="400px">

					<table:tr>

						<table:td/>
						<table:td>
							<a name="${translatorNerdAnchor}" />
							<div id="${translatorNerd}" class="floatleft" style="height: 30px; font-size: 14px;"></div>
						</table:td>

					</table:tr>

					<c:forEach var="language" items="${languages}">
						<c:if test="${language != languageNerd}">
							<table:tr>

								<table:td>
									${language.code}
								</table:td>
								<table:td>
									<textarea id="translation_${language.code}" rows="5" cols="40"></textarea>
								</table:td>

							</table:tr>
						</c:if>
					</c:forEach>

				</table:table>

			</c:if>
		</div>

	</div>

</tags:page>