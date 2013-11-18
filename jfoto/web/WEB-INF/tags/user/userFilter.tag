<%@ tag import="controllers.users.list.UserFilterModel" %>
<%@ tag import="core.general.user.UserMembershipType" %>

<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="actionPrefix" required="true" type="java.lang.String" %>

<jsp:useBean id="userFilterModel" type="controllers.users.list.UserFilterModel" scope="request"/>

<c:if test="${userFilterModel.visible}">
	<tags:inputHintForm/>

	<c:set var="filterUserNameControl" value="<%=UserFilterModel.USER_NAME_FORM_CONTROL%>"/>

	<c:set var="membershipTypeListValues" value="<%=UserMembershipType.values()%>"/>
	<c:set var="membershipTypeControl" value="userFilterModel.membershipTypeList"/>

	<eco:form action="${eco:baseUrlWithPrefix()}/${actionPrefix}/filter/">

		<table:table width="400">

			<tr>
				<td colspan="2">
						<h3>${eco:translate( 'Filter' )}:</h3>
				</td>
			</tr>

			<tr>
				<td align="right" width="320px">
					<label for="${filterUserNameControl}">${eco:translate( 'Name' )}</label>
				</td>
				<td class="datacolumn">
					<tags:inputHint inputId="${filterUserNameControl}" hintTitle_t="${eco:translate('Filter')}"
									hint="${eco:translate('Filter by user name')}">
						<jsp:attribute name="inputField">
							<input id="${filterUserNameControl}" type="text" name="${filterUserNameControl}" value="${userFilterModel.filterUserName}"/>
						</jsp:attribute>
					</tags:inputHint>
				</td>
			</tr>

			<table:tredit>
				<table:tdtext text_t="Membership type" labelFor="membershipTypeId" />

				<table:tddata>
					<form:checkboxes items="${membershipTypeListValues}" path="${membershipTypeControl}" itemLabel="name" itemValue="id" delimiter="<br/>" htmlEscape="false" />
					<%--<html:checkbox inputId="membershipTypeId" inputValue="0" /> ${eco:translate('any')} <br />
					<c:forEach var="mType" items="${membershipTypeListValues}">
						<html:checkbox inputId="membershipTypeId" inputValue="${mType.id}" /> ${mType.name} <br />
					</c:forEach>--%>
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<td class="buttoncolumn">
					<html:submitButton id="applyUserListFilterButton" caption_t="Apply filter"/>
				</td>
				<table:td>
					<html:resetButton caption_t="Reset filter"/>
				</table:td>
			</table:tredit>

		</table:table>

	</eco:form>

	<tags:springErrorHighliting bindingResult="${userFilterModel.bindingResult}" />
</c:if>