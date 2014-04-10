<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="text_t" type="java.lang.String" required="false" description="Column text" %>
<%@ attribute name="labelFor" type="java.lang.String" required="false" description="Column text" %>
<%@ attribute name="isMandatory" type="java.lang.Boolean" required="false" description="Column text" %>

<td class="titlecolumn">

	<c:if test="${not empty labelFor}">
		<label for="${labelFor}">
	</c:if>

		${eco:translate( text_t )} <c:if test="${not empty isMandatory}"> <span style="color: #AA0000">*</span></c:if>

	<c:if test="${not empty labelFor}">
		</label>
	</c:if>
	<jsp:doBody />
</td>