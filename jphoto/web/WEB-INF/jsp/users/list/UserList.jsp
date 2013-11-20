<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<jsp:useBean id="userListModel" type="controllers.users.list.UserListModel" scope="request"/>

<tags:page pageModel="${userListModel.pageModel}">

	<user:userListWithFilter
			users="${userListModel.userList}"
			listTitle="${userListModel.userListTitle}"
			showPaging="${userListModel.showPaging}"
			userListDataMap="${userListModel.userListDataMap}"
			showEditIcons="${userListModel.showEditIcons}"
			/>
	
</tags:page>
