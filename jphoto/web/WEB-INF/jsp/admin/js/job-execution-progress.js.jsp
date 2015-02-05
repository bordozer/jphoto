<%@ page contentType="text/javascript" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

define( [ 'jquery' ], function ( $ ) {

	function updateProgress( jobId, updateInterval, callback ) {

		var jobProgressDTO = Backbone.JPhoto.ajaxService().getJobProgressAjax( jobId ); // TODO: handle an exception

		var percentage = parseInt( jobProgressDTO.jobExecutionPercentage );

		if ( !jobProgressDTO.jobActive ) {
			document.location.reload();
			return;
		}

		$( '#progressStatusFullDescription_' + jobId ).text( jobProgressDTO.progressStatusFullDescription );

		$( "#progressbar_" + jobId ).progressbar( {
			 value:percentage
		 } );

		callback( percentage );

		setTimeout( function () {
			updateProgress( jobId, updateInterval, callback );
		}, updateInterval );
	}

	return {

		updateProgress: function ( jobId, updateInterval, callback ) {
			updateProgress( jobId, updateInterval, callback );
		}
	}
});