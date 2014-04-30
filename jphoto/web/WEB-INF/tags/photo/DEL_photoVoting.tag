<%@ tag import="java.util.List" %>
<%@ tag import="core.general.photo.PhotoVotingCategory" %>
<%@ tag import="core.services.entry.VotingCategoryService" %>
<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ tag import="core.general.user.User" %>
<%@ tag import="core.services.utils.UrlUtilsServiceImpl" %>
<%@ tag import="ui.controllers.voting.PhotoVotingModel" %>
<%@ tag import="core.general.configuration.ConfigurationKey" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

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

<c:set var="votingCategoryMarkControl" value="<%=PhotoVotingModel.VOTING_CATEGORY_MARK_CONTROL%>" />
<c:set var="votingMarkQty" value="<%=ApplicationContextHelper.getConfigurationService().getInt( ConfigurationKey.PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT )%>"/>

<c:set var="votingTitle" value="${eco:translate('Photo appraisal')}" />
<c:if test="${hasUserAlreadyVotedForPhoto}">
	<c:set var="votingTitle" value="${eco:translate1('You has already voted at $1', eco:formatDateTimeShort(userPhotoVotes[0].votingTime))}" />
</c:if>

<c:set var="voteUrl" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoVotingLink( photo.getId() )%>"/>

<div id="photoVotingDiv">
	<eco:form formName="frmPhotoVoting" action="">

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
					<c:set var="photoVotingCategoryName" value="${eco:translateVotingCategory(userPhotoVote.photoVotingCategory.id)}"/>
					<table:tr>
						<table:td width="220" cssClass="textright">${photoVotingCategoryName}</table:td>
						<table:td>
							<span title="${eco:translate2('Your set mark for $1: $2', photoVotingCategoryName, userPhotoVote.mark)}">
								${userPhotoVote.mark > 0 ? '+' : ''}${userPhotoVote.mark}
							</span>
							/
							<span title="${eco:translate1('Max accessible at voting time mark: $1', userPhotoVote.maxAccessibleMark)}">+${userPhotoVote.maxAccessibleMark}</span>
						</table:td>
					</table:tr>
				</c:forEach>
			</c:if>

			<c:if test="${not hasUserAlreadyVotedForPhoto}">
				<table:tr>
					<table:td cssClass="buttoncolumn"><html:submitButton id="voteButton" caption_t="${eco:translate('Vote')}" onclick="return submitVotingForm()" /></table:td>
					<table:td>
						<a href="#" onclick="return voteWithMaxMarks();" title="${eco:translate('Appraisal the photo with the maximum marks')}">
							+${maxMarkForGenre} / +${maxMarkForGenre} / +${maxMarkForGenre}
						</a>

						<script type="text/javascript">
							function voteWithMaxMarks() {
								// TODO: should it be confirmation?
								for ( var i = 1; i <= 3; i++ ) {
									$( "#${votingCategoryMarkControl}" + i ).val( $( "#${votingCategoryMarkControl}1 option:first" ).val() );
								}
								return submitVotingForm();
							}
						</script>

					</table:td>
				</table:tr>
			</c:if>

		</table:table>

	</eco:form>
</div>


<c:if test="${not hasUserAlreadyVotedForPhoto}">
	<script type="text/javascript">

		require( [ 'jquery', '/js/pages/photo-appraisal.js.jsp' ], function( $, photo_appraisal ) {

			$( document ).ready( function () {
				for ( var i = 1; i <= ${votingMarkQty}; i++ ) {
					photo_appraisal.initVotingCategoryValue( [ ${votingCategories[0].id}, ${votingCategories[1].id}, ${votingCategories[2].id} ], i );
					photo_appraisal.setAccessToVotingCategoryMark( i );
				}
			} );
		});

		function submitVotingForm() {
			require( [ 'jquery', '/js/pages/photo-appraisal.js.jsp' ], function ( $, photo_appraisal ) {
				photo_appraisal.submitVotingForm( '${voteUrl}' );
			} );

			return false;
		}

		function setAccessToVotingCategoryMark( number ) {
			require( [ 'jquery', '/js/pages/photo-appraisal.js.jsp' ], function ( $, photo_appraisal ) {
				photo_appraisal.setAccessToVotingCategoryMark( number );
			} );
		}

	</script>
</c:if>