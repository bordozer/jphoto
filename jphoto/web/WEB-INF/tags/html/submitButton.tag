<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="id" type="java.lang.String" required="true" description="Button ID" %>
<%@ attribute name="caption_t" required="true" type="java.lang.String" %>
<%@ attribute name="onclick" type="java.lang.String" required="false" description="Button onclick" %>
<%@ attribute name="doNotTranslate" type="java.lang.Boolean" required="false" %>
<%@ attribute name="icon" type="java.lang.String" required="false" %>

<c:set var="buttonTitle" value="${eco:translate(caption_t)}" />
<c:if test="${doNotTranslate}">
	<c:set var="buttonTitle" value="${caption_t}" />
</c:if>

<c:set var="imgClass" value="imgClass_${id}"/>

<c:if test="${not empty icon}">
	<style type="text/css">
		.imgClass_${id} {
			background-image: url( ${eco:imageFolderURL()}/${icon} );
			background-repeat:no-repeat;
			background-position: left top;
		}
	</style>
</c:if>

<button type="submit" id="${id}" name="${id}" class="ui-state-default ui-corner-all ${imgClass}"
	<c:if test="${not empty onclick}">onclick="${onclick}"</c:if> title="${buttonTitle}" >
	<c:if test="${not empty icon}">&nbsp;&nbsp;&nbsp;</c:if>${buttonTitle}
</button>