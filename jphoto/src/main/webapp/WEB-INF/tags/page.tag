<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ tag import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ tag import="com.bordozer.jphoto.ui.services.ajax.AjaxService" %>
<%@ tag import="com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="pageModel" required="true" type="com.bordozer.jphoto.ui.elements.PageModel" %>

<c:set var="baseUrl" value="${eco:baseUrl()}"/>
<c:set var="isSuperAdmin" value="<%=ApplicationContextHelper.getSecurityService().isSuperAdminUser( EnvironmentContext.getCurrentUser().getId() )%>"/>
<c:set var="menuType_PhotoId" value="<%=EntryMenuType.PHOTO.getId()%>"/>

<eco:page pageModel="${pageModel}">

    <script type="text/javascript">

        function showUIMessage_Notification(messageText) {
            require(['jquery', 'ui_messages'], function ($, ui_messages) {
                ui_messages.showUIMessage_Notification(messageText);
            });
        }

        function showUIMessage_InformationMessage_ManualClosing(messageText) {
            require(['jquery', 'ui_messages'], function ($, ui_messages) {
                ui_messages.showUIMessage_InformationMessage_ManualClosing(messageText);
            });
        }

        function showUIMessage_Information(messageText) {
            require(['jquery', 'ui_messages'], function ($, ui_messages) {
                ui_messages.showUIMessage_Information(messageText);
            });
        }

        function showUIMessage_Warning(messageText) {
            require(['jquery', 'ui_messages'], function ($, ui_messages) {
                ui_messages.showUIMessage_Warning(messageText);
            });
        }

        function showUIMessage_Error(messageText) {
            require(['jquery', 'ui_messages'], function ($, ui_messages) {
                ui_messages.showUIMessage_Error(messageText);
            });
        }

        function showUIMessage_FromCustomDiv(element) {
            require(['jquery', 'ui_messages'], function ($, ui_messages) {
                ui_messages.showUIMessage_FromCustomDiv(element);
            });
        }

        function renderEntryIcon(userId, bookmarkEntryId, bookmarkEntryTypeId, iconSize, container) {
            require(['jquery', 'modules/icon/entry-icon'], function ($, bookmarking) {
                bookmarking(userId, bookmarkEntryId, bookmarkEntryTypeId, iconSize, container);
            });
        }
    </script>

    <div id="sendPrivateMessageToUserDivId" title="..." style="display: none;">
        <html:textarea inputId="privateMessageTextId" title="${eco:translate('Private message form: Title')}"
                       hint="${eco:translate('Private message form: Text')}" rows="7" cols="50"/>
    </div>

    <script type="text/javascript">

        require(['jquery', 'jquery_ui'], function ($) {
            $(function () {
                $("#sendPrivateMessageToUserDivId").dialog({
                    height: 300
                    , width: 600
                    , modal: true
                    , autoOpen: false
                });
            });
        });

        function sendPrivateMessage(fromUserId, toUserId, toUserName, callback) {
            require(['jquery', '/js/pages/send-private-message.js.jsp'], function ($, sendMessageFunction) {
                sendMessageFunction.sendPrivateMessage(fromUserId, toUserId, toUserName, callback);
            });
        }

    </script>


    <c:if test="${isSuperAdmin}">

        <div id="restrictEntryIFrameDivId" title="..." style="display: none;">
            <iframe id="restrictEntryIFrame" src="" width="98%" height="98%" style="border: none;"></iframe>
        </div>

        <script type="text/javascript">

            require(['jquery', 'jquery_ui'], function ($) {
                $(function () {
                    $("#restrictEntryIFrameDivId").dialog({
                        height: 500
                        , width: 1000
                        , modal: true
                        , autoOpen: false
                    });
                });
            });

            function adminRestrictUser(userId, userName) {
                require(['jquery', '/admin/js/common.js'], function ($, adminFunctions) {
                    adminFunctions.adminRestrictUser(userId, userName);
                });
            }

            function adminRestrictPhoto(photoId, photoName) {
                require(['jquery', '/admin/js/common.js'], function ($, adminFunctions) {
                    adminFunctions.adminRestrictPhoto(photoId, photoName);
                });
            }

            function adminPhotoNudeContentSet(photoId, callback) {
                require(['jquery', '/admin/js/common.js'], function ($, adminFunctions) {
                    adminFunctions.adminPhotoNudeContentSet(photoId, callback);
                });
            }

            function adminPhotoNudeContentRemove(photoId, callback) {
                require(['jquery', '/admin/js/common.js'], function ($, adminFunctions) {
                    adminFunctions.adminPhotoNudeContentRemove(photoId, callback);
                });
            }

            function movePhotoToCategory(photoId, genreId, callback) {
                if (confirm('${eco:translate( 'Move photo to the selected category?' )}')) {
                    require(['jquery', '/admin/js/common.js'], function ($, adminFunctions) {
                        adminFunctions.movePhotoToCategory(photoId, genreId, callback);
                    });
                }
            }

            function generatePhotoPreview(photoId, callback) {
                require(['jquery', '/admin/js/common.js'], function ($, adminFunctions) {
                    adminFunctions.generatePhotoPreview(photoId, callback);
                });
            }

            function reloadTranslations(photoId, callback) {
                require(['jquery', '/admin/js/translations-reload.js'], function ($, translations) {
                    translations.reloadTranslations(photoId, callback);
                });
            }
        </script>

    </c:if>

    <%--<div class="row">--%>
    <%--<div class="col-lg-12" style="margin-left: auto; margin-right: auto;">--%>
    <jsp:doBody/>
    <%--</div>--%>
    <%--</div>--%>

    <tags:devMode>
        <input type="hidden" id="the_end_of_the_page_content">
    </tags:devMode>

</eco:page>
