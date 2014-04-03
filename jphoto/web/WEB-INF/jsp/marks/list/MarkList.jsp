<%@ page import="core.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<jsp:useBean id="photoMarkListModel" type="ui.controllers.marks.list.PhotoMarkListModel" scope="request"/>

<c:set var="photo" value="${photoMarkListModel.photo}" />
<c:set var="votingCategories" value="${photoMarkListModel.votingCategories}" />
<c:set var="userVotesMap" value="${photoMarkListModel.userVotesMap}" />
<c:set var="marksByCategoriesMap" value="${photoMarkListModel.marksByCategoriesMap}" />

<c:set var="currentUser" value="<%=EnvironmentContext.getCurrentUser()%>" />

<tags:page pageModel="${photoMarkListModel.pageModel}">

	<style type="text/css">
		.empty {
		}
		.ownVote {
			font-weight: bold;
		}
	</style>

	<div class="photoPreviewLight">
		<photo:photoPreviewLight photoPreviewWrapper="${photoMarkListModel.photoPreviewWrapper}" />
	</div>

	<table:table width="1400" border="0">

		<jsp:attribute name="thead">

			<table:td>${eco:translate( "Voter" )}</table:td>

			<c:forEach var="votingCategory" items="${votingCategories}">
				<table:td width="160px">${votingCategory.name}</table:td>
			</c:forEach>

			<table:td width="160px">${eco:translate( "Vote time" )}</table:td>

		</jsp:attribute>

		<jsp:body>

			<c:forEach var="entry" items="${userVotesMap}">

				<c:set var="user" value="${entry.key}" />
				<c:set var="userVoteForCategoryMap" value="${entry.value}" />

				<c:set var="css" value="empty" />
				<c:if test="${currentUser.id == user.id}">
					<c:set var="css" value="ownVote" />
				</c:if>

				<table:tr>

					<table:tdunderlined cssClass="${css}">
						<user:userCard user="${user}" />
					</table:tdunderlined>

					<c:set var="votingTime" value="" />

					<c:forEach var="votingCategory" items="${votingCategories}">

						<c:set var="userPhotoVote" value="${userVoteForCategoryMap[ votingCategory ]}" />
						<c:set var="mark" value="${userPhotoVote.mark}" />
						<c:set var="maxAccessibleMark" value="${userPhotoVote.maxAccessibleMark}" />

						<table:tdunderlined width="120" cssClass="textcentered">
							<c:set var="isMaxAccessibleMark" value="${mark > 0 and maxAccessibleMark > 0 and mark == maxAccessibleMark}" />

							<c:if test="${mark > 0}">
								<span style="color: darkgreen" title="${eco:translate1('Set mark', maxAccessibleMark)}">
									+${mark}
								</span>
								<c:if test="${maxAccessibleMark > 0 and not isMaxAccessibleMark}">
									<span style="color:gray" title="${eco:translate('Max accessible for voter mark')}"> / ${maxAccessibleMark}</span>
								</c:if>
							</c:if>

							<c:if test="${isMaxAccessibleMark}">
								<%--<span style="color: red" title="${eco:translate1('$1 set max accessible for him at voting time mark', eco:escapeHtml(user.name))}">MAX!</span>--%>
								<html:img16 src="icons16/top-points.png" alt="${eco:translate1('$1 set max accessible for him at voting time mark', eco:escapeHtml(user.name))}" />
							</c:if>

							<c:if test="${mark < 0}">
								<span style="color: #AA0000">${mark}</span>
							</c:if>
						</table:tdunderlined>

						<c:if test="${userPhotoVote.votingTime.time > 0}">
							<c:set var="votingTime" value="${userPhotoVote.votingTime}" />
						</c:if>
					</c:forEach>

					<table:tdunderlined cssClass="textcentered">
						${eco:formatDate(votingTime)} &nbsp; ${eco:formatTimeShort(votingTime)}
					</table:tdunderlined>

				</table:tr>

			</c:forEach>

			<table:tr>
				<table:td />

				<c:set var="total" value="0" />
				<c:forEach var="votingCategory" items="${votingCategories}">
					<c:set var="total" value="${total + marksByCategoriesMap[votingCategory]}" />

					<table:td cssClass="textcentered">
						<b>${marksByCategoriesMap[votingCategory]}</b>
					</table:td>

				</c:forEach>

				<table:td />
			</table:tr>

			<table:tr>
				<table:td colspan="${colspan}">
					<b>${eco:translate1("Total marks: $1", total)}</b>
				</table:td>
			</table:tr>

		</jsp:body>

	</table:table>

</tags:page>

