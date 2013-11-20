<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="messages" tagdir="/WEB-INF/tags/messages" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="privateMessage" required="true" type="core.general.message.PrivateMessage" %>

<c:set var="fromUser" value="${privateMessage.fromUser}"/>
<c:set var="toUser" value="${privateMessage.toUser}"/>
<c:set var="privateMessageType" value="${privateMessage.privateMessageType}"/>
<c:set var="messageText" value="${privateMessage.messageText}"/>

<div style="float: left; width: 90%; padding-bottom: 5px; padding-top: 5px;">

	<div style="float: left; width: 100%; height: 20px; border-bottom: 1px solid #CDCDCD;">

		<html:img16 src="messages/${privateMessageType.icon}" alt="${privateMessageType.nameTranslated}" />

		&nbsp;&nbsp;

		<c:if test="${privateMessageType != 'USER_PRIVATE_MESSAGE_OUT'}">
			<c:if test="${privateMessageType == 'USER_PRIVATE_MESSAGE_IN'}">
				${eco:translate('From')} <user:userCard user="${fromUser}"/> /
			</c:if>

			<b>${eco:formatDate(privateMessage.creationTime)} ${eco:formatTimeShort(privateMessage.creationTime)}</b>

			<c:if test="${privateMessage.readTime > 0}">
				/ ${eco:translate('You read this message at ')} ${eco:formatDate(privateMessage.readTime)} ${eco:formatTimeShort(privateMessage.readTime)}
			</c:if>

			<c:if test="${privateMessageType == 'USER_PRIVATE_MESSAGE_IN'}">
				/ <icons:sendPrivateMessage toUser="${fromUser}" callback="reloadPageCallbackFunction" />
			</c:if>
		</c:if>

		<c:if test="${privateMessageType == 'USER_PRIVATE_MESSAGE_OUT'}">
			${eco:translate('To')} <user:userCard user="${toUser}"/> /

			<b>${eco:formatDate(privateMessage.creationTime)} ${eco:formatTimeShort(privateMessage.creationTime)}</b>

			<c:if test="${privateMessage.readTime > 0}">
				/ ${eco:translate1('$1 read this message at ', toUser.nameEscaped)} ${eco:formatDate(privateMessage.readTime)} ${eco:formatTimeShort(privateMessage.readTime)}
			</c:if>

			<c:if test="${privateMessage.readTime == 0}">
				${eco:translate1('$1 has not read this message yet ', toUser.nameEscaped)}
			</c:if>
			/
			<icons:sendPrivateMessage toUser="${toUser}" callback="reloadPageCallbackFunction" />
		</c:if>

	</div>


	<div style="float: left; width: 100%; margin-left: 15px; padding-left: 5px; padding-top: 5px;">
		${messageText}
	</div>

</div>

<script type="text/javascript">
	function reloadPageCallbackFunction() {
		document.location.reload();
	}
</script>
