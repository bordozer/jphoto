<%@ tag import="java.util.List" %>
<%@ tag import="core.general.photo.PhotoVotingCategory" %>
<%@ tag import="core.services.entry.VotingCategoryService" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.context.EnvironmentContext" %>
<%@ tag import="core.general.user.User" %>
<%@ tag import="utils.UserUtils" %>
<%@ tag import="core.services.utils.UrlUtilsServiceImpl" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<%@ attribute name="photo" required="true" type="core.general.photo.Photo" %>
<%@ attribute name="userPhotoVotes" required="true" type="java.util.List" %>
<%@ attribute name="minMarkForGenre" required="false" type="java.lang.Integer" %>
<%@ attribute name="maxMarkForGenre" required="false" type="java.lang.Integer" %>

<%
	final VotingCategoryService categoryService = ApplicationContextHelper.getBean( VotingCategoryService.BEAN_NAME );
	final List<PhotoVotingCategory> photoVotingCategories = categoryService.getGenreVotingCategories( photo.getGenreId() ).getVotingCategories();
	final User loggedUser = EnvironmentContext.getCurrentUser();
	final boolean isUserVotedForPhoto = userPhotoVotes.size() > 0;
%>

<c:set var="votingCategories" value="<%=photoVotingCategories%>" />
<c:set var="loggedUser" value="<%=loggedUser%>"/>
<c:set var="hasUserAlreadyVotedForPhoto" value="<%=isUserVotedForPhoto%>"/>

<c:set var="frmPhotoVoting" value="frmPhotoVoting"/>
<c:set var="votingMarkQty" value="<%=VotingCategoryService.PHOTO_VOTING_CATEGORY_QTY%>"/>

<c:set var="votingTitle" value="${eco:translate('Voiting')}" />
<c:if test="${hasUserAlreadyVotedForPhoto}">
	<c:set var="votingTitle" value="${eco:translate1('You voted at $1', eco:formatDateTimeShort(userPhotoVotes[0].votingTime))}" />
</c:if>

<c:set var="voteUrl" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoVotingLink( photo.getId() )%>"/>

<div id="photoVotingDiv">
	<eco:form formName="${frmPhotoVoting}" action="">

		<table:table border="0" width="99%>">

			<table:separatorInfo colspan="2" title="${votingTitle}" />

			<c:if test="${not hasUserAlreadyVotedForPhoto}">
				<c:forEach begin="1" end="${votingMarkQty}" var="counter">
					<photo:photoVotingCategory votingCategories="${votingCategories}"
											   number="${counter}"
											   minMarkForGenre="${minMarkForGenre}"
											   maxMarkForGenre="${maxMarkForGenre}"
							/>
				</c:forEach>
			</c:if>

			<c:if test="${hasUserAlreadyVotedForPhoto}">
				<c:forEach var="userPhotoVote" items="${userPhotoVotes}">
					<table:tr>
						<table:td>${userPhotoVote.photoVotingCategory.name}</table:td>
						<table:td>
							<span title="${eco:translate2('Your set mark for $1: $2', userPhotoVote.photoVotingCategory.name, userPhotoVote.mark)}">${userPhotoVote.mark}</span>
							/
							<span title="${eco:translate1('Max accessible at voting time mark: $1', userPhotoVote.maxAccessibleMark)}">${userPhotoVote.maxAccessibleMark}</span>
						</table:td>
					</table:tr>
				</c:forEach>
			</c:if>

			<c:if test="${not hasUserAlreadyVotedForPhoto}">
				<table:trok text_t="Vote" onclick="return submitVotingForm()" />
			</c:if>

		</table:table>

	</eco:form>
</div>


<c:if test="${not hasUserAlreadyVotedForPhoto}">
	<script type="text/javascript">

		var votingCategories = [ ${votingCategories[0].id}, ${votingCategories[1].id}, ${votingCategories[2].id} ];
		var votingCategoryPreviousValues = [];

		jQuery().ready(function() {
			for( var i = 1; i <= ${votingMarkQty}; i++) {
				initVotingCategoryValue( i );
				setAccessToVotingCategoryMark( i );
			}
		});

		function initVotingCategoryValue( i ) {
			var categoryValue = votingCategories[ i - 1 ];
			$( '#votingCategory' + i ).val( categoryValue );
			votingCategoryPreviousValues[ i ] = categoryValue;
		}

		function setAccessToVotingCategoryMark( number ) {
			var id = 'votingCategory' + number;
			var votingCategory = $( '#' + id );
			var votingCategoryMark = $( '#votingCategoryMark' + number );
			var votingCategoryMarkContainer = $( '#votingCategoryMarkContainer' + number );

			if ( votingCategory.val() == 0 ) {
				hideVotingCategoryMark( votingCategoryMark, votingCategoryMarkContainer );
				votingCategoryPreviousValues[ number ] = votingCategory.val();
				return;
			}

			var hasErrors = false;

			$( ".votingCategoryClass" ).each( function () {
				var element = $( this );
				if ( element.attr( 'id' ) != id && votingCategory.val() != 0 && element.val() == votingCategory.val() ) {
					var optionText = $( "option[value='" + votingCategory.val() + "']", votingCategory ).text();
					showWarningMessage( "'" + optionText + "' ${eco:translate('is duplicated value')}" );
					hasErrors = true;
				}
			} );

			if ( !hasErrors ) {
				showVotingCategoryMark( votingCategoryMark, votingCategoryMarkContainer, number, votingCategory );
			} else {
				hideVotingCategoryMark( votingCategoryMark, votingCategoryMarkContainer );
				resetVotingCategoryToPreviousValue( votingCategory, number );
			}
		}

		function resetVotingCategoryToPreviousValue( votingCategory, number ) {
			votingCategory.val( votingCategoryPreviousValues[ number ] );
			setAccessToVotingCategoryMark( number );
		}

		function showVotingCategoryMark( votingCategoryMark, votingCategoryMarkContainer, number, votingCategory ) {
			votingCategoryMarkContainer.html( '' );
			votingCategoryMark.show();
//				votingCategoryMark.focus(); // Do not set the focus or Opera jumps to the control otherwise
			votingCategoryPreviousValues[ number ] = votingCategory.val();
		}

		function hideVotingCategoryMark( votingCategoryMark, votingCategoryMarkContainer ) {
			votingCategoryMark.hide();
			votingCategoryMarkContainer.html( '<b>X</b>' );
		}

		function submitVotingForm() {
			var hasErrors = validateVotingCategories();
			if ( hasErrors ) {
				showWarningMessage( '${eco:translate('Please, select at least one category')}' );
				return false;
			}

			voteAndLoadResults();

			return false;
		}

		function voteAndLoadResults() {
			$.ajax( {
						type:'POST',
						url:'${voteUrl}',
						data:$( '#${frmPhotoVoting}' ).serialize(),
						success:function ( response ) {
							$( '#photoVotingDiv' ).html( response ); // response == voting/PhotoVoting.jsp

						},
						error:function () {
							showErrorMessage( '${eco:translate('Error due voting')}' );
						}
					} ).done(function() {
						refreshPhotoInfo();
						noty( {
								text:'${eco:translate('Your marks have been saved')}', type:'success', layout:'bottomRight', timeout:3000
							} );
					});
		}

		function validateVotingCategories() {
			var hasErrors = true;

			for ( var i = 1; i <= ${votingMarkQty}; i++ ) {
				hasErrors = !doesVotingCategoryHaveValue( i );
				if ( !hasErrors ) {
					break;
				}
			}
			return hasErrors;
		}

		function doesVotingCategoryHaveValue( number ) {
			var id = 'votingCategory' + number;
			var votingCategory = $( '#' + id );
			var votingCategoryMark = $( '#votingCategoryMark' + number );

			return votingCategory.val() != 0; // || ( votingCategory.val() != 0 && votingCategoryMark.val == 0 );
		}

	</script>
</c:if>