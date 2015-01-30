<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="core.services.security.SecurityService" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ tag import="core.services.utils.DateUtilsService" %>
<%@ tag import="ui.services.menu.entry.items.EntryMenuType" %>
<%@ tag import="ui.controllers.users.edit.UserEditDataValidator" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="entryMenu" required="true" type="ui.services.menu.entry.items.EntryMenu" %>
<%@ attribute name="lastUserActivityTime" required="true" type="java.util.Date" %>
<%@ attribute name="isEditable" required="true" type="java.lang.Boolean" %>

<%
	final SecurityService securityService = ApplicationContextHelper.getSecurityService();
	final DateUtilsService dateUtilsService = ApplicationContextHelper.getDateUtilsService();
%>
<c:set var="isSuperAdminUser" value="<%=securityService.isSuperAdminUser( EnvironmentContext.getCurrentUserId() )%>"/>
<c:set var="isLastUserActivityTime" value="<%=! dateUtilsService.isEmptyTime( lastUserActivityTime )%>"/>

<tags:contextMenuJs />

	<table:table>

		<table:separatorInfo colspan="2" height="50" title="${eco:translate('User edit data tab: Personal information')}"/>

		<table:trinfo>
			<table:tdtext text_t="id"/>
			<table:td>${user.id}</table:td>
		</table:trinfo>

		<c:if test="${isSuperAdminUser}">
			<table:trinfo>
				<table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_LOGIN%>"/>
				<table:td>
					${eco:escapeHtml(user.login)}
				</table:td>
			</table:trinfo>
		</c:if>

		<table:trinfo>
			<table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_NAME%>"/>
			<table:td>
				${eco:escapeHtml(user.name)}
				<icons:userIcons user="${user}" hideIconSendPrivateMessage="true" />
				<%--<tags:entryMenu entryMenu="${entryMenu}" />--%>
				<tags:contextMenu entryId="${user.id}" entryMenuType="<%=EntryMenuType.USER%>" />
			</table:td>
		</table:trinfo>

		<table:trinfo>
			<table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_LAST_ACTIVITY_TIME%>"/>
			<table:td>
				<c:if test="${isLastUserActivityTime}">
					${eco:formatDateTimeShort(lastUserActivityTime)}
				</c:if>
				&nbsp;
			</table:td>
		</table:trinfo>

		<table:trinfo>
			<table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_STATUS%>"/>
			<table:td>${eco:translate(user.userStatus.name)}</table:td>
		</table:trinfo>

		<table:trinfo>
			<table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_GENDER%>"/>
			<table:td>
				<icons:userGender user="${user}" />
			</table:td>
		</table:trinfo>

		<table:trinfo>
			<table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_EMAIL%>"/>
			<table:td>
				${eco:escapeHtml(user.email)}
			</table:td>
		</table:trinfo>

		<table:trinfo>
			<table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_BIRTHDAY%>"/>
			<table:td>${eco:formatDate(user.dateOfBirth)}</table:td>
		</table:trinfo>

		<table:trinfo>
			<table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_MEMBERSHIP_TYPE%>"/>
			<table:td>${eco:translate(user.membershipType.name)}</table:td>
		</table:trinfo>

		<table:trinfo>
			<table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_REGISTRATION_TIME%>"/>
			<table:td>${eco:formatDateTimeShort(user.registrationTime)}</table:td>
		</table:trinfo>

	</table:table>

	<%-- TODO: move this link to the user's context menu --%>
	<c:if test="${isEditable}">
		<links:userEdit user="${user}">
			${eco:translate('User card: Edit member personal data')}
		</links:userEdit>
	</c:if>