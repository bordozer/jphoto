<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>

<%@ attribute name="jobModel" type="admin.controllers.jobs.edit.AbstractAdminJobModel" required="true" %>

<admin:jobEditData jobModel="${jobModel}">

	<jsp:attribute name="jobForm">

		<div class="row">
			<div class="col-lg-12">
				${eco:translate('The job has no parameters')}
			</div>
		</div>

	</jsp:attribute>

</admin:jobEditData>


