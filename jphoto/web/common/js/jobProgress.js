function updateProgress( jobId ) {
	var jobProgressDTO = jsonRPC.jobExecutionService.getJobProgressAjax( jobId );
	var current = jobProgressDTO.current;
	var total = jobProgressDTO.total;
	var jobStatusId = jobProgressDTO.jobStatusId;
	var isJobActive = jobProgressDTO.jobActive;
	var jobExecutionDuration = jobProgressDTO.jobExecutionDuration;

	if ( !isJobActive ) {
		document.location.reload();
		return;
	}
	var percentage = Math.floor( 100 * parseInt( current ) / parseInt( total ) );

	$( '#totalStepsDivId' ).text( total > 0 ? total : "${calculatingText}" );
	$( '#currentJobProgressId' ).text( current );
	$( '#percentageJobProgressId' ).text( percentage + '%, ' + jobExecutionDuration );

	$( "#progressbar_" + jobId ).progressbar( {
										 value:percentage
									 } );

	setTimeout( function () {
		updateProgress( jobId );
	}, interval );
}