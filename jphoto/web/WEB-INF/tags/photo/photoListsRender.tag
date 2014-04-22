<%@ tag import="java.util.Set" %>
<%@ tag import="static com.google.common.collect.Sets.newHashSet" %>
<%@ tag import="ui.elements.PhotoList" %>
<%@ tag import="core.general.photo.PhotoInfo" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photoLists" required="true" type="java.util.List" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<c:set var="baseUrl" value="${eco:baseUrl()}" />

<link rel="stylesheet" href="${baseUrl}/css/photo-list.css" type="text/css"/>

<%--<%
	final Set<Integer> photoIds = newHashSet();
	for ( final Object _photoList : photoLists ) {
		final PhotoList photoList = ( PhotoList ) _photoList;
		for ( final PhotoInfo photoInfo : photoList.getPhotoInfos() ) {
			photoIds.add( photoInfo.getPhoto().getId() );
		}
	}
%>
<c:set var="photoIds" value="<%=photoIds%>" />--%>

<tags:contextMenuJs />

<c:forEach var="photoList" items="${photoLists}">

	<%--<photo:photoList photoList="${photoList}" />--%>
	<photo:photoListLaizyLoading photoList="${photoList}" />

</c:forEach>