<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
	function voteForUserRankInGenre( userId, genreId, votingPoints ) {
		$.ajax( {
					type:'GET',
					url:"${eco:baseUrlWithPrefix()}/voting/rank/voting/?userId=" + userId + "&genreId=" + genreId + "&points=" + votingPoints,
					success:function ( response ) {
						// response == RankInGenreVotingSave.jsp
						$( "#user_by_genre_voting_" + genreId ).html( response );
					},
					error:function () {
						showErrorMessage( '${eco:translate('Error!')}' );
					}
				} );
	}

	function voteForUserRankInGenreByPhoto( photoId, genreId, votingPoints ) {
		$.ajax( {
					type:'GET',
					url:"${eco:baseUrlWithPrefix()}/voting/rank/voting/?photoId=" + photoId + "&points=" + votingPoints,
					success:function ( response ) {
						// response == RankInGenreVotingSave.jsp
						$( "#user_by_genre_voting_" + genreId ).html( response );
					},
					error:function () {
						showErrorMessage( '${eco:translate('Error!')}' );
					}
				} );
	}
</script>