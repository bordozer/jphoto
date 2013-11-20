<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<%@ attribute name="text_t" type="java.lang.String" required="false" description="Header" %>
<%@ attribute name="title_t" type="java.lang.String" required="false" description="Title" %>
<%@ attribute name="width" type="java.lang.String" required="false" description="Column width" %>

<table:td width="${width}"><span title="${eco:translate(title_t)}">${eco:translate(text_t)}</span></table:td>