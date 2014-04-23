<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>

<%@ attribute name="bindingResult" required="true" type="org.springframework.validation.BindingResult" %>

<c:if test="${not empty(bindingResult)}">

	<script type="text/javascript">

		require( ['jquery'], function ( $ ) {

			$( document ).ready( function () {

				$( ".invalid" ).removeClass( "invalid" );

				<c:if test="${ not empty(bindingResult.fieldErrors) }">
					var fieldErrorMessage = "";
					<c:forEach var="error" items="${bindingResult.fieldErrors}" varStatus="status">
						$( "[name='${error.field}']" ).addClass( "invalid" );
						fieldErrorMessage += "<div class=\"spring-error-highlight\">${error.code}</div>";
					</c:forEach>
					fieldErrorMessage += "";

					showUIMessage_Error( fieldErrorMessage );
				</c:if>

				<c:if test="${ empty(bindingResult.fieldErrors) and not empty bindingResult.allErrors }">
					var generalErrorMessage = "";

					<c:forEach var="error" items="${bindingResult.allErrors}" varStatus="status">
						generalErrorMessage += "<div class=\"spring-error-highlight\">${error.code}: ${error.defaultMessage}</div>";
					</c:forEach>

					showUIMessage_Error( generalErrorMessage );
				</c:if>
			} );
		} );
	</script>

</c:if>