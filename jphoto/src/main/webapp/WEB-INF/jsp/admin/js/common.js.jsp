<%@ page contentType="text/javascript" %>

    <%@ taglib prefix="eco" uri="http://taglibs" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    define(['jquery'], function ($) {

        function executeAjaxRequest(_url, callback) {

            $.ajax({
                type: 'GET',
                url: _url,
                success: function (response) {
                    if (callback) {
                        callback();
                    }
                },
                error: function () {
                    showUIMessage_Error("${eco:translate('Error executing ajax query')}");
                }
            });
        }

        return {
            adminRestrictUser: function (userId, userName) {

                var url = "${eco:baseAdminUrl()}/restrictions/members/" + userId + "/";
                $('#restrictEntryIFrame').attr('src', url);

                $("#restrictEntryIFrameDivId")
                    .dialog('option', 'title', "${eco:translate('Admin User Restriction: Restrict user dialog title')}" + ' ' + userName + ' ( #' + userId + ' )')
                    .dialog('option', 'buttons', {
                        Cancel: function () {
                            $(this).dialog("close");
                        }
                    })
                    .dialog("open");
            },

            adminRestrictPhoto: function (photoId, photoName) {

                var url = "${eco:baseAdminUrl()}/restrictions/photos/" + photoId + "/";
                $('#restrictEntryIFrame').attr('src', url);

                $("#restrictEntryIFrameDivId")
                    .dialog('option', 'title', "${eco:translate('Admin Photo Restriction: Restrict photo dialog title')}: " + " '" + photoName + "' ( #" + photoId + ' )')
                    .dialog('option', 'buttons', {
                        Cancel: function () {
                            $(this).dialog("close");
                        }
                    })
                    .dialog("open");
            },

            adminPhotoNudeContentSet: function (photoId, callback) {
                var url = "${eco:baseUrl()}/com.bordozer.jphoto.rest/photos/" + photoId + "/nude-content/true/";
                executeAjaxRequest(url, callback);
            },

            adminPhotoNudeContentRemove: function (photoId, callback) {
                var url = "${eco:baseUrl()}/com.bordozer.jphoto.rest/photos/" + photoId + "/nude-content/false/";
                executeAjaxRequest(url, callback);
            },

            movePhotoToCategory: function (photoId, genreId, callback) {
                var url = "${eco:baseUrl()}/com.bordozer.jphoto.rest/photos/" + photoId + "/move-to-genre/" + genreId + "/";
                executeAjaxRequest(url, callback);
            },

            generatePhotoPreview: function (photoId, callback) {
                var url = "${eco:baseUrl()}/com.bordozer.jphoto.rest/photos/" + photoId + "/preview/";
                executeAjaxRequest(url, callback);
            }
        }
    });
