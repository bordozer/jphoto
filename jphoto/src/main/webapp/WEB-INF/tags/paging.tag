<%@ tag import="com.bordozer.jphoto.core.services.dao.BaseEntityDao" %>
<%@ tag import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ tag import="com.bordozer.jphoto.utils.PagingUtils" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="showSummary" required="false" type="java.lang.Boolean" %>

<jsp:useBean id="pagingModel" type="com.bordozer.jphoto.core.general.base.PagingModel" scope="request"/>

<c:set var="currentPage" value="<%=pagingModel.getCurrentPage()%>"/>
<c:set var="pageItems" value="<%=pagingModel.getPageItems( EnvironmentContext.getLanguage() )%>"/>
<c:set var="totalPages" value="<%=pagingModel.getTotalPages()%>"/>

<c:set var="pageParam" value="<%=PagingUtils.PAGING_PARAMETER_NAME%>"/>

<c:set var="delimeter" value="&nbsp;&nbsp;&nbsp;"/>

<div class="row row-bottom-padding-10">

    <div class="col-lg-4">
        <c:if test="${showSummary}">
            ${eco:translate('Paging: Items total')}: ${pagingModel.totalItems} ${delimeter} / ${delimeter} ${eco:translate('Paging: Pages total')}: ${totalPages} ${delimeter}
            <c:if test="${pagingModel.totalItems > 0}">
                / ${delimeter} ${eco:translate('Paging: Items on page')}: ${pagingModel.itemsOnPage} ${delimeter}
            </c:if>
        </c:if>
    </div>

    <c:if test="${pagingModel.totalItems > pagingModel.itemsOnPage}">
        <div class="col-lg-8">
            <ul class="pagination">
                <c:forEach var="pageItem" items="${pageItems}">

                    <c:if test="${currentPage == pageItem.number}">
                        <li class="active"><a href="#" onclick="return false;">${pageItem.number}</a></li>
                    </c:if>

                    <c:if test="${currentPage != pageItem.number}">

                        <c:set var="pageItemTitle" value="${eco:translate1('Paging: Page $1', pageItem.number)}"/>

                        <c:if test="${pageItem.number == 0}">
                            <li class="disabled"><a href="#" onclick="return false;">&nbsp;...&nbsp;</a></li>
                        </c:if>

                        <c:if test="${pageItem.number != 0}">
                            <li>
                                <a href="${pagingModel.requestUrl}?${pageParam}=${pageItem.number}" title="${pageItemTitle}">
                                        ${pageItem.number}
                                </a>
                            </li>
                        </c:if>
                    </c:if>

                </c:forEach>
            </ul>
        </div>
    </c:if>

</div>
