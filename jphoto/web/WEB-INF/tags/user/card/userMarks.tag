<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="marksByCategoryInfos" required="true" type="java.util.List" %>

<table:table border="0" width="100%" oddEven="true">

	<table:separatorInfo colspan="3" height="50" title="${eco:translate('Last set marks')}"/>

	<c:forEach var="marksByCategoryInfo" items="${marksByCategoryInfos}" >
		<table:trinfo>
			
			<table:tdright>
				<links:photosByUserByVotingCategory user="${user}" votingCategory="${marksByCategoryInfo.photoVotingCategory}" />
			</table:tdright>
			
			<table:tddata>
				<span title="${eco:translate3('$1 marked $2 photo(s) as $3', user.nameEscaped, marksByCategoryInfo.quantity, marksByCategoryInfo.photoVotingCategory.name)}">${marksByCategoryInfo.quantity} ${eco:translate('photos')}</span>
			</table:tddata>

			<table:tddata>
				<span title="${eco:translate2('Summary marks set for $1: $2', marksByCategoryInfo.photoVotingCategory.name, marksByCategoryInfo.sumMark)}">${marksByCategoryInfo.sumMark} ${eco:translate('marks')}</span>
			</table:tddata>

		</table:trinfo>
	</c:forEach>

</table:table>