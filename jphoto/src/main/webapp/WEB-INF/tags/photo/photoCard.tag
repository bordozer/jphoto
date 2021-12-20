<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ tag import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photo" required="true" type="com.bordozer.jphoto.core.general.photo.Photo" %>

<%=ApplicationContextHelper.getEntityLinkUtilsService().getPhotoCardLink(photo, EnvironmentContext.getLanguage())%>
