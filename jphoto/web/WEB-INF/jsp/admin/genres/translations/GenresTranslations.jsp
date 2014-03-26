<%@ page import="core.services.translator.Language" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="genresTranslationsModel" type="admin.controllers.genres.translations.GenresTranslationsModel" scope="request"/>

<c:set var="languageValues" value="<%=genresTranslationsModel.getLanguages()%>"/>
<c:set var="genreMap" value="<%=genresTranslationsModel.getGenreMap()%>"/>

<tags:page pageModel="${genresTranslationsModel.pageModel}">

	<form:form modelAttribute="genresTranslationsModel" method="POST">

		<table:table width="300px">

			<table:tr>
				<table:td>
					${eco:translate('Language')}
				</table:td>
				<table:td>
					<form:radiobuttons path="selectedLanguageId" items="${languageValues}" itemValue="id" itemLabel="name" onchange="$( '#genresTranslationsModel' ).submit();" delimiter="<br />"/>
				</table:td>
			</table:tr>

			<c:forEach var="entry" items="${genresTranslationsModel.translationEntriesMap}">
				<c:set var="genreId" value="${entry.key}"/>
				<c:set var="translationEntry" value="${entry.value}"/>

				<c:set var="language" value="${translationEntry.language}"/>
				<c:set var="genre" value="${genreMap[genreId]}"/>
				<c:set var="fieldId" value="translationEntriesMap['${genreId}'].value"/>

				<c:if test="${language.id == genresTranslationsModel.selectedLanguageId}">
					<table:tr>
						<table:td>
							<label for="${fieldId}">${genre.name}</label>
						</table:td>
						<table:td>
							<html:input fieldId="${fieldId}" fieldValue="${translationEntry.value}" />
						</table:td>
					</table:tr>
				</c:if>

			</c:forEach>
		</table:table>

	</form:form>

</tags:page>