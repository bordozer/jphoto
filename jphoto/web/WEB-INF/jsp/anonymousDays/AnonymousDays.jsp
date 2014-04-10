<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="anonym" tagdir="/WEB-INF/tags/anonym" %>

<jsp:useBean id="anonymousDaysModel" type="ui.controllers.anonymousDays.AnonymousDaysModel" scope="request"/>

<tags:pageLight title="${eco:translate('Anonymous Days')}">

	<script type="text/javascript">

		$( function () {
			$( "#datepicker" ).datepicker( {
											  inline:true
											, firstDay:1
											, showOtherMonths:true
											, showWeek: true
											, showButtonPanel: true
											, changeMonth: true
											, changeYear: true
											, numberOfMonths: 1
											, dayNamesMin:['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
											, dateFormat: "mm/dd/yy"
											, onSelect: showInfoAboutAnonymousDay
											, beforeShowDay: highlightAnonymousDays
//											, selectOtherMonths: true
										   } );
		} );

		function showInfoAboutAnonymousDay( pickerDate ) {
			if ( isAnonymousDay( pickerDate ) ) {
				alert( convertDateToPrint( pickerDate ) + ' ${eco:translate('is anonymous day')}' );
			} else {
				alert( convertDateToPrint( pickerDate ) + ' ${eco:translate('is not anonymous day')}' );
			}
		}

	</script>

	<div style="margin-left: 40px;">
		<anonym:anunymousDays anonymousDays="${anonymousDaysModel.anonymousDays}" anonymousPeriod="${anonymousDaysModel.anonymousPeriod}" />
	</div>

</tags:pageLight>
