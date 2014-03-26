<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="translations" tagdir="/WEB-INF/tags/links/translations" %>

<jsp:useBean id="votingCategoryListModel" type="admin.controllers.votingCategories.list.VotingCategoryListModel" scope="request" />

<c:set var="votingCategories" value="<%=votingCategoryListModel.getPhotoVotingCategories()%>"/>

<c:set var="colspan" value="5"/>
<c:set var="separatorHeight" value="1"/>

<c:set var="newVotingCategoryHint" value="${eco:translate( 'Create new voting category' )}"/>
<c:set var="deleteButtonHint" value="${eco:translate( 'Delete voting category' )}"/>

<tags:page pageModel="${votingCategoryListModel.pageModel}">

	<links:votingCategoryNew>
		<html:img id="addNewVotingCategoryIcon" src="add32.png" width="32" height="32" alt="${newVotingCategoryHint}" />
	</links:votingCategoryNew>

	<translations:votingCategories>
		<html:img32 src="icons32/translate.png" alt="${eco:translate('Voting categories translations')}" />
	</translations:votingCategories>

	<br/>
	<br/>

	<table:table width="800">

		<jsp:attribute name="thead">
			<table:th>&nbsp;</table:th>
			<table:th>&nbsp;</table:th>
			<table:th text_t="id" width="20" />
			<table:th text_t="Name" width="200" />
			<table:th text_t="Description" />
		</jsp:attribute>

		<jsp:body>
			<table:separator colspan="${colspan}" height="${separatorHeight}"/>

			<c:forEach var="votingCategory" items="${votingCategories}">

				<table:tr>

					<table:tdicon>
						<links:votingCategoryEdit id="${votingCategory.id}">
							<html:img id="editVotingCategoryIcon" src="edit16.png" width="16" height="16" alt="${eco:translate('Edit')}" />
						</links:votingCategoryEdit>
					</table:tdicon>

					<table:tdicon>
						<links:votingCategoryDelete id="${votingCategory.id}">
							<html:img id="deleteVotingCategoryIcon" src="delete16.png" width="16" height="16" alt="${deleteButtonHint}" onclick="return confirmDeletion( 'Delete ${votingCategory.name}?' );" />
						</links:votingCategoryDelete>
					</table:tdicon>

					<table:tdicon>${votingCategory.id}</table:tdicon>
					<table:td>${votingCategory.name}</table:td>
					<table:td>${votingCategory.description}</table:td>
				</table:tr>

				<table:separator colspan="${colspan}" height="${separatorHeight}"/>

			</c:forEach>
		</jsp:body>
	</table:table>

	<js:confirmAction />

</tags:page>