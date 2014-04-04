<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.services.security.SecurityService" %>
<%@ tag import="core.context.EnvironmentContext" %>
<%@ tag import="core.services.utils.DateUtilsService" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="entryMenu" required="true" type="core.general.menus.EntryMenu" %>
<%@ attribute name="lastUserActivityTime" required="true" type="java.util.Date" %>

<%
	final SecurityService securityService = ApplicationContextHelper.getSecurityService();
	final DateUtilsService dateUtilsService = ApplicationContextHelper.getDateUtilsService();
%>
<c:set var="isSuperAdminUser" value="<%=securityService.isSuperAdminUser( EnvironmentContext.getCurrentUserId() )%>"/>
<c:set var="isLastUserActivityTime" value="<%=! dateUtilsService.isEmptyTime( lastUserActivityTime )%>"/>

<icons:favoritesJS />
<tags:entryMenuJs />

<table:table border="0" width="100%" oddEven="true">

	<table:separatorInfo colspan="2" height="50" title="${eco:translate('Personal information')}"/>

	<table:trinfo>
		<table:tdtext text_t="id"/>
		<table:td>${user.id}</table:td>
	</table:trinfo>

	<c:if test="${isSuperAdminUser}">
		<table:trinfo>
			<table:tdtext text_t="Login"/>
			<table:td>
				${eco:escapeHtml(user.login)}
			</table:td>
		</table:trinfo>
	</c:if>

	<table:trinfo>
		<table:tdtext text_t="Member name"/>
		<table:td>
			${eco:escapeHtml(user.name)}
			<icons:userIcons user="${user}" hideIconSendPrivateMessage="true" />
			<tags:entryMenu entryMenu="${entryMenu}" />
		</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:tdtext text_t="Last activity time"/>
		<table:td>
			<c:if test="${isLastUserActivityTime}">
				${eco:formatDateTimeShort(lastUserActivityTime)}
			</c:if>
			&nbsp;
		</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:tdtext text_t="Status"/>
		<table:td>${eco:translate(user.userStatus.name)}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:tdtext text_t="Gender"/>
		<table:td>
			<icons:userGender user="${user}" />
		</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:tdtext text_t="Email"/>
		<table:td>
			${eco:escapeHtml(user.email)}
			<br />
			<a href="" title="${eco:translate("Send email")}" onclick="alert( '${eco:translate('This functionality has not been implemented yet...')}' ); return false;">${eco:translate("Send email")}</a>
		</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:tdtext text_t="Birthday"/>
		<table:td>${eco:formatDate(user.dateOfBirth)}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:tdtext text_t="Membership type"/>
		<table:td>${user.membershipType.name}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:tdtext text_t="Registered"/>
		<table:td>${eco:formatDateTimeShort(user.registrationTime)}</table:td>
	</table:trinfo>

</table:table>