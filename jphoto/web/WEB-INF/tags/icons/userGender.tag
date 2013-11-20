<%@ tag import="core.enums.UserGender" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>

<c:set var="maleGenderId" value="<%=UserGender.MALE.getId()%>" />

<html:img id="genderIcon${user.id}" src="/icons16/${user.gender.id == maleGenderId ? 'male.png' : 'female.png'}" width="16" height="16" alt="${user.gender.nameTranslated}" />