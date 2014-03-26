<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="customTranslationsModel" type="admin.controllers.translator.custom.CustomTranslationsModel" scope="request"/>

<c:set var="languageValues" value="<%=customTranslationsModel.getLanguages()%>"/>
<c:set var="customTranslatableEntriesMap" value="<%=customTranslationsModel.getCustomTranslatableEntriesMap()%>"/>

<tags:page pageModel="${customTranslationsModel.pageModel}">

	<form:form modelAttribute="customTranslationsModel" method="POST" action="${eco:baseAdminUrlWithPrefix()}/translations/custom/">

		<table:table width="400px">

			<table:separatorInfo colspan="3" title="${eco:translate('System languages')}" />

			<table:tr>
				<table:td>
					${eco:translate('Language')}
				</table:td>
				<table:td/>
				<table:td>
					<form:radiobuttons path="selectedLanguageId" items="${languageValues}" itemValue="id" itemLabel="name" onchange="$( '#customTranslationsModel' ).submit();" delimiter="<br />"/>
				</table:td>
			</table:tr>

			<table:separatorInfo colspan="3" title="${eco:translate('Translations')}" />

			<c:forEach var="entry" items="${customTranslationsModel.selectedLanguageTranslationEntriesMap}">

				<c:set var="customTranslatableEntryId" value="${entry.key}"/>
				<c:set var="translationEntry" value="${entry.value}"/>

				<c:set var="language" value="${translationEntry.language}"/>
				<c:set var="customTranslatableEntryTranslation" value="${customTranslatableEntriesMap[customTranslatableEntryId]}"/>
				<c:set var="fieldId" value="selectedLanguageTranslationEntriesMap['${customTranslatableEntryId}'].translation"/>

				<table:tr>

				<c:if test="${language.id == customTranslationsModel.selectedLanguageId}">
					<table:td>
						<label for="${fieldId}">${customTranslatableEntryTranslation}</label>
					</table:td>
					<table:td> ${language}</table:td>
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
				var form = $( '#customTranslationsModel' );
				form.attr( 'action', '${eco:baseAdminUrlWithPrefix()}/translations/custom/save/' );
				form.submit();
			}
		</script>

	</form:form>

</tags:page>