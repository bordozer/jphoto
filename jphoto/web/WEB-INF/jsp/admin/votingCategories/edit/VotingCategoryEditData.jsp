<%@ page import="core.services.utils.UrlUtilsServiceImpl" %>
<%@ page import="admin.controllers.votingCategories.edit.VotingCategoryEditDataModel" %>

<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<jsp:useBean id="votingCategoryEditDataModel" type="admin.controllers.votingCategories.edit.VotingCategoryEditDataModel" scope="request" />

<c:set var="isNew" value="<%=votingCategoryEditDataModel.isNew()%>" />
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

		<input type="hidden" name="${votingCategoryIdControl}" id="${votingCategoryIdControl}" value="${votingCategoryEditDataModel.votingCategoryId}" />

		<table:table width="600" border="0">

			<table:separatorInfo colspan="2" height="50" title="${eco:translate('Voting category data')}" />

			<table:tredit>
				<table:tdtext text_t="Voting category name" labelFor="${votingCategoryNameControl}" isMandatory="true" />

				<table:tddata>
					<tags:inputHint inputId="${votingCategoryNameControl}" hintTitle_t="Voting category name" hint="${eco:translate( 'Voting category name' )}<br /><br />${mandatoryText}" focused="true" >
						<jsp:attribute name="inputField">
							<html:input fieldId="${votingCategoryNameControl}" fieldValue="${votingCategoryEditDataModel.votingCategoryName}" />
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<table:tdtext text_t="Voting category description" labelFor="${votingCategoryDescriptionControl}" />

				<table:tddata>
					<tags:inputHint inputId="${votingCategoryDescriptionControl}" hintTitle_t="Voting category description" hint="${eco:translate( 'Voting category description' )}<br /><br />${optionalText}" >
						<jsp:attribute name="inputField">
							<html:textarea inputId="${votingCategoryDescriptionControl}" inputValue="${votingCategoryEditDataModel.votingCategoryDescription}" title="Description" hint="${descriptionRequirement}" />
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<table:trok text_t="${isNew ? 'Create new voting category' : 'Save voting category '}" />

		</table:table>

	</eco:form>

	<tags:springErrorHighliting bindingResult="${votingCategoryEditDataModel.bindingResult}"/>

</tags:page>