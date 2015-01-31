<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="caption_t" required="true" type="java.lang.String" %>
<%@ attribute name="onclick" type="java.lang.String" required="false" description="Button onclick" %>

<button type="button" class="btn btn-default" onclick="${onclick}">${eco:translate(caption_t)}</button>