<%@ page import="ui.elements.PhotoList" %>
<%@ page import="static com.google.common.collect.Lists.newArrayList" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<jsp:useBean id="userTeamMemberCardModel" type="ui.controllers.users.team.card.UserTeamMemberCardModel" scope="request" />

<%
	final List<PhotoList> photoLists = newArrayList();
	photoLists.add( userTeamMemberCardModel.getPhotoList() );
%>
<c:set var="photoLists" value="<%=photoLists%>" />

<tags:page pageModel="${userTeamMemberCardModel.pageModel}">

	<photo:photoListsRender photoLists="${userTeamMemberCardModel.photoList}" />

</tags:page>