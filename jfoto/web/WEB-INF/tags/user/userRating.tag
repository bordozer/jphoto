<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="userRatings" required="true" type="java.util.List" %>
<%@ attribute name="listTitle" required="true" type="java.lang.String" %>

<table:table width="100%">
	<table:separatorInfo colspan="3" title="${listTitle}"/>
	<c:forEach var="userRating" items="${userRatings}" varStatus="status">
		<table:tr>
			<table:tdunderlined width="15" cssClass="textright">${status.index + 1}</table:tdunderlined>
			<table:tdunderlined><user:userCard user="${userRating.user}"/></table:tdunderlined>
			<table:tdunderlined>${userRating.rating}</table:tdunderlined>
		</table:tr>
	</c:forEach>
</table:table>