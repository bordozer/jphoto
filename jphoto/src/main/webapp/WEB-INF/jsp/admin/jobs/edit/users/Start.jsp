<%@ page import="com.bordozer.jphoto.admin.controllers.jobs.edit.users.UserGenerationModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="userGenerationModel" type="com.bordozer.jphoto.admin.controllers.jobs.edit.users.UserGenerationModel" scope="request"/>

<c:set var="userQtyLimitControl" value="<%=UserGenerationModel.USER_QTY_FORM_CONTROL%>"/>
<c:set var="avatarDirControl" value="<%=UserGenerationModel.AVATAR_DIR_FORM_CONTROL%>"/>

<tags:page pageModel="${userGenerationModel.pageModel}">

    <admin:jobEditData jobModel="${userGenerationModel}">

		<jsp:attribute name="jobForm">

			<div class="row">
                <div class="col-lg-5 text-right">
                        ${eco:translate('User generation job: Users count to generate')}:
                </div>
                <div class="col-lg-7">
                    <form:input path="${userQtyLimitControl}" size="4"/>
                </div>
            </div>

			<div class="row">
                <div class="col-lg-5 text-right">
                        ${eco:translate('User generation job: Avatar dir')}
                </div>
                <div class="col-lg-7">
                    <form:input path="${avatarDirControl}" size="40"/>
                    <br/>
                    <small>${eco:translate('The folder must contain male and female folders with avatars (case does matter)')}</small>
                </div>
            </div>

		</jsp:attribute>

    </admin:jobEditData>

</tags:page>
