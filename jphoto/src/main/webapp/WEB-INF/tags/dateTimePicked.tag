<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="dateFieldName" required="true" type="java.lang.String" %>
<%@ attribute name="timeFieldName" required="true" type="java.lang.String" %>
<%@ attribute name="date" required="true" type="java.lang.String" %>
<%@ attribute name="time" required="true" type="java.lang.String" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:datePicker fieldName="${dateFieldName}" fieldValue="${date}"/> <html:input fieldId="${timeFieldName}" size="4" fieldValue="${time}"/>

