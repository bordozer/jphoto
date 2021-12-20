<%@ tag import="com.bordozer.jphoto.core.general.user.UserMembershipType" %>
<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ tag import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ tag import="com.bordozer.jphoto.ui.controllers.users.list.UserFilterModel" %>
<%@ tag import="com.bordozer.jphoto.ui.translatable.GenericTranslatableList" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="userFilterModel" type="com.bordozer.jphoto.ui.controllers.users.list.UserFilterModel" scope="request"/>

<c:set var="membershipTypeListValues"
       value="<%=GenericTranslatableList.userMembershipTypeTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>

<c:if test="${userFilterModel.visible}">
    <tags:inputHintForm/>

    <c:set var="filterUserNameControl" value="<%=UserFilterModel.USER_NAME_FORM_CONTROL%>"/>

    <c:set var="membershipTypeControl" value="userFilterModel.membershipTypeList"/>

    <eco:form action="${eco:baseUrl()}/members/filter/">

        <table:table width="400">

            <tr>
                <td colspan="2">
                    <h3>${eco:translate( 'User list filter: Members search' )}:</h3>
                </td>
            </tr>

            <tr>
                <td align="right" width="320px">
                    <label for="${filterUserNameControl}">${eco:translate( 'User list filter: Member name' )}</label>
                </td>
                <td class="datacolumn">
                        <%--<tags:inputHint inputId="${filterUserNameControl}" hintTitle_t="User list filter: Members search" hint="${eco:translate('User list filter: Filter by member name')}">--%>
                        <%--<jsp:attribute name="inputField">--%>
                    <input id="${filterUserNameControl}" type="text" name="${filterUserNameControl}" value="${userFilterModel.filterUserName}"/>
                        <%--</jsp:attribute>--%>
                        <%--</tags:inputHint>--%>
                </td>
            </tr>

            <table:tredit>
                <table:tdtext text_t="User list filter: Membership type" labelFor="membershipTypeId"/>

                <table:tddata>
                    <form:checkboxes items="${membershipTypeListValues}" path="${membershipTypeControl}" itemLabel="name" itemValue="id" delimiter="<br/>"/>
                    <%--<html:checkbox inputId="membershipTypeId" inputValue="0" /> ${eco:translate('any')} <br />
                    <c:forEach var="mType" items="${membershipTypeListValues}">
                        <html:checkbox inputId="membershipTypeId" inputValue="${mType.id}" /> ${mType.name} <br />
                    </c:forEach>--%>
                </table:tddata>
            </table:tredit>

            <table:tredit>
                <td class="buttoncolumn">
                    <html:submitButton id="applyUserListFilterButton" caption_t="User list filter: Search members"/>
                </td>
                <table:td>
                    <html:resetButton caption_t="User list filter: Reset filter"/>
                </table:td>
            </table:tredit>

        </table:table>

    </eco:form>

    <tags:springErrorHighliting bindingResult="${userFilterModel.bindingResult}"/>
</c:if>
