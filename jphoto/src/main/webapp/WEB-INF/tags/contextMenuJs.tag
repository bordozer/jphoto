<%@ tag import="com.bordozer.jphoto.ui.services.ajax.AjaxService" %>
<%@ tag import="com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuType" %>
<%@ tag import="com.bordozer.jphoto.ui.services.menu.entry.items.comment.ComplaintReasonType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<c:set var="customDescriptionTextId" value="customDescriptionTextId"/>
<c:set var="sendComplaintDivId" value="sendCoplaintDivId"/>

<c:set var="baseUrl" value="${eco:baseUrl()}"/>

<c:set var="complaintEntities" value="<%=EntryMenuType.values()%>"/>
<c:set var="complaintReasonTypes" value="<%=ComplaintReasonType.values()%>"/>

<c:set var="menuType_PhotoId" value="<%=EntryMenuType.PHOTO.getId()%>"/>

<div id="${sendComplaintDivId}" title="..." style="display: none;">
    <html:textarea inputId="${customDescriptionTextId}" title="${eco:translate('Send complaint form: Message')}" rows="7" cols="50"/>
</div>

<script type="text/javascript">

    function initContextMenuForEntry(entryId, menuTypeId, container) {
        require(['components/menu/context-menu'], function (contextMenu) {
            contextMenu(entryId, menuTypeId, container);
        });
    }
</script>

<script type="text/javascript">

    var complaintEntities = {
        <c:forEach var="complaintEntity" items="${complaintEntities}" varStatus="status">
        '${complaintEntity.id}': "${eco:translate(complaintEntity.name)}" <c:if test="${not status.last}">, </c:if> </c:forEach>
    };
    var complaintReasonTypes = {
        <c:forEach var="complaintReasonType" items="${complaintReasonTypes}" varStatus="status">
        '${complaintReasonType.id}': "${eco:translate(complaintReasonType.name)}" <c:if test="${not status.last}">, </c:if> </c:forEach>
    };

    require(['jquery', 'jquery_ui'], function ($, ui) {

        $(function () {
            $("#${sendComplaintDivId}").dialog({
                height: 300
                , width: 600
                , modal: true
                , autoOpen: false
            });
        });
    });


    function getEntityNameById(typeId, entities) {
        for (var entity in entities) {
            if (entity[0] == typeId) {
                return entities[entity];
            }
        }
        return "TODO";
    }

    function sendComplaintMessage(complaintEntityTypeId, entryId, fromUserId, complaintReasonTypeId) {
        $('#${customDescriptionTextId}').val('');

        $("#${sendComplaintDivId}")
            .dialog('option', 'title', "${eco:translate('Send Complaint Message: Send complaint message dialog title')}" + getEntityNameById(complaintEntityTypeId, complaintEntities) + ' => ' + getEntityNameById(complaintReasonTypeId, complaintReasonTypes) + ', ID=' + entryId)
            .dialog('option', 'buttons', {
                Cancel: function () {
                    $(this).dialog("close");
                },
                "${eco:translate('Send Complaint Message: Send message button text')}": function () {
                    var complaintDTO = new ComplaintMessageDTO(complaintEntityTypeId, entryId, fromUserId, complaintReasonTypeId, $('#${customDescriptionTextId}').val());

                    var ajaxResultDTO = jsonRPC.ajaxService.sendComplaintMessageAjax(complaintDTO);

                    if (!ajaxResultDTO.successful) {
                        showUIMessage_Error(ajaxResultDTO.message);
                        return false;
                    }

                    showUIMessage_Notification("${eco:translate('Send Complaint Message: The message has been sent')}");

                    $(this).dialog("close");
                }
            })
            .dialog("open");
    }

    function ComplaintMessageDTO(complaintEntityTypeId, entryId, fromUserId, complaintReasonTypeId, customDescription) {
        this.complaintEntityTypeId = complaintEntityTypeId;
        this.entryId = entryId;
        this.fromUserId = fromUserId;
        this.complaintReasonTypeId = complaintReasonTypeId;
        this.customDescription = customDescription;

        this.getComplaintEntityTypeId = function () {
            return this.complaintEntityTypeId;
        };

        this.getEntryId = function () {
            return this.entryId;
        };

        this.getFromUserId = function () {
            return this.fromUserId;
        };

        this.getComplaintReasonTypeId = function () {
            return this.complaintReasonTypeId;
        };

        this.getCustomDescription = function () {
            return this.customDescription;
        };
    }

    function goToMemberPhotos(memberId) {
        document.location.href = '${eco:baseUrl()}/photos/members/' + memberId + '/';
    }

    function goToMemberPhotosByGenre(memberId, genreId) {
        document.location.href = '${eco:baseUrl()}/photos/members/' + memberId + '/genre/' + genreId + '/';
    }

    function goToMemberPhotosByTeamMember(memberId, teamMemberId) {
        document.location.href = '${eco:baseUrl()}/members/' + memberId + '/team/' + teamMemberId + '/';
    }

    function goToMemberPhotosByAlbum(memberId, albumId) {
        document.location.href = '${eco:baseUrl()}/members/' + memberId + '/albums/' + albumId + '/';
    }

    function editPhotoData(photoId) {
        document.location.href = '${eco:baseUrl()}/photos/' + photoId + '/edit/';
    }

    function deletePhoto(photoId) {
        if (confirmDeletion('${eco:translate("Context menu: Delete photo?")}')) {
            document.location.href = '${eco:baseUrl()}/photos/' + photoId + '/delete/';
        }
    }
</script>
