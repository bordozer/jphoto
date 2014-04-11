<%@ tag import="utils.PagingUtils" %>
<%@ tag import="core.services.dao.BaseEntityDao" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="showSummary" required="false" type="java.lang.Boolean" %>

<jsp:useBean id="pagingModel" type="core.general.base.PagingModel" scope="request"/>

<c:set var="currentPage" value="<%=pagingModel.getCurrentPage()%>"/>
<c:set var="pageItems" value="<%=pagingModel.getPageItems( EnvironmentContext.getLanguage() )%>"/>
<c:set var="totalPages" value="<%=pagingModel.getTotalPages()%>"/>

<c:set var="pageParam" value="<%=PagingUtils.PAGING_PARAMETER_NAME%>" />

<c:set var="delimeter" value="&nbsp;&nbsp;&nbsp;"/>

<c:if test="${showSummary}">
	<div class="floatleft pagingsummary" style="margin-bottom: 20px; margin-left: 20px;">

		${eco:translate('Paging: Items total')}: ${pagingModel.totalItems} ${delimeter} / ${delimeter} ${eco:translate('Paging: Pages total')}: ${totalPages} ${delimeter}

		<c:if test="${pagingModel.totalItems > 0}">
			/ ${delimeter} ${eco:translate('Paging: Items on page')}: ${pagingModel.itemsOnPage} ${delimeter}
		</c:if>

	</div>
</c:if>

<div class="floatleft" style="text-align: center; padding: 10px;">

	<c:forEach var="pageItem" items="${pageItems}">

		<c:if test="${currentPage == pageItem.number}">
			<span class="page selectedPage block-border">${pageItem.number}</span>
		</c:if>

		<c:set var="pageItemTitle" value="${eco:translate1('Paging: Page $1', pageItem.number)}"/>

		<c:if test="${currentPage != pageItem.number}">

			<c:if test="${pageItem.number == 0}">
				<span title="${pageItemTitle}">&nbsp;...&nbsp;</span>
			</c:if>

			<c:if test="${pageItem.number != 0}">
				<a href="${pagingModel.requestUrl}?${pageParam}=${pageItem.number}" title="${pageItemTitle}">
					<span class="page block-background block-border block-shadow">${pageItem.number}</span>
				</a>
			</c:if>
		</c:if>

	</c:forEach>
</div>