<%@ tag import="controllers.voting.PhotoVotingModel" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<%@ attribute name="votingCategories" required="true" type="java.util.List" %>
<%@ attribute name="number" required="true" type="java.lang.Integer" %>
<%@ attribute name="minMarkForGenre" required="false" type="java.lang.Integer" %>
<%@ attribute name="maxMarkForGenre" required="false" type="java.lang.Integer" %>

<c:set var="votingCategoryMarkControl" value="<%=PhotoVotingModel.VOTING_CATEGORY_MARK_CONTROL%>" />

<table:tr>

	<table:td  width="195px" align="right">
		<select id="votingCategory${number}" name="votingCategory${number}" onchange="setAccessToVotingCategoryMark( ${number} )" class="votingCategoryClass">
			<option value="0"> - - - - - - - - </option>
			<c:forEach var="votingCategory" items="${votingCategories}">
				<option value="${votingCategory.id}">${eco:translateVotingCategory(votingCategory.id)}&nbsp;&nbsp;</option>
			</c:forEach>
		</select>
	</table:td>

	<table:td align="left">
		<span id="votingCategoryMarkContainer${number}"></span>
		<select id="${votingCategoryMarkControl}${number}" name="${votingCategoryMarkControl}${number}">

			<c:forEach begin="1" end="${maxMarkForGenre}" var="counter">
				<c:set var="optionValue" value="${ maxMarkForGenre - counter + 1}" />

				<option value="${optionValue}" <c:if test="${optionValue == 1}">selected</c:if> >
					+${optionValue}&nbsp;
				</option>
			</c:forEach>

			<c:set var="minMarkValueAbs" value="${-minMarkForGenre}" />
			<c:forEach begin="1" end="${minMarkValueAbs}" var="counter">
				<c:set var="optionValue" value="${counter}" />

				<option value="${-counter}">-${optionValue}&nbsp;</option>
			</c:forEach>
		</select>
	</table:td>

</table:tr>