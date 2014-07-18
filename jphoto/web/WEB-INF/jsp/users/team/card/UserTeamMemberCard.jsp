<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<jsp:useBean id="userTeamMemberCardModel" type="ui.controllers.users.team.card.UserTeamMemberCardModel" scope="request" />

<tags:page pageModel="${userTeamMemberCardModel.pageModel}">

	<photo:photoListsRender photoLists="${userTeamMemberCardModel.photoLists}" />

</tags:page>