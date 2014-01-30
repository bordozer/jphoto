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

<c:set var="privateMessageTypeValuesLength" value="<%=privateMessageListModel.getMessagesByType().size()%>"/>
<c:set var="cellWidth" value="${eco:floor(100 / privateMessageTypeValuesLength)-1}"/>
<c:set var="messagesByType" value="${privateMessageListModel.messagesByType}"/>
<c:set var="forUserId" value="${privateMessageListModel.forUser.id}"/>

<c:set var="usersWhoCommunicatedWithUser" value="${privateMessageListModel.usersWhoCommunicatedWithUser}"/>

<tags:page pageModel="${privateMessageListModel.pageModel}">

	<style type="text/css">
		.selectedTab {
			border: 1px solid #d3d3d3;
			border-right-color: #aaaaaa;
			border-bottom-color: #aaaaaa;
			border-radius: 12px 12px 0 0;
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
						<c:set var="css" value="selectedUser block-background"/>
					</c:if>

					<div style="float: left; width: 100%; border-bottom: 1px solid #d3d3d3;" class="${css}">
						<a class="${css}" href="${eco:baseUrlWithPrefix()}/members/${forUserId}/messages/with/${withUser.id}/"
						   title="${eco:translate1('Show messaging with $1', withUser.nameEscaped)}">
								${withUser.nameEscaped}
						</a>
						<br />
						<span style="margin-left: 5px;" title="${eco:translate1('Sent: $1', communicator.sentMessagesCount)}">${eco:translate1('Sent: $1', communicator.sentMessagesCount)}</span>
						<br/>
						<span style="margin-left: 5px;" title="${eco:translate1('Received: $1', communicator.receivedMessagesCount)}">${eco:translate1('Received: $1', communicator.receivedMessagesCount)}</span>
					</div>
					<br/>
				</c:forEach>
			</div>

			<div style="float: left; width: auto;">

				<table:table width="1000" border="0">

					<table:tr>

						<table:td>
							<div style="width: 100%; align: center; margin-left: auto; margin-right: auto; height: 91px; border-bottom: 2px solid #d3d3d3; text-align: center;">

								<c:forEach var="entry" items="${messagesByType}">
									<c:set var="privateMessageType" value="${entry.key}"/>
									<c:set var="messageTypeData" value="${entry.value}"/>

									<div style="float: left; width: ${cellWidth}%; padding-top: 10px; padding-bottom: 10px; text-align: center;" ${selectedPrivateMessageTypeType == privateMessageType ? 'class="selectedTab block-background"' : ''}>
										<messages:privateMessageIcon user="${privateMessageListModel.forUser}" privateMessageType="${privateMessageType}"/>
										<br/>
										${privateMessageType.nameTranslated}
										<br/>
										<span title="${eco:translate('Total messages')}">${messageTypeData.messages}</span>
										<c:if test="${messageTypeData.newMessages > 0}">
											( <span title="${eco:translate('New messages')}">${messageTypeData.newMessages > 0 ? '+' : ''}${messageTypeData.newMessages}</span> )
										</c:if>
									</div>
								</c:forEach>

							</div>
						</table:td>
					</table:tr>

					<c:if test="${privateMessageListModel.showPaging}">
						<table:tr>
							<table:td>
								<tags:paging showSummary="false"/>
							</table:td>
						</table:tr>
					</c:if>

					<table:tr>
						<table:td>
							<js:checkBoxChecker namePrefix="selectedMessagesIds"/>
						</table:td>
					</table:tr>

					<c:forEach var="privateMessage" items="${privateMessages}">

						<table:tr>

							<table:td>

								<messages:privateMessageView privateMessage="${privateMessage}"/>

							</table:td>

						</table:tr>

					</c:forEach>

					<table:tr>
						<table:td cssClass="buttoncolumn">

							<c:if test="${not empty privateMessages}">
								<html:submitButton id="markAllMessagesAsReadButton" caption_t="Mark all messages as read" onclick="return markAllMessagesAsRead();" />
								<html:submitButton id="deleteMessages" caption_t="Delete selected private messages" onclick="return deleteSelectedPrivateMessages();" />
							</c:if>

						</table:td>
					</table:tr>

				</table:table>

				<script type="text/javascript">

					<c:if test="${not empty privateMessages}">
						function deleteSelectedPrivateMessages() {
							var action = "${eco:baseUrlWithPrefix()}/members/${forUserId}/messages/delete/";
							var message = "${eco:translate('Delete selected private messages?')}";

							return performAction( action, message );
						}

						function markAllMessagesAsRead() {
							var action = "${eco:baseUrlWithPrefix()}/members/${forUserId}/messages/type/${privateMessageListModel.privateMessageType.id}/markAllAsRead/";
							var message = "${eco:translate('Mark all messages as read?')}";

							return performAction( action, message );
						}

						function performAction( action, message ) {
							$( '#privateMessageListModel' ).attr( 'action', action );
							return confirm( message );
						}
					</c:if>

					function reloadPageCallbackFunction() {
						document.location.reload();
					}

				</script>

			</div>

		</div>

	</form:form>

	<c:if test="${privateMessageListModel.showPaging}">
		<tags:paging showSummary="true"/>
	</c:if>

	<div class="footerseparator"></div>

</tags:page>