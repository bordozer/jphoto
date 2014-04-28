<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="photoEditDataModel" type="ui.controllers.photos.DEL_edit.PhotoEditDataModel" scope="request" />

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="isNew" value="<%=photoEditDataModel.isNew()%>"/>

<tags:page pageModel="${photoEditDataModel.pageModel}">
	The photo's data is saved sucessfully
</tags:page>