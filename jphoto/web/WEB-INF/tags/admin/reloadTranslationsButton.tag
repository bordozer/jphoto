<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<script type="text/javascript" src="<c:url value="/common/js/translationsReload.jsp" /> "></script>
<html:submitButton id="reload-translations" caption_t="Reload translations" onclick="reloadTranslations(); return false;"/>