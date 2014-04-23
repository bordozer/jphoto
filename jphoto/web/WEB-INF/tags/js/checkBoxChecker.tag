<%@ tag import="java.util.Date" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="namePrefix" type="java.lang.String" required="true" %>
<%--<%@ attribute name="checkboxClass" type="java.lang.String" required="false" %>--%>
<%--<%@ attribute name="container" type="java.lang.String" required="true" %>--%>
<%@ attribute name="initiallyChecked" type="java.lang.Boolean" required="false" %>

<%--<c:set var="iconId" value="checkAllIcon${checkboxClass}"/>--%>

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

	/*var func${checkboxClass} = define( [ 'jquery' ], function( $ ) {

		var isChecked = ${!initiallyChecked};

		return {
			switchIcon: function ( icon ) {
				if ( isChecked ) {
					icon.attr( 'src', '${eco:imageFolderURL()}/icons16/uncheckAll.png' );
					checkNone${checkboxClass}();
				} else {
					icon.attr( 'src', '${eco:imageFolderURL()}/icons16/checkAll.png' );
					checkAll${checkboxClass}();
				}
				isChecked = !isChecked;
			},

			checkAll${checkboxClass}: function() {
				$( "[name*='${namePrefix}']" ).each( function () {
					$( this ).attr( "checked", "checked" );
				} );
			},

			checkNone${checkboxClass}: function () {
				$( "[name*='${namePrefix}']" ).each( function () {
					$( this ).removeAttr( 'checked' );
				} );
			}
		};
	});

	require( [ 'jquery' ], function( $ ) {
		$( document ).ready( function() {
			var icon = $( '#${iconId}' );
			icon.bind( 'click', checkAll${checkboxClass} );
	//		switchIcon( icon );

			icon.click( function() {
				func${checkboxClass}.switchIcon( icon );
			});
		});
	});

	function checkAll${checkboxClass}() {
		func${checkboxClass}.checkAll${checkboxClass}();
	}*/

</script>