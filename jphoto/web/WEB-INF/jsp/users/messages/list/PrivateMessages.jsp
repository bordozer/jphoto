<%@ page import="core.enums.PrivateMessageType" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="messages" tagdir="/WEB-INF/tags/messages" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="privateMessageListModel" class="controllers.users.messages.list.PrivateMessageListModel" scope="request"/>

<c:set var="privateMessageTypeValuesLength" value="<%=PrivateMessageType.values().length%>"/>
<c:set var="cellWidth" value="${eco:floor(100 / privateMessageTypeValuesLength)-1}"/>
<c:set var="messagesByType" value="${privateMessageListModel.messagesByType}"/>

<c:set var="usersWhoCommunicatedWithUser" value="${privateMessageListModel.usersWhoCommunicatedWithUser}"/>

<tags:page pageModel="${privateMessageListModel.pageModel}">

	<style type="text/css">
		.selectedTab {
			border: 1px solid #d3d3d3;
			border-right-color: #aaaaaa;
			border-bottom-color: #aaaaaa;
			border-radius: 12px 12px 0 0;
		}

		.selectedUser {
			font-weight: bold;
		}
	</style>

	<c:set var="privateMessages" value="${privateMessageListModel.privateMessages}"/>
	<c:set var="selectedPrivateMessageTypeType" value="${privateMessageListModel.privateMessageType}"/>

	<form:form modelAttribute="privateMessageListModel" method="POST">

		<div style="float: left; width: 100%;">

			<div style="float: left; width: 15%; margin-top: 80px; border-right: solid 1px #d3d3d3;">
				<c:forEach var="communicator" items="${usersWhoCommunicatedWithUser}">

					<c:set var="withUser" value="${communicator.withUser}"/>

					<c:set var="css" value=""/>
					<c:if test="${withUser.id == privateMessageListModel.messagingWithUserId}">
						<c:set var="css" value="selectedUser"/>
					</c:if>

					<a class="${css}" href="${eco:baseUrlWithPrefix()}/members/${privateMessageListModel.forUser.id}/messages/with/${withUser.id}/"
					   title="${eco:translate1('Show messaging with $1', withUser.nameEscaped)}">
							${withUser.nameEscaped}
					</a>
					<br/>
				<span style="float: left; width: 100%; border-bottom: 1px solid #d3d3d3">
					<span style="margin-left: 5px;"
						  title="${eco:translate1('Sent: $1', communicator.sentMessagesCount)}">${eco:translate1('Sent: $1', communicator.sentMessagesCount)}</span>
					<br/>
					<span style="margin-left: 5px;"
						  title="${eco:translate1('Received: $1', communicator.receivedMessagesCount)}">${eco:translate1('Received: $1', communicator.receivedMessagesCount)}</span>
				</span>
					<br/>
				</c:forEach>
			</div>

			<div style="float: left; width: 85px;">

				<table:table width="800" border="0">

					<table:tr>

						<table:td>
							<div style="width: 100%; align: center; margin-left: auto; margin-right: auto; height: 77px; border-bottom: 2px solid #d3d3d3; text-align: center;">

								<c:forEach var="entry" items="${messagesByType}">
									<c:set var="privateMessageType" value="${entry.key}"/>
									<c:set var="messages" value="${entry.value}"/>

									<div style="float: left; width: ${cellWidth}%; padding-top: 10px; padding-bottom: 10px; text-align: center;" ${selectedPrivateMessageTypeType == privateMessageType ? 'class="selectedTab"' : ''}>
										<messages:privateMessageIcon user="${privateMessageListModel.forUser}" privateMessageType="${privateMessageType}"/>
										<br/>
										${privateMessageType.nameTranslated} ( ${messages} )
									</div>
								</c:forEach>

							</div>
						</table:td>
					</table:tr>

					<table:tr>
						<table:td>
							<js:checkBoxChecker namePrefix="selectedMessagesIds"/>
						</table:td>
					</table:tr>

					<c:set var="counter" value="1"/>
					<c:forEach var="privateMessage" items="${privateMessages}">

						<table:tr>

							<table:td>

								<messages:privateMessageView counter="${counter}" privateMessage="${privateMessage}"/>

							</table:td>

						</table:tr>

						<c:set var="counter" value="${counter + 1}"/>

					</c:forEach>

					<table:tr>
						<table:td cssClass="buttoncolumn">
							<html:submitButton id="deleteMessages" caption_t="Delete selected private messages" onclick="return deleteSelectedPrivateMessages();" />
						</table:td>
					</table:tr>

				</table:table>

				<script type="text/javascript">
					function deleteSelectedPrivateMessages() {
						return confirm( "${eco:translate('Delete selected private messages?')}" );
					}
				</script>

			</div>

		</div>

	</form:form>

</tags:page>