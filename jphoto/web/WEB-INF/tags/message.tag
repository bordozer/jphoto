<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="id" required="true" type="java.lang.String" %>
<%@ attribute name="title_t" required="true" type="java.lang.String" %>
<%@ attribute name="bgColor" required="true" type="java.lang.String" %>
<%@ attribute name="fontColor" required="true" type="java.lang.String" %>
<%@ attribute name="icon" required="true" type="java.lang.String" %>
<%@ attribute name="messageText" required="false" type="java.lang.String" %>

<div id="${id}" style="width: 400px; height: auto; padding: 10px; background: ${bgColor}; border: solid 2px ${fontColor}; color: ${fontColor};  min-height: 70px; display: none;" onclick="fadeoutAndCloseMessageBox( '${id}' );">

	<div id="messageDivIcon" style="float: left; width: 15%; height: 30%; vertical-align: middle;">
		<html:img id="messageImage" src="icons32/${icon}" width="32" height="32"/>
	</div>

	<div id="messageDivTitle" style="float: left; width: 75%; height: 30%; text-align: center; vertical-align: middle; font-weight: bold;">${eco:translate(title_t)}</div>

	<div id="messageDivClose" style="float: right; width: 5%; height: 30%; text-align: center; vertical-align: middle;">
		<html:img id="${id}_close" src="icons16/close16.png" width="16" height="16" onclick="closeMessageBox( '${id}' );"/>
	</div>

	<div id="${id}_message" style="float: right; width: 100%; height: 50%; vertical-align: top; padding-top: 15px;">${messageText}</div>

</div>