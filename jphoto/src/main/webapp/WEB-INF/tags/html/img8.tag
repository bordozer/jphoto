<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="src" required="true" type="java.lang.String" %>
<%@ attribute name="alt" required="false" type="java.lang.String" %>
<%@ attribute name="onclick" required="false" type="java.lang.String" %>

<html:img id="id" src="${src}" width="8" height="8" alt="${alt}" onclick="${onclick}"/>
