<%@ tag import="com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="jobModel" type="com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel" required="true" %>

<c:set var="saveJobModeFormControl" value="<%=AbstractAdminJobModel.SAVE_JOB_MODE_FORM_CONTROL%>"/>
<c:set var="saveAsCopyFormControl" value="<%=AbstractAdminJobModel.SAVE_JOB_AS_COPY_FORM_CONTROL%>"/>
<c:set var="saveJobNameFormControl" value="<%=AbstractAdminJobModel.SAVE_JOB_NAME_FORM_CONTROL%>"/>
<c:set var="saveJobActiveFormControl" value="<%=AbstractAdminJobModel.SAVE_JOB_ACTIVE_FORM_CONTROL%>"/>

<input id="${saveJobModeFormControl}" name="${saveJobModeFormControl}" type="hidden" value="1"/>
<input id="${saveAsCopyFormControl}" name="${saveAsCopyFormControl}" type="hidden" value="false"/>

<style type="text/css">
    .job-box {
        width: 800px;
        margin-left: auto;
        margin-right: auto;
        margin-top: 20px;
    }
</style>

<div class="panel panel-info job-box">

    <div class="panel panel-heading">
        <div class="row">
            <div class="col-lg-3">
                <html:img id="save" src="/icons32/save32.png" width="32" height="32" onclick="saveJob();"
                          alt="${eco:translate1('Save job $1', jobModel.jobName)}"/>
                <html:img id="saveAsCopy" src="/icons32/saveAsCopy32.png" width="32" height="32" onclick="saveJobAsCopy();"
                          alt="${eco:translate1('Save job $1 as copy', jobModel.jobName)}"/>
                <html:img id="run" src="/icons32/start32.png" width="32" height="32" onclick="checkAndStart();"
                          alt="${eco:translate1('Run job $1', jobModel.jobName)}"/>
            </div>
            <h3 class='panel-title'>
                ${eco:translate(jobModel.job.jobName)}
            </h3>
        </div>
    </div>

    <div class="panel-body">

        <div class="row">
            <div class="col-lg-6">
                ${eco:translate('Job: Job name')}
            </div>
            <div class="col-lg-6">
                <html:input fieldId="${saveJobNameFormControl}" fieldValue="${jobModel.jobName}" size="30"/>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-6">
                ${eco:translate('Job: This is an active job')}
            </div>
            <div class="col-lg-6">
                <form:checkbox path="${saveJobActiveFormControl}"/>
            </div>
        </div>

    </div>

    <div class="panel-footer">
        ${eco:translate(jobModel.job.jobType.name)}
    </div>
</div>

<script type="text/javascript">
    function saveJob() {
        $('#${saveJobModeFormControl}').val(true);
        $('#FormName').attr('action', '${eco:baseAdminUrl()}/jobs/${jobModel.job.jobType.prefix}/save/');
        $('#FormName').submit();
    }

    function saveJobAsCopy() {
        if (confirmDeletion('${eco:translate1('Job: Save "$1" as copy?', jobModel.job.jobName)}')) {
            $('#${saveAsCopyFormControl}').val(true);
            saveJob();
        }
    }

    function checkAndStart() {
        $('#${saveJobModeFormControl}').val(false);
        $('#FormName').attr('action', '${eco:baseAdminUrl()}/jobs/${jobModel.job.jobType.prefix}/');

        if (confirmDeletion('${eco:translate1('Job: Run "$1?"', jobModel.job.jobName)}')) {
            $('#FormName').submit();
        }
    }
</script>
