<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photo" required="true" type="core.general.photo.Photo" %>

<%=ApplicationContextHelper.getEntityLinkUtilsService().getPhotoCardLink( photo )%>