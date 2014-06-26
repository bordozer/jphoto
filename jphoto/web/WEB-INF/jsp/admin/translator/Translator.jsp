<%@ page import="core.services.translator.Language" %>
<%@ page import="java.util.Arrays" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="translatorModel" type="admin.controllers.translator.TranslatorModel" scope="request"/>

<c:set var="languages" value="<%=Language.getUILanguages()%>"/>
<c:set var="languageNerd" value="<%=Language.NERD%>"/>

<tags:page pageModel="${translatorModel.pageModel}">
	
	<style type="text/css">
		.selected {
			font-weight: bold;
		}

		.left-pane {
			float: left;
			width: 100%;
		}

	</style>

	<c:set var="mode" value="${translatorModel.translationMode}"/>
	<c:set var="isTranslatedMode" value="${mode == 'TRANSLATED'}"/>
	<c:set var="isUntranslatedMode" value="${mode == 'UNTRANSLATED'}"/>

	<c:set var="translatorNerd" value="translatorNerd"/>
	<c:set var="translatorNerdAnchor" value="translatorNerdAnchor"/>

	<div class="floatleft" style="margin-top: 20px;">

		<div class="left-pane">

			<c:if test="${isTranslatedMode}"><span class='selected'></c:if>
			<a href="${eco:baseAdminUrl()}/translator/translated/">${eco:translate('Translator: Translated')}</a>
			<c:if test="${isTranslatedMode}"></span></c:if>
			&nbsp;&nbsp;
			<c:if test="${isUntranslatedMode}"><span class='selected'></c:if>
			<a href="${eco:baseAdminUrl()}/translator/untranslated/">${eco:translate('Translator: Untranslated')}</a>
			<c:if test="${isUntranslatedMode}"></span></c:if>

			<br/>
			<br/>

			<c:forEach var="letter" items="${translatorModel.letters}">
				<c:set var="isSelected" value="${translatorModel.filterByLetter == letter}"/>
				<c:if test="${isSelected}"><span class='selected'></c:if>
				<a href="${eco:baseAdminUrl()}/translator/${translatorModel.translationMode.prefix}/letter/${letter}/">${letter}</a>
				<c:if test="${isSelected}"></span></c:if>
				&nbsp;
			</c:forEach>

			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

			<c:forEach var="language" items="${languages}" >
				<a href="${eco:baseAdminUrl()}/translator/${translatorModel.translationMode.prefix}/language/${language.code}/"> ${eco:translate(language.name)}</a> &nbsp;&nbsp;
			</c:forEach>

			<br/>
			<br/>

			<c:forEach var="entry" items="${translatorModel.translationsMap}">
				<c:set var="nerdKey" value="${entry.key}"/>
				<c:set var="translationData" value="${entry.value}"/>

				<a href="#${translatorNerdAnchor}" onclick="placeToTranslatorForm( '${eco:escapeJavaScript(nerdKey.nerd)}' );">${nerdKey.nerd}</a>
				<br/>
				<c:forEach var="translations" items="${translationData.translations}">
					<span title="${eco:translate(translations.translationEntryType.description)}">${translations.language}: "${eco:escapeHtml(translations.value)}"</span>
					<br/>
				</c:forEach>
				<br/>
			</c:forEach>
		</div>

		<%--<div class="right-pane">

			<script type="text/javascript">
				function placeToTranslatorForm( nerd ) {
					$( "#${translatorNerd}" ).html( nerd );

					var translationDTO = jsonRPC.translatorService.getTranslationAjax( nerd );

					var map = translationDTO.translations.map;
					for( languageCode in map ) {
						$( "#translation_" + languageCode ).text( map[ languageCode ] );
					}
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

				<table:tr>
					<table:td colspan="2">
						<admin:reloadTranslationsButton />
					</table:td>
				</table:tr>

			</table:table>

		</div>--%>

	</div>

</tags:page>