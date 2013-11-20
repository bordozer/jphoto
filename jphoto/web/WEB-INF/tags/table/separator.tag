<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="colspan" required="true" type="java.lang.String" %>
<%@ attribute name="height" required="false" type="java.lang.String" %>
<%@ attribute name="title_t" required="false" type="java.lang.String" %>

<tr <c:if test="${not empty height}">height="${height}"</c:if>>
	<td colspan=${colspan}>
		<div class='separator block-border base-font-color'><c:if test="${not empty title_t}">${eco:translate(title_t)}</c:if></div>
	</td>
</tr>