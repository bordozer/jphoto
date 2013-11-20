<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="inputId" required="true" type="java.lang.String" %>
<%@ attribute name="hintTitle_t" required="false" type="java.lang.String" %>
<%@ attribute name="hint" required="false" type="java.lang.String" %>
<%@ attribute name="focused" required="false" type="java.lang.Boolean" %>
<%@ attribute name="inputField" fragment="true" %>

<jsp:invoke fragment="inputField"/>

<script type="text/javascript">

	<c:if test="${not empty hint}">
		/* TODO: this comment implements HINT!!! --> */
		/*var field = $( "#${inputId}" );

		field.focus( function() {
			$( "#titleMessage" ).html( "${eco:translate( hintTitle_t )}" );
			$( "#tooltipMessage" ).html( "${hint}" );
			$( "#effect" ).show( "blind", {}, 500, callback );
		} );

		field.focusout( function() {
			$( "#effect:visible" ).removeAttr( "style" ).fadeOut();
		} );*/
		/* TODO: this comment implements HINT!!! <-- */

		function callback() {

		}

		<c:if test="${focused}">
			jQuery().ready( function(){
				var focused = $( '#${inputId}' );
				focused.focus();
				focused.select();
			} );
		</c:if>

	</c:if>

</script>

<%--<br />
${hint}--%>

<%--<tags:inputHintInit inputId="${inputId}" hintTitle="${hintTitle}" hint="${hint}" />--%>

<%--<script type="text/javascript">

	$( "#effect" ).hide();

	$( "#${inputId}" ).focus( function() {
		$( "#titleMessage" ).html( "${eco:translate( title )}" );
		$( "#tooltipMessage" ).html( "${hint}" );
	} );

</script>--%>

<%--<div class="your_selector">
	<div id="download" class="trigger">
		<jsp:invoke fragment="inputField"/>
	</div>
	<div class="tooltip_description"
		 style="display:none;"
		 title="Field requirement">
		${controlHint}
	</div>
</div>

<script type="text/javascript">
	$("div.your_selector").tooltip();
</script>--%>


<%--<div class="bubbleInfo">

	<div id="download" class="trigger">
		<jsp:invoke fragment="inputField"/>
	</div>

	<table style="opacity: 0; top: -50px; left: -33px; display: none;" id="dpop" class="popup">
		<tbody>
		<tr>
			<td id="topleft" class="corner"></td>
			<td class="top"></td>
			<td id="topright" class="corner"></td>
		</tr>

		<tr>
			<td class="left"></td>
			<td style="background-color: #FFFFFF;">
				<div class="inputFieldHint">${eco:translate( controlHint )}</div>
			</td>
			<td class="right"></td>
		</tr>

		<tr>
			<td class="corner" id="bottomleft"></td>
			<td class="bottom">
				<img alt="popup tail" src="${eco:baseUrl()}/common/js/popup/files/bubble-t.png" height="29" width="30"/>
			</td>
			<td id="bottomright" class="corner"></td>
		</tr>
		</tbody>
	</table>
</div>--%>
