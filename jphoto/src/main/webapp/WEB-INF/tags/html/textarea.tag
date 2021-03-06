<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="inputId" required="true" type="java.lang.String" %>
<%@ attribute name="inputValue" required="false" type="java.lang.String" %>
<%@ attribute name="title" required="false" type="java.lang.String" %>
<%@ attribute name="hint" required="false" type="java.lang.String" %>
<%@ attribute name="rows" required="false" type="java.lang.String" %>
<%@ attribute name="cols" required="false" type="java.lang.String" %>
<%@ attribute name="isDisabled" required="false" type="java.lang.Boolean" %>
<%@ attribute name="maxlength" required="false" type="java.lang.String" %>

<c:if test="${empty rows}">
    <c:set var="rows" value="4"/>
</c:if>

<c:if test="${empty cols}">
    <c:set var="cols" value="40"/>
</c:if>

<textarea
        <c:if test="${not empty maxlength}">maxlength="${maxlength}"</c:if> class="ui-widget-content ui-corner-all" id="${inputId}" name="${inputId}"
        rows="${rows}" cols="${cols}"
        <c:if test="${isDisabled}">disabled="disabled"</c:if> >${inputValue}</textarea>
<span id="textarea_${inputId}" style="width: 100%; float: left; height: auto;"></span>
