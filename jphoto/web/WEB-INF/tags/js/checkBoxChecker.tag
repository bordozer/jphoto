<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="namePrefix" type="java.lang.String" required="true" %>
<%@ attribute name="initiallyChecked" type="java.lang.Boolean" required="false" %>

<img id="mass-selector-icon-${namePrefix}" width="16" height="16" />

<script type="text/javascript">


	require( [ 'jquery', 'mass_checker' ], function ( $, mass_checker ) {

		var massChecker = mass_checker.getMassChecker();

		console.log( 'MassChecker: ',  massChecker );

		<c:if test="${initiallyChecked}">
			massChecker.registerSelected( "${namePrefix}", "${eco:imageFolderURL()}" );
		</c:if>

		<c:if test="${not initiallyChecked}">
			massChecker.registerUnselected( "${namePrefix}", "${eco:imageFolderURL()}" );
		</c:if>

	});

</script>