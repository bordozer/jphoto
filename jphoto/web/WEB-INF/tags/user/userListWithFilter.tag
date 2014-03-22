<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="users" required="true" type="java.util.List" %>
<%@ attribute name="listTitle" required="false" type="java.lang.String" %>
<%@ attribute name="showPaging" required="false" type="java.lang.Boolean" %>
<%@ attribute name="userListDataMap" required="true" type="java.util.Map" %>
<%@ attribute name="showEditIcons" required="true" type="java.lang.Boolean" %>

<c:if test="${not empty listTitle}">
	<center><b>${listTitle}</b></center>
	<br/>
</c:if>

<div class="floatleft">
	<c:if test="${showPaging}">
		<tags:paging showSummary="false" />
	</c:if>

	<user:userList users="${users}" userListDataMap="${userListDataMap}" showEditIcons="${showEditIcons}"/>

	<c:if test="${showPaging}">
		<tags:paging showSummary="true" />
	</c:if>
</div>

<div class="floatleft">
	<user:userFilter />
</div>

<div class="footerseparator"></div>