<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="fieldId" required="true" type="java.lang.String" %>
<%@ attribute name="fieldValue" required="false" type="java.lang.String" %>

<input class="ui-state-default ui-corner-all" id="${fieldId}" type="password" name="${fieldId}" value="${fieldValue}"/>
