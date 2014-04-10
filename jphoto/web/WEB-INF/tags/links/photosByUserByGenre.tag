<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="genre" required="true" type="core.general.genre.Genre" %>

${eco:photosByUserByGenreLink(user, genre)}