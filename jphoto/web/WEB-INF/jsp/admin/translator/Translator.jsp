<%@ page import="core.services.translator.Language" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="admin.controllers.translator.TranslationMode" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="translatorModel" type="admin.controllers.translator.TranslatorModel" scope="request"/>

<c:set var="languages" value="<%=Language.getUILanguages()%>"/>
<c:set var="languageNerd" value="<%=Language.NERD%>"/>
<c:set var="translationModes" value="<%=TranslationMode.values()%>" />

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

	<div class="floatleft" style="margin-top: 20px;">

		<div class="left-pane">

			<c:forEach var="translationMode" items="${translationModes}" >
				<c:if test="${translationMode == translatorModel.translationMode}"><span class='selected'></c:if>
				<a href="${eco:baseAdminUrl()}/translator/${translationMode.prefix}/">${eco:translate(translationMode.name)}</a> ( ${translatorModel.modeRecordsCount[translationMode]} )
				<c:if test="${translationMode == translatorModel.translationMode}"></span></c:if>

				&nbsp;&nbsp;&nbsp;&nbsp;

			</c:forEach>

			<br/>
			<br/>

			<c:forEach var="letter" items="${translatorModel.letters}">
				<c:set var="isSelected" value="${translatorModel.filterByLetter == letter}"/>
				<c:if test="${isSelected}"><span class='selected'></c:if>
				<a href="${eco:baseAdminUrl()}/translator/${translatorModel.translationMode.prefix}/letter/${letter}/" class="block-background">${letter}</a>
				<c:if test="${isSelected}"></span></c:if>
				&nbsp;
			</c:forEach>

			<c:if test="${translatorModel.translationMode != 'UNUSED_TRANSLATIONS' }">

				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

				<c:forEach var="language" items="${languages}" >
					<c:if test="${language == translatorModel.language}"><span class='selected'></c:if>
					<a href="${eco:baseAdminUrl()}/translator/${translatorModel.translationMode.prefix}/language/${language.code}/" class="block-background">${eco:translate(language.name)}</a>
					<c:if test="${language == translatorModel.language}"></span></c:if>
					&nbsp;&nbsp;
				</c:forEach>

			</c:if>

			<br/>
			<br/>

			<c:forEach var="entry" items="${translatorModel.translationsMap}">
				<c:set var="nerdKey" value="${entry.key}"/>
				<c:set var="translationData" value="${entry.value}"/>

				<span class="selected base-font-color photo-category-link">${nerdKey.nerd}</span> ( usage index: <b>${translationData.usageIndex}</b> )
				<br/>
				<c:forEach var="translations" items="${translationData.translations}">
					<span title="${eco:translate(translations.translationEntryType.description)}">
						<span class="selected block-background">${translations.language}</span>: "${eco:escapeHtml(translations.value)}"</span>
					<br/>
				</c:forEach>
				<br/>
			</c:forEach>
		</div>

	</div>

</tags:page>