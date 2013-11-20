<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="progressBarId" required="true" type="java.lang.String" %>
<%@ attribute name="percentage" required="true" type="java.lang.Integer" %>
<%@ attribute name="width" required="true" type="java.lang.Integer" %>
<%@ attribute name="height" required="true" type="java.lang.Integer" %>
<%@ attribute name="color" required="false" type="java.lang.String" %>

<c:set var="textWidth" value="45"/>

<div style="float: left; width: ${width}px;">
	<div id="${progressBarId}" style="float: left; width: ${width - textWidth - 5}px; height: ${height}px; margin-top: 4px;"></div>
	<div style="float: right; width: ${textWidth}px;"> - ${percentage}%</div>
</div>

<c:if test="${not empty color}">
	<style type="text/css">
		.ui-progressbar.customColor_${progressBarId} .ui-progressbar-value {
			background: ${color};
		}
	</style>
</c:if>

<script type="text/javascript">

	$( function () {
		$( "#${progressBarId}" ).progressbar( {
												  value:${percentage}
											  } ).addClass( 'customColor_${progressBarId}' );
	} );

</script>