<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="controlPanelModel" type="admin.controllers.control.ControlPanelModel" scope="request"/>

<tags:page pageModel="${controlPanelModel.pageModel}">

	<form:form method="POST" modelAttribute="controlPanelModel" action="${eco:baseAdminUrlWithPrefix()}/control-panel/" >

		<div class="floatleft">

			<div class="floatleft" style="width: 30%;">
				<html:submitButton id="reload-system-properties" caption_t="Reload system properties"
								   onclick="return submitControlPanelForm( 'reload-system-properties', '${eco:translate('Reload system properties?')}' );"/>
				<br />
				<html:submitButton id="reload-translations" caption_t="Reload translations"
								   onclick="return submitControlPanelForm( 'reload-translations', '${eco:translate('Reload translations?')}' );"/>
				<br />
				<html:submitButton id="clear-cache" caption_t="Clear system cache"
								   onclick="return submitControlPanelForm( 'clear-cache', '${eco:translate('Clear system cache?')}' );"/>
			</div>

			<div class="floatleft" style="width: 30%;">
				<table:table>

					<table:tr>
						<table:td cssClass="textright">${eco:translate('Users total')}</table:td>
						<table:td>${controlPanelModel.usersTotal}</table:td>
					</table:tr>

					<table:tr>
						<table:td cssClass="textright">${eco:translate('Photos total')}</table:td>
						<table:td>${controlPanelModel.photosTotal}</table:td>
					</table:tr>

					<table:tr>
						<table:td cssClass="textright">${eco:translate('Photo previews total')}</table:td>
						<table:td>${controlPanelModel.photoPreviewsTotal}</table:td>
					</table:tr>

					<table:tr>
						<table:td cssClass="textright">${eco:translate('Photo comments total')}</table:td>
						<table:td>${controlPanelModel.photoCommentsTotal}</table:td>
					</table:tr>

					<table:tr>
						<table:td cssClass="textright">${eco:translate('Private messages total')}</table:td>
						<table:td>${controlPanelModel.privateMessagesTotal}</table:td>
					</table:tr>

				</table:table>
			</div>

		</div>

	</form:form>

	<script type="text/javascript">
		function submitControlPanelForm( prefix, confirmation ) {
			if ( confirm( confirmation ) ) {
				var form = $( '#controlPanelModel' );
				form.attr( 'action', '${eco:baseAdminUrlWithPrefix()}/control-panel/' + prefix + '/' );
				form.submit();

				return true;
			}

			return false;
		}
	</script>

</tags:page>