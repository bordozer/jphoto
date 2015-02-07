<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="portalPageModel" type="ui.controllers.portalpage.PortalPageModel" scope="request" />

<tags:page pageModel="${pageModel}">

	Portal Page

</tags:page>
