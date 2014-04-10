<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="colspan" required="true" type="java.lang.String" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="height" required="false" type="java.lang.String" %>

<tr <c:if test="${not empty height}">height="${height}"</c:if>>
	<td colspan=${colspan}>
		<div class='block-background block-border block-shadow' style="padding: 7px; border-radius: 7px 7px 0 0">
			<div class='separatorInfo base-font-color'><c:if test="${not empty title}">${title}</c:if></div>
		</div>
	</td>
</tr>