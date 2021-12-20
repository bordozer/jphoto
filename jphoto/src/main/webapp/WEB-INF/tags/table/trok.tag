<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="id" type="java.lang.String" required="false" description="Button ID" %>
<%@ attribute name="text_t" type="java.lang.String" required="false" description="Button text" %>
<%@ attribute name="onclick" type="java.lang.String" required="false" description="Button onclick" %>

<c:set var="buttonId" value="submitButton"/>
<c:if test="${not empty id}">
    <c:set var="buttonId" value="${id}"/>
</c:if>

<table:tredit>
    <td class="buttoncolumn">
        <html:submitButton id="${buttonId}" caption_t="${text_t}" onclick="${onclick}"/>
    </td>
    <table:td>&nbsp;</table:td>
</table:tredit>
