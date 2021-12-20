<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="loginRestrictionModel" type="com.bordozer.jphoto.ui.controllers.restriction.LoginRestrictionModel" scope="request"/>
<c:set var="restriction" value="${loginRestrictionModel.restriction}"/>

<tags:page pageModel="${loginRestrictionModel.pageModel}">

    <div style="float: left; width: 150px;">
        <html:img128 src="icons128/notlogged.png"/>
    </div>

    <div style="width: 800px;">
        <h3>${eco:translate("LoginRestriction: Login is restricted")}</h3>
            ${eco:translate("LoginRestriction: You are logged out because you are restricted in this right.")}

        <p>
                ${eco:translate("LoginRestriction: Login restriction creator")}
                <user:userCard user="${restriction.creator}"/>

        <p>
                ${eco:translate("LoginRestriction: The restriction period is")}
                ${eco:translate("LoginRestriction: Login restriction from")}
            <b>${loginRestrictionModel.restrictionDateFrom}</b><sup>${loginRestrictionModel.restrictionTimeFrom}</sup>
                ${eco:translate("LoginRestriction: Login restriction to")}
            <b>${loginRestrictionModel.restrictionDateTo}</b><sup>${loginRestrictionModel.restrictionTimeTo}</sup>

        <p>
                ${eco:translate("LoginRestriction: The reason of restriction")}:
        <p>

                ${not empty restriction.restrictionMessage ? restriction.restrictionMessage : eco:translate("LoginRestriction: unknown reason of restriction")}

    </div>

</tags:page>
