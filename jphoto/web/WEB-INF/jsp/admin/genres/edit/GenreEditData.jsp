<%@ page import="core.general.genre.Genre" %>
<%@ page import="admin.controllers.genres.edit.GenreEditDataModel" %>
<%@ page import="utils.EditDataValidationUtils" %>
<%@ page import="core.services.utils.UrlUtilsServiceImpl" %>

<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="genreEditDataModel" type="admin.controllers.genres.edit.GenreEditDataModel" scope="request"/>

<%
	final Genre genre = genreEditDataModel.getGenre();
%>

<c:set var="genre" value="<%=genre%>"/>
<c:set var="isNew" value="<%=genreEditDataModel.isNew()%>"/>

<c:set var="entityUrl" value="<%=UrlUtilsServiceImpl.GENRES_URL%>"/>
<c:set var="genreIdControl" value="<%=GenreEditDataModel.GENRE_EDIT_DATA_ID_FORM_CONTROL%>"/>
<c:set var="genreNameControl" value="<%=GenreEditDataModel.GENRE_EDIT_DATA_NAME_FORM_CONTROL%>"/>
<c:set var="genreMinMarksControl" value="<%=GenreEditDataModel.GENRE_EDIT_MIN_MARKS_FORM_CONTROL%>"/>
<c:set var="genreDescriptionControl" value="<%=GenreEditDataModel.GENRE_EDIT_DATA_DESCRIPTION_FORM_CONTROL%>"/>

<c:set var="allowedVotingCategoriesControl" value="<%=GenreEditDataModel.GENRE_EDIT_DATA_ALLOWED_VOTING_CATEGORIES_FORM_CONTROL%>"/>

<c:set var="minMarksRequirement" value="${eco:translate( 'Photos of this ganre has to have minimal marks to be in genre\\\'s best. <br />0 - use default system\\\'s value.' )}"/>
<c:set var="descriptionRequirement" value="${eco:translate( 'Any information about genre' )}"/>

<c:set var="mandatoryText" value="<%=EditDataValidationUtils.getFieldIsMandatoryText()%>"/>
<c:set var="optionalText" value="<%=EditDataValidationUtils.getFieldIsOptionalText()%>"/>

<tags:page pageModel="${genreEditDataModel.pageModel}">

	<tags:inputHintForm/>

	<eco:form action="${eco:baseAdminUrlWithPrefix()}/${entityUrl}/save/">

		<input type="hidden" name="${genreIdControl}" id="${genreIdControl}" value="${genre.id}"/>

		<table:table width="680" border="0">

			<table:separatorInfo colspan="2" height="50" title="${eco:translate('Genre data')}"/>

			<table:tredit>
				<table:tdtext text_t="Name" labelFor="${genreNameControl}" isMandatory="true"/>

				<table:tddata>
					<tags:inputHint inputId="${genreNameControl}" hintTitle_t="Genre name"
									hint="${eco:translate( 'Genre name.' )}<br /><br />${mandatoryText}" focused="true">
						<jsp:attribute name="inputField">
							<html:input fieldId="${genreNameControl}" fieldValue="${genre.name}"/>
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<table:tdtext text_t="Allowed voiting categories" labelFor="allowedVotingCategoryIDs1"/>

				<table:tddata>
					<form:checkboxes items="${genreEditDataModel.photoVotingCategories}"
									 path="genreEditDataModel.${allowedVotingCategoriesControl}"
									 itemLabel="name" itemValue="id" delimiter="<br/>" htmlEscape="false"/>
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<table:tdtext text_t="Minimal mark" labelFor="${genreMinMarksControl}"/>

				<table:tddata>
					<tags:inputHint inputId="${genreMinMarksControl}" hintTitle_t="Minimal marks"
									hint="${minMarksRequirement}<br /><br />${optionalText}">
						<jsp:attribute name="inputField">
							<html:input fieldId="${genreMinMarksControl}"
										fieldValue="${genreEditDataModel.minMarksForBest}" size="3" maxLength="3"/>
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<table:separator colspan="2" />

			<table:tredit>
				<table:tdtext text_t="Can contain nude content" labelFor="canContainNudeContent1"/>

				<table:tddata>
					<form:checkbox path="genreEditDataModel.canContainNudeContent"/>
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<table:tdtext text_t="Contains nude content" labelFor="containsNudeContent1"/>

				<table:tddata>
					<form:checkbox path="genreEditDataModel.containsNudeContent"/>
				</table:tddata>
			</table:tredit>

			<table:separator colspan="2" />

			<table:tredit>
				<table:tdtext text_t="Description" labelFor="${genreDescriptionControl}"/>

				<table:tddata>
					<tags:inputHint inputId="${genreDescriptionControl}" hintTitle_t="Description"
									hint="${eco:translate( 'Genre description.' )}<br /><br />${optionalText}">
									<jsp:attribute name="inputField">
										<html:textarea inputId="${genreDescriptionControl}"
													   inputValue="${genre.description}"
													   title="Description" hint="${descriptionRequirement}"/>
									</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<table:trok text_t="${isNew ? 'Create new genre' : 'Save'}"/>

		</table:table>

	</eco:form>

	<tags:springErrorHighliting bindingResult="${genreEditDataModel.bindingResult}"/>

</tags:page>