<%@ page import="controllers.users.team.edit.UserTeamMemberEditDataModel" %>
<%@ page import="core.enums.UserTeamMemberType" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="userTeamMemberEditDataModel" type="controllers.users.team.edit.UserTeamMemberEditDataModel" scope="request" />

<c:set var="user" value="${userTeamMemberEditDataModel.user}"/>

<c:set var="teamMemberNameControl" value="<%=UserTeamMemberEditDataModel.FORM_CONTROL_TEAM_MEMBER_NAME%>"/>
<c:set var="teamMemberUserIdControl" value="<%=UserTeamMemberEditDataModel.FORM_CONTROL_TEAM_MEMBER_USER_ID%>"/>
<c:set var="teamMemberTypeIdControl" value="<%=UserTeamMemberEditDataModel.FORM_CONTROL_TEAM_MEMBER_TYPE_ID%>"/>

<c:set var="userTeamMemberTypeValues" value="<%=UserTeamMemberType.values()%>"/>

<tags:page pageModel="${userTeamMemberEditDataModel.pageModel}">

	<form:form modelAttribute="userTeamMemberEditDataModel" method="POST" action="${eco:baseUrlWithPrefix()}/members/${user.id}/team/save/">

		<table:table width="500">

			<table:separatorInfo colspan="2" title="${eco:translate('Member parameters')}" />

			<table:tr>
				<table:tdtext text_t="Team member custom name" isMandatory="true" />
				<table:tddata><form:input path="${teamMemberNameControl}" /></table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="Model name" />
				<table:tddata><form:radiobuttons path="${teamMemberTypeIdControl}" items="${userTeamMemberTypeValues}" itemValue="id" itemLabel="nameTranslated" delimiter="<br />" htmlEscape="false"/></table:tddata>
			</table:tr>

			<table:separatorInfo colspan="2" title="${eco:translate('Member')}" />

			<table:tr>
				<table:td colspan="2">
					<user:userPicker userIdControl="${teamMemberUserIdControl}" user="${userTeamMemberEditDataModel.teamMemberUser}" callbackJSFunction="setMemberName" />
				</table:td>
			</table:tr>

			<table:trok text_t="Save" />

		</table:table>

	</form:form>

	<script type="text/javascript">

		<%--
		Parameters:
		label:item.userNameEscaped + ", " + item.userGender,
		value:item.userNameEscaped,
		userId:item.userId,
		userCardLink:item.userCardLink,
		userAvatarUrl:item.userAvatarUrl
		userNameEscaped:item.userNameEscaped
		userName:item.userName,
		--%>

		function setMemberName( item ) {
			var teamMemberNameControl = $( '#${teamMemberNameControl}' );
			if ( teamMemberNameControl.val() == '' ) {
				teamMemberNameControl.val( item.userName );
			}
		}
	</script>

	<tags:springErrorHighliting bindingResult="${userTeamMemberEditDataModel.bindingResult}" />

</tags:page>