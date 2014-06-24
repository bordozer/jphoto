<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
	function voteForUserRankInGenre( userId, genreId, votingPoints ) {
		$.ajax( {
					type:'GET',
					url:"${eco:baseUrl()}/voting/rank/voting/?userId=" + userId + "&genreId=" + genreId + "&points=" + votingPoints,
					success:function ( response ) {
						// response == RankInGenreVotingSave.jsp
						$( "#user_by_genre_voting_" + genreId ).html( response );
					},
					error:function () {
						showUIMessage_Error( '${eco:translate('ForUserRankInGenre: Error!')}' );
					}
				} );
	}

	function voteForUserRankInGenreByPhoto( photoId, genreId, votingPoints ) {
		$.ajax( {
					type:'GET',
					url:"${eco:baseUrl()}/voting/rank/voting/?photoId=" + photoId + "&points=" + votingPoints,
					success:function ( response ) {
						// response == RankInGenreVotingSave.jsp
						$( "#user_by_genre_voting_" + genreId ).html( response );
					},
					error:function () {
						showUIMessage_Error( '${eco:translate('ForUserRankInGenre: Error!')}' );
					}
				} );
	}
</script>