<%@ tag import="com.bordozer.jphoto.admin.jobs.enums.SavedJobType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<%@ attribute name="activeJobTypes" type="java.util.Set" required="true" %>
<%@ attribute name="jobListTab" type="com.bordozer.jphoto.admin.jobs.enums.JobListTab" required="true" %>
<%@ attribute name="activeJobHistoryMap" type="java.util.Map" required="true" %>

<c:set var="savedJobTypes" value="<%=SavedJobType.values()%>"/>

<div class="panel panel-default">

    <div class="panel-heading">
        <h3 class="panel-title">${eco:translate(jobListTab.name)}</h3>
    </div>

    <div class="panel-body">

        <c:forEach var="jobType" items="${savedJobTypes}">

            <c:if test="${jobListTab == jobType.jobListTab}">

                <c:set var="jobTypeId" value="${jobType.id}"/>

                <div class="row" style="height: 45px;">

                    <div class="col-lg-1">
                        <html:img32 src="jobtype/${jobType.icon}"/>
                    </div>

                    <div class="col-lg-10">

                        <div class="row">
                            <div class="col-lg-12">
                                <admin:jobTemplate savedJobType="${jobType}"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-lg-12">
                                <c:forEach var="entry" items="${activeJobHistoryMap}">
                                    <c:set var="jobHistoryEntryDTO" value="${entry.value}"/>

                                    <c:if test="${jobHistoryEntryDTO.jobType == jobType}">
                                        <div class="row">
                                            <div class="col-lg-12">
                                                <admin:jobExecutionProgress jobHistoryEntryDTO="${jobHistoryEntryDTO}"/>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>

                    </div>
                </div>
            </c:if>

        </c:forEach>

    </div>

</div>



