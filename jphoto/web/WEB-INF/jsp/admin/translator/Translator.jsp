<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="translatorModel" type="admin.controllers.translator.TranslatorModel" scope="request"/>

<tags:page pageModel="${translatorModel.pageModel}" >

	<a href="${eco:baseAdminUrlWithPrefix()}/translator/">${eco:translate('Translated')}</a>
	&nbsp;&nbsp;
	<a href="${eco:baseAdminUrlWithPrefix()}/translator/untranslated/">${eco:translate('Untranslated')}</a>

	<br />
	<br />

	<c:forEach var="letter" items="${translatorModel.letters}" >
		<a href="${eco:baseAdminUrlWithPrefix()}/translator/${translatorModel.urlPrefix}/${letter}/">${letter}</a> &nbsp;
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