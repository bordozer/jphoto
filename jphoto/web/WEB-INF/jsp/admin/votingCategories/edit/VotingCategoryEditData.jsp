<%@ page import="core.services.utils.UrlUtilsServiceImpl" %>
<%@ page import="admin.controllers.votingCategories.edit.VotingCategoryEditDataModel" %>
<%@ page import="core.general.photo.PhotoVotingCategory" %>

<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<jsp:useBean id="votingCategoryEditDataModel" type="admin.controllers.votingCategories.edit.VotingCategoryEditDataModel" scope="request" />

<%
	final PhotoVotingCategory photoVotingCategory = votingCategoryEditDataModel.getPhotoVotingCategory();
%>

<c:set var="votingCategory" value="<%=photoVotingCategory%>" />
<c:set var="isNew" value="<%=photoVotingCategory.isNew()%>" />
<c:set var="entityUrl" value="<%=UrlUtilsServiceImpl.VOTING_CATEGORIES_URL%>" />

<c:set var="votingCategoryIdControl" value="<%=VotingCategoryEditDataModel.VOTING_CATEGORIES_ID_FORM_CONTROL%>" />
<c:set var="votingCategoryNameControl" value="<%=VotingCategoryEditDataModel.VOTING_CATEGORIES_NAME_FORM_CONTROL%>" />
<c:set var="votingCategoryDescriptionControl" value="<%=VotingCategoryEditDataModel.VOTING_CATEGORIES_DESCRIPTION_FORM_CONTROL%>" />
<c:set var="descriptionRequirement" value="${eco:translate( 'Description of voting category' )}" />

<c:set var="mandatoryText" value="<%=votingCategoryEditDataModel.getDataRequirementService().getFieldIsMandatoryText()%>"/>
<c:set var="optionalText" value="<%=votingCategoryEditDataModel.getDataRequirementService().getFieldIsOptionalText()%>"/>

<tags:page pageModel="${votingCategoryEditDataModel.pageModel}">

	<tags:inputHintForm />

	<eco:form action="${eco:baseAdminUrlWithPrefix()}/${entityUrl}/save/">

		<input type="hidden" name="${votingCategoryIdControl}" id="${votingCategoryIdControl}" value="${votingCategory.id}" />

		<table:table width="600" border="0">

			<table:separatorInfo colspan="2" height="50" title="${eco:translate('Genre data')}" />

			<table:tredit>
				<table:tdtext text_t="Name" labelFor="${votingCategoryNameControl}" isMandatory="true" />

				<table:tddata>
					<tags:inputHint inputId="${votingCategoryNameControl}" hintTitle_t="Genre name" hint="${eco:translate( 'Voting category name.' )}<br /><br />${mandatoryText}" focused="true" >
						<jsp:attribute name="inputField">
							<html:input fieldId="${votingCategoryNameControl}" fieldValue="${votingCategory.name}" />
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<table:tdtext text_t="Description" labelFor="${votingCategoryDescriptionControl}" />

				<table:tddata>
					<tags:inputHint inputId="${votingCategoryDescriptionControl}" hintTitle_t="Description" hint="${eco:translate( 'Voting category description.' )}<br /><br />${optionalText}" >
						<jsp:attribute name="inputField">
							<html:textarea inputId="${votingCategoryDescriptionControl}" inputValue="${votingCategory.description}" title="Description" hint="${descriptionRequirement}" />
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<table:trok text_t="${isNew ? 'Create new voting category' : 'Save'}" />

		</table:table>

	</eco:form>

	<tags:springErrorHighliting bindingResult="${votingCategoryEditDataModel.bindingResult}"/>

</tags:page>