<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>
<%@ attribute name="votingCategory" required="true" type="com.bordozer.jphoto.core.general.photo.PhotoVotingCategory" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotosByUserByVotingCategoryLink( user.getId(), votingCategory.getId() )%>"/>

<c:set var="votingCategoryNameTranslated" value="${eco:translateVotingCategory(votingCategory.id)}"/>

<a href="${link}"
   title="${eco:translate2('Links: Photos was appraised $1 by $2', votingCategoryNameTranslated, eco:escapeHtml(user.name))}">${votingCategoryNameTranslated}</a>
