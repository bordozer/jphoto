<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="userReportModel" type="com.bordozer.jphoto.admin.controllers.reports.users.UserReportModel" scope="request"/>

<tags:page pageModel="${userReportModel.pageModel}">

    <script type="text/javascript" src="${eco:baseUrl()}/js/lib/jQuery-Visualize-master/js/visualize.jQuery.js"></script>
    <link rel="stylesheet" href="${eco:baseUrl()}/js/lib/jQuery-Visualize-master/css/basic.css" type="text/css"/>
    <link rel="stylesheet" href="${eco:baseUrl()}/js/lib/jQuery-Visualize-master/css/visualize.css" type="text/css"/>

    <style type="text/css">
        .rotate {
            -moz-transform: rotate(-270deg);
            -moz-transform-origin: bottom left;
            -webkit-transform: rotate(-270deg);
            -webkit-transform-origin: bottom left;
            -o-transform: rotate(-270deg);
            -o-transform-origin: bottom left;
            filter: progid:DXImageTransform.Microsoft.BasicImage(rotation=1);

            text-wrap: avoid;
        }

        .label {
            width: 400px;
        }
    </style>

    <c:set var="registrationsMap" value="${userReportModel.registrationsMap}"/>

    <div class="floatleft" style="padding: 50px;">

        <table id="userRegistrationGraph" style="display: none;">
            <caption>${eco:translate('User registrations by date graph')}</caption>
            <thead>
            <tr>
                <c:set var="counter" value="0"/>

                <c:forEach var="entry" items="${registrationsMap}" varStatus="status">
                    <c:set var="date" value="${entry.key}"/>
                    <c:set var="showTrace" value="${counter == 0}"/>

                    <c:if test="${showTrace}">
                        <th scope="col"><span class="rotate">${eco:formatDate(date)}</span></th>
                        <c:set var="counter" value="5"/>
                    </c:if>

                    <c:if test="${not showTrace}">
                        <th scope="col"></th>
                    </c:if>

                    <c:set var="counter" value="${counter - 1}"/>
                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <tr>
                <c:forEach var="entry" items="${registrationsMap}">
                    <c:set var="date" value="${entry.key}"/>
                    <c:set var="registrationData" value="${entry.value}"/>
                    <td scope="row">${registrationData.users}</td>
                </c:forEach>
            </tr>
            </tbody>
        </table>
    </div>

    <script type="text/javascript">

        require(['jquery'], function ($) {
            $(document).ready(function () {
                var chartOptions = {type: 'line', width: 1300, height: 300, lineWeight: 2, barGroupMargin: 10};
                $('#userRegistrationGraph').visualize(chartOptions);
            });
        });

    </script>

</tags:page>
