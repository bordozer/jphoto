<%@ page import="com.bordozer.jphoto.admin.controllers.jobs.edit.rankVoting.RankVotingJobModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="rankVotingJobModel" type="com.bordozer.jphoto.admin.controllers.jobs.edit.rankVoting.RankVotingJobModel" scope="request"/>

<c:set var="actionsQtyControl" value="<%=RankVotingJobModel.ACTIONS_QTY_FORM_CONTROL%>"/>

<tags:page pageModel="${rankVotingJobModel.pageModel}">

    <admin:jobEditData jobModel="${rankVotingJobModel}">

		<jsp:attribute name="jobForm">

			<div class="row">
                <div class="col-lg-5 text-right">
                        ${eco:translate('RankVotingJob: Actions count')}:
                </div>
                <div class="col-lg-7">
                    <form:input path="${actionsQtyControl}" size="4"/>
                </div>
            </div>

		</jsp:attribute>

    </admin:jobEditData>

</tags:page>
