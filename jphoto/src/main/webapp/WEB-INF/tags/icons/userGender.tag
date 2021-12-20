<%@ tag import="com.bordozer.jphoto.core.enums.UserGender" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>

<c:set var="maleGenderId" value="<%=UserGender.MALE.getId()%>"/>

<html:img id="genderIcon${user.id}" src="/icons16/${user.gender.id == maleGenderId ? 'male.png' : 'female.png'}" width="16" height="16"
          alt="${eco:translate(user.gender.name)}"/>
