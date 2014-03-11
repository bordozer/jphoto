<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="votingCategory" required="true" type="core.general.photo.PhotoVotingCategory" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotosByUserByVotingCategoryLink( user.getId(), votingCategory.getId() )%>" />

<a href ="${link}" title="${eco:translate2('Photos was appraised \'$1\' by $2', votingCategory.name, eco:escapeHtml(user.name))}">${votingCategory.name}</a>