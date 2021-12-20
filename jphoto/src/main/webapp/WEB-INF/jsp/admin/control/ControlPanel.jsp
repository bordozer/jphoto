<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="controlPanelModel" type="com.bordozer.jphoto.admin.controllers.control.ControlPanelModel" scope="request"/>
<c:set var="tabWidth" value="450"/>
<c:set var="containerWidth" value="30%"/>

<tags:page pageModel="${controlPanelModel.pageModel}">

    <form:form method="POST" modelAttribute="controlPanelModel" action="${eco:baseAdminUrl()}/control-panel/">

        <div class="floatleft">

            <div class="floatleft" style="width: ${containerWidth};">
                <table:table width="${tabWidth}">

                    <table:separatorInfo colspan="2" title="${eco:translate('Control panel: System tab')}"/>

                    <table:tr cssClass="text-centered">
                        <table:td>
                            <html:submitButton id="reload-system-properties" caption_t="Control panel: Reload system properties"
                                               onclick="return submitControlPanelForm( 'reload-system-properties', '${eco:translate('Control panel: Reload system properties')}?' );"/>
                        </table:td>
                    </table:tr>

                    <table:tr>
                        <table:td cssClass="text-centered">
                            <admin:reloadTranslationsButton/>
                        </table:td>
                    </table:tr>

                    <table:tr>
                        <table:td cssClass="text-centered">
                            <html:submitButton id="clear-cache" caption_t="Control panel: Clear system cache"
                                               onclick="return submitControlPanelForm( 'clear-cache', '${eco:translate('Control panel: Clear system cache')}?' );"/>
                        </table:td>
                    </table:tr>
                </table:table>
            </div>

            <div class="floatleft" style="width: ${containerWidth};">
                <table:table width="${tabWidth}">

                    <table:separatorInfo colspan="3" title="${eco:translate('Control panel: Statistics tab')}"/>

                    <table:tr>
                        <table:td cssClass="textright">${eco:translate('Control panel: Users total')}</table:td>
                        <table:td>${controlPanelModel.usersTotal}</table:td>
                        <table:td>
                            <a href="${eco:baseAdminUrl()}/reports/users/"> ${eco:translate('Control panel: Registration graph')}</a>
                        </table:td>
                    </table:tr>

                    <table:tr>
                        <table:td cssClass="textright">${eco:translate('Control panel: Photos total')}</table:td>
                        <table:td>${controlPanelModel.photosTotal}</table:td>
                        <table:td></table:td>
                    </table:tr>

                    <table:tr>
                        <table:td cssClass="textright">${eco:translate('Control panel: Photo previews total')}</table:td>
                        <table:td>${controlPanelModel.photoPreviewsTotal}</table:td>
                        <table:td></table:td>
                    </table:tr>

                    <table:tr>
                        <table:td cssClass="textright">${eco:translate('Control panel: Photo comments total')}</table:td>
                        <table:td>${controlPanelModel.photoCommentsTotal}</table:td>
                        <table:td></table:td>
                    </table:tr>

                    <table:tr>
                        <table:td cssClass="textright">${eco:translate('Control panel: Private messages total')}</table:td>
                        <table:td>${controlPanelModel.privateMessagesTotal}</table:td>
                        <table:td></table:td>
                    </table:tr>

                </table:table>
            </div>

            <div class="floatleft" style="width: ${containerWidth};">
                <table:table width="${tabWidth}">

                    <table:separatorInfo colspan="1" title="${eco:translate('Control panel: Delete user photos')}"/>

                    <table:tr>
                        <table:td>
                            <div class="user-picker-container" style="float: left; width: 100%;"></div>
                            <script type="text/javascript">
                                require(['components/user-picker/user-picker'], function (userPicker) {
                                    userPicker("userIdToCleanup", 0, onSelectUser, $('.user-picker-container'));
                                });

                                function onSelectUser(user) {

                                }
                            </script>
                            <html:submitButton id="cleanup-user" caption_t="Control panel: delete photos"
                                               onclick="return submitControlPanelForm( 'cleanup-user', '${eco:translate('Control panel: Delete user photos?')}' );"/>
                        </table:td>
                    </table:tr>

                </table:table>
            </div>

        </div>

    </form:form>

    <script type="text/javascript">
        function submitControlPanelForm(prefix, confirmation) {
            if (confirm(confirmation)) {
                var form = $('#controlPanelModel');
                form.attr('action', '${eco:baseAdminUrl()}/control-panel/' + prefix + '/');
                form.submit();

                return true;
            }

            return false;
        }
    </script>

</tags:page>
