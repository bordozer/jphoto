<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<%@ attribute name="photoList" required="true" type="ui.elements.PhotoList" %>

<photo:photoListHeader photoList="${photoList}" />

