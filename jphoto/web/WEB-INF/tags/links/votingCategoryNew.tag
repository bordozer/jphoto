<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getAdminVotingCategoryNewLink()%>" />

<a href ="${link}"><jsp:doBody/></a>