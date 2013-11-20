<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="id" required="true" type="java.lang.String" %>
<%@ attribute name="src" required="true" type="java.lang.String" %>
<%@ attribute name="width" required="true" type="java.lang.String" %>
<%@ attribute name="height" required="true" type="java.lang.String" %>
<%@ attribute name="alt" required="false" type="java.lang.String" %>
<%@ attribute name="onclick" required="false" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>

<img id="${id}" src="${eco:imageFolderURL()}/${src}" border="0" width="${width}" height="${height}" alt="${alt}" title="${alt}"
		<c:if test="${not empty onclick}">onclick="${onclick}"</c:if><c:if test="${not empty cssClass}">class="${cssClass}"</c:if>
>


