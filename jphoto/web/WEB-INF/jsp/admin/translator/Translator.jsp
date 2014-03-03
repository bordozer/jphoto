<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="translatorModel" type="admin.controllers.translator.TranslatorModel" scope="request"/>

<tags:page pageModel="${translatorModel.pageModel}" >

	<c:forEach var="entry" items="${translatorModel.translationsMap}">
		<c:set var="nerd" value="${entry.key}"/>
		<c:set var="translationData" value="${entry.value}"/>

		${nerd}
		<br />
		<c:forEach var="translations" items="${translationData.translations}">
			${translations.language}: ${translations.value}
			<br />
		</c:forEach>
		<br />
	</c:forEach>

</tags:page>