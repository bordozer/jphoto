<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ attribute name="current" required="true" type="java.lang.Integer" %>
<%@ attribute name="total" required="true" type="java.lang.Integer" %>
<%@ attribute name="totalSteps" required="false" type="java.lang.Integer" %>
<%@ attribute name="size" required="false" type="java.lang.Integer" %>
<%@ attribute name="color" required="false" type="java.lang.String" %>

<%
	final String[] colors = {
		"FF0044", "FE0A41", "FE143E", "FE1E3B", "FD2839", "FD3336", "FD3D33", "FC4730", "FC512E", "FC5B2B", "FB6628",
		"FB7026", "FB7A23", "FA8420", "FA8E1D", "FA991B", "F9A318", "F9AD15", "F9B713", "F8C110", "F8CC0D", "F8D60A",
		"F7E008", "F7EA05", "F7F402", "F7FF00", "EFFB00", "E8F801", "E0F501", "D9F202", "D1EF02", "CAEC03", "C3E804",
		"BBE504", "B4E205", "ACDF05", "A5DC06", "9ED907", "96D507", "8FD208", "87CF08", "80CC09", "78C909", "71C60A",
		"6AC20B", "62BF0B", "5BBC0C", "53B90C", "4CB60D", "45B30E"
};
%>
<c:set var="colors" value="<%=colors%>"/>

<c:if test="${empty totalSteps}">
	<c:set var="totalSteps" value="50"/>
</c:if>

<c:set var="currentStep" value="${eco:ceil(totalSteps / total * current)}"/>
<c:set var="percent" value="${eco:round(current / total * 100)}"/>

<c:if test="${empty size}">
	<c:set var="size" value="7"/>
</c:if>

<c:set var="boxColor" value="#FF0000"/>
<c:set var="width" value="${totalSteps * (size + 1)}"/>
<c:set var="colorsLen" value="${fn:length(colors)}"/>

<div style="float: left; margin: 1px;">
	<div style="float: left; width: ${width}px; margin: 3px; padding: 1px; border: solid 1px #CDCDCD; margin-top: 4px;">

		<c:forEach begin="1" end="${totalSteps}" var="i">

			<c:if test="${empty color}">
				<c:forEach begin="0" end="${colorsLen - 1}" var="j">
					<c:if test="${j <= eco:floor( i / totalSteps * colorsLen )}">
						<c:set var="boxColor" value="#${colors[j]}"/>
					</c:if>
				</c:forEach>
			</c:if>

			<c:if test="${i > currentStep}">
				<c:set var="boxColor" value="#CDCDCD"/>
			</c:if>

			<div style="float: left; width: ${size}px; height:${size}px; margin-left: 1px; background-color: ${boxColor}">&nbsp;</div>

		</c:forEach>
	</div>

	${percent}%
</div>