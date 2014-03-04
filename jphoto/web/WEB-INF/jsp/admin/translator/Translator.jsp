<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="translatorModel" type="admin.controllers.translator.TranslatorModel" scope="request"/>

<tags:page pageModel="${translatorModel.pageModel}" >

	<style type="text/css">
		.selected {
			/*text-decoration: underline;*/
			font-weight: bold;
		}
	</style>

	<c:set var="mode" value="${translatorModel.translationMode}"/>
	<c:if test="${mode == 'TRANSLATED'}"><span class='selected'></c:if><a href="${eco:baseAdminUrlWithPrefix()}/translator/">${eco:translate('Translated')}</a><c:if test="${mode == 'TRANSLATED'}"></span></c:if>
	&nbsp;&nbsp;
	<c:if test="${mode == 'UNTRANSLATED'}"><span class='selected'></c:if><a href="${eco:baseAdminUrlWithPrefix()}/translator/untranslated/">${eco:translate('Untranslated')}</a><c:if test="${mode == 'UNTRANSLATED'}"></span></c:if>

	<br />
	<br />

	<c:forEach var="letter" items="${translatorModel.letters}" >
		<c:set var="isSelected" value="${translatorModel.filterByLetter == letter}"/>
		<c:if test="${isSelected}"><span class='selected'></c:if><a href="${eco:baseAdminUrlWithPrefix()}/translator/${translatorModel.urlPrefix}/${letter}/">${letter}</a><c:if test="${isSelected}"></span></c:if>
		 &nbsp;
	</c:forEach>

	<br />
	<br />

	<c:forEach var="entry" items="${translatorModel.translationsMap}">
		<c:set var="nerdKey" value="${entry.key}"/>
		<c:set var="translationData" value="${entry.value}"/>

		${nerdKey.nerd}
		<br />
		<c:forEach var="translations" items="${translationData.translations}">
			${translations.language}: ${translations.value}
			<br />
		</c:forEach>
		<br />
	</c:forEach>

</tags:page>