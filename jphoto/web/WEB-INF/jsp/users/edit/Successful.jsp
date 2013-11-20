<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="core.general.user.User" %>

<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="userEditDataModel" type="controllers.users.edit.UserEditDataModel" scope="request" />

<c:set var="model" value="<%=userEditDataModel%>"/>
<c:set var="userId" value="<%=userEditDataModel.getUserId()%>"/>
<c:set var="isNew" value="<%=userEditDataModel.isNew()%>"/>

<tags:page pageModel="${userEditDataModel.pageModel}">
	<c:if test="${isNew}">
		<em><strong>${model.name}</strong></em>, ${eco:translate('you are registered now. Please, login.')}
	</c:if>

	<c:if test="${!isNew}">
		<em><strong>${model.name}</strong></em>, ${eco:translate('your data is saved.')}
	</c:if>
</tags:page>
