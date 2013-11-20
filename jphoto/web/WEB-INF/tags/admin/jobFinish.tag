<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="jobModel" type="admin.controllers.jobs.edit.AbstractAdminJobModel" required="true" %>

&nbsp;&nbsp;${eco:translate('Users: ')} <b>${jobModel.usersTotal}</b>
<br />
&nbsp;&nbsp;${eco:translate('Photos: ')} <b>${jobModel.photosTotal}</b>


