<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photo" required="true" type="core.general.photo.Photo" %>

<%=ApplicationContextHelper.getEntityLinkUtilsService().getPhotoCardLink( photo, EnvironmentContext.getLanguage() )%>