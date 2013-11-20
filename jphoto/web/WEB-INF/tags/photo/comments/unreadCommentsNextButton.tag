<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="userId" required="true" type="java.lang.Integer" %>
<%@ attribute name="pagingModel" required="true" type="core.general.base.PagingModel" %>
<%@ attribute name="showInfo" required="false" type="java.lang.Boolean" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getUnreadCommentsToUserList( userId )%>" />
<c:set var="totalItems" value="${pagingModel.totalItems}" />
<c:set var="itemsOnPage" value="${pagingModel.itemsOnPage > totalItems ? totalItems : pagingModel.itemsOnPage}" />
<c:set var="unreadComments" value="${totalItems}" />

<c:if test="${pagingModel.itemsOnPage < totalItems}">
	<c:set var="itemsOnNextPage" value="${itemsOnPage}" />
	<c:if test="${itemsOnPage * 2 > totalItems}">
		<c:set var="itemsOnNextPage" value="${totalItems - itemsOnPage}" />
	</c:if>

	<c:set var="refreshPageText" value="${eco:translate2('Next $1 of $2 >>', itemsOnNextPage, unreadComments)}" />

	<c:if test="${showInfo}">
		<div style="float: left; width: 100%; height: 20px;">
			${eco:translate2('Shown $1 of $2 unread comments', itemsOnPage, unreadComments)}
		</div>
	</c:if>

	<div style="float: left; width: 100%; height: 30px; text-align: center;">
		<html:submitButton id="refresh" caption_t="${refreshPageText}" onclick="document.location.href = '${link}';" doNotTranslate="true" />
	</div>
</c:if>