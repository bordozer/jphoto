<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoRatingJobModel" type="com.bordozer.jphoto.admin.controllers.jobs.edit.photoRating.PhotoRatingJobModel" scope="request"/>

<tags:page pageModel="${photoRatingJobModel.pageModel}">

    <admin:jobEditData jobModel="${photoRatingJobModel}">

		<jsp:attribute name="jobForm">

			<tags:dateRange title_t="Job / Photo rating recalculation: Recalculate rating for period"
                            dateRangeTypeId="${photoRatingJobModel.dateRangeTypeId}"
                            dateFrom="${photoRatingJobModel.dateFrom}"
                            dateTo="${photoRatingJobModel.dateTo}"
                            timePeriod="${photoRatingJobModel.timePeriod}"
            />

		</jsp:attribute>

    </admin:jobEditData>

</tags:page>
