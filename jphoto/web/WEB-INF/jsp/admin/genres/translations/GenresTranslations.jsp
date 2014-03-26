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

	<form:form modelAttribute="genresTranslationsModel" method="POST" action="${eco:baseAdminUrlWithPrefix()}/translations/custom/">

		<table:table width="400px">

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
				<c:set var="fieldId" value="translationEntriesMap['${genreId}'].translation"/>

				<table:tr>

				<c:if test="${language.id == genresTranslationsModel.selectedLanguageId}">
					<table:td>
						<label for="${fieldId}">${genre.name} ${language}</label>
					</table:td>
					<table:td>
						<html:input fieldId="${fieldId}" fieldValue="${translationEntry.translation}" />
					</table:td>
				</c:if>

				</table:tr>

			</c:forEach>

			<table:trok text_t="Save translations" onclick="saveTranslations();" />

		</table:table>
		
		<script type="text/javascript">
			function saveTranslations() {
				$( '#genresTranslationsModel' ).attr( 'action', '${eco:baseAdminUrlWithPrefix()}/genres/translations/save/' );
			}
		</script>

	</form:form>

</tags:page>