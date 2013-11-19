<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comments" tagdir="/WEB-INF/tags/photo/comments" %>

<jsp:useBean id="photoCommentViewModel" type="controllers.comment.view.PhotoCommentViewModel" scope="request"/>

<c:set var="photoCommentInfo" value="${photoCommentViewModel.photoCommentInfo}"/>

<comments:commentView commentInfo="${photoCommentInfo}" useAnimation="true" />