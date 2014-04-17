function updateProgress( jobId, callback ) {
	var jobProgressDTO = jsonRPC.jobExecutionService.getJobProgressAjax( jobId );

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
		updateProgress( jobId, callback );
	}, interval );

//	return percentage;
}