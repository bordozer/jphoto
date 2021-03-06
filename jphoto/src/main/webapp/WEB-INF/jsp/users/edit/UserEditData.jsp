<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl" %>
<%@ page import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ page import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ page import="com.bordozer.jphoto.ui.controllers.users.edit.UserEditDataModel" %>
<%@ page import="com.bordozer.jphoto.ui.controllers.users.edit.UserEditDataValidator" %>
<%@ page import="com.bordozer.jphoto.ui.services.validation.DataRequirementService" %>
<%@ page import="com.bordozer.jphoto.ui.services.validation.UserRequirement" %>
<%@ page import="com.bordozer.jphoto.ui.translatable.GenericTranslatableList" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="userEditDataModel" type="com.bordozer.jphoto.ui.controllers.users.edit.UserEditDataModel" scope="request"/>

<%
    final Integer[] photosOnPageValues = {16, 20, 24, 28, 32, 36, 40};
%>

<%
    final int minLoginLength = userEditDataModel.getMinLoginLength();
    final int maxLoginLength = userEditDataModel.getMaxLoginLength();

    final int minUserNameLength = userEditDataModel.getMinUserNameLength();
    final int maxUserNameLength = userEditDataModel.getMaxUserNameLength();
%>

<c:set var="isNew" value="<%=userEditDataModel.isNew()%>"/>

<c:set var="userId" value="<%=userEditDataModel.getUserId()%>"/>
<c:set var="userLogin" value="<%=userEditDataModel.getLogin()%>"/>
<c:set var="password" value="<%=userEditDataModel.getPassword()%>"/> <%-- TODO: remove this, MUST be empty!--%>
<c:set var="confirmPassword" value="<%=userEditDataModel.getConfirmPassword()%>"/> <%-- TODO: remove this, MUST be empty!--%>
<c:set var="userName" value="<%=userEditDataModel.getName()%>"/>
<c:set var="userEmail" value="<%=userEditDataModel.getEmail()%>"/>
<c:set var="userHomeSite" value="<%=userEditDataModel.getHomeSite()%>"/>
<c:set var="selfDescription" value="<%=userEditDataModel.getSelfDescription()%>"/>
<c:set var="userDateOfBirth" value="<%=userEditDataModel.getDateOfBirth()%>"/>

<c:set var="userIdControl" value="<%=UserEditDataModel.USER_ID_FORM_CONTROL%>"/>
<c:set var="userLoginControl" value="<%=UserEditDataModel.USER_LOGIN_FORM_CONTROL%>"/>
<c:set var="userPasswordControl" value="<%=UserEditDataModel.USER_PASSWORD_FORM_CONTROL%>"/>
<c:set var="userConfirmPasswordControl" value="<%=UserEditDataModel.USER_CONFIRM_PASSWORD_FORM_CONTROL%>"/>
<c:set var="filterUserNameControl" value="<%=UserEditDataModel.USER_NAME_FORM_CONTROL%>"/>
<c:set var="userEmailControl" value="<%=UserEditDataModel.USER_EMAIL_FORM_CONTROL%>"/>
<c:set var="userHomeSiteControl" value="<%=UserEditDataModel.USER_HOME_SITE_FORM_CONTROL%>"/>
<c:set var="selfDescriptionControl" value="<%=UserEditDataModel.USER_SELF_DESCRIPTION_FORM_CONTROL%>"/>
<c:set var="userDateOfBirthControl" value="<%=UserEditDataModel.USER_DATE_OF_BIRTH_FORM_CONTROL%>"/>
<c:set var="membershipTypeControl" value="<%= UserEditDataModel.MEMBERSHIP_TYPE_FORM_CONTROL%>"/>
<c:set var="showNudeContentControl" value="<%= UserEditDataModel.FORM_CONTROL_SHOW_NUDE_CONTENT%>"/>
<c:set var="membershipTypeId1" value="membershipTypeId1"/>

<c:set var="emailNotificationOptionIdsControl" value="<%=UserEditDataModel.FORM_CONTROL_EMAIL_NOTIFICATION_OPTION_IDS%>"/>

<c:set var="defaultPhotoCommentsAllowanceIdControl" value="<%=UserEditDataModel.FORM_CONTROL_DEFAULT_COMMENTS_ALLOWANCE_ID%>"/>
<c:set var="defaultPhotoVotingAllowanceIdControl" value="<%=UserEditDataModel.FORM_CONTROL_DEFAULT_VOTING_ALLOWANCE_ID%>"/>

<c:set var="photosOnPageControl" value="userEditDataModel.photosOnPage"/>
<c:set var="photosOnPageValues" value="<%=photosOnPageValues%>"/>

<c:set var="membershipTypeListValues"
       value="<%=GenericTranslatableList.userMembershipTypeTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>
<c:set var="userGenderValues"
       value="<%=GenericTranslatableList.userGenderTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>
<c:set var="emailNotificationOptionsValues"
       value="<%=GenericTranslatableList.emailNotificationTypeTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>

<c:set var="accessibleCommentAllowances" value="${userEditDataModel.accessibleCommentAllowances}"/>
<c:set var="accessibleVotingAllowances" value="${userEditDataModel.accessibleVotingAllowances}"/>

<c:set var="userGenderControl" value="userEditDataModel.userGenderId"/>
<c:set var="userGenderId1" value="userGenderId1"/>

<c:set var="separatorHeight" value="50"/>

<%
    final DataRequirementService dataRequirementService = userEditDataModel.getDataRequirementService();
    final UserRequirement userRequirement = userEditDataModel.getDataRequirementService().getUserRequirement();
%>

<c:set var="loginRequirement" value="<%=userRequirement.getLoginRequirement( minLoginLength, maxLoginLength )%>"/>

<c:set var="passwordRequirement" value="<%=userRequirement.getPasswordRequirement( false )%>"/> <%-- TODO: set to true--%>
<c:set var="confirmPasswordRequirement" value="<%=userRequirement.getConfirmPasswordRequirement()%>"/>
<c:set var="nameRequirement" value="<%=userRequirement.getNameRequirement( minUserNameLength, maxUserNameLength )%>"/>
<c:set var="emailRequirement" value="<%=userRequirement.getEmailRequirement()%>"/>
<c:set var="birthdayRequirement" value="<%=userRequirement.getBirthdayRequirement()%>"/>
<c:set var="homeSiteRequirement" value="<%=userRequirement.getHomeSiteRequirement()%>"/>

<c:set var="mandatoryText" value="<%=dataRequirementService.getFieldIsMandatoryText()%>"/>
<c:set var="optionalText" value="<%=dataRequirementService.getFieldIsOptionalText()%>"/>

<c:set var="minLoginLength" value="<%=minLoginLength%>"/>
<c:set var="maxLoginLength" value="<%=maxLoginLength%>"/>

<c:set var="minUserNameLength" value="<%=minUserNameLength%>"/>
<c:set var="maxUserNameLength" value="<%=maxUserNameLength%>"/>

<c:set var="userPathPrefix" value="<%=UrlUtilsServiceImpl.USERS_URL%>"/>

<tags:page pageModel="${userEditDataModel.pageModel}">

    <tags:inputHintForm/>

    <eco:form action="${eco:baseUrl()}/${userPathPrefix}/${userId}/save/">

        <input type="hidden" name="${userIdControl}" id="${userIdControl}" value="${userId}"/>

        <table:table width="800">

            <table:separatorInfo colspan="2" height="${separatorHeight}" title="${eco:translate('User edit data tab: Account information')}"/>

            <table:tredit>
                <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_LOGIN%>" labelFor="${userLoginControl}" isMandatory="true"/>

                <table:tddata>
                    <%--<tags:inputHint inputId="${userLoginControl}" hintTitle_t="<%=UserEditDataValidator.USER_DATA_LOGIN%>" hint="${loginRequirement}<br /><br />${mandatoryText}" focused="true">--%>
                    <%--<jsp:attribute name="inputField">--%>
                    <html:input fieldId="${userLoginControl}" fieldValue="${userLogin}" maxLength="${maxUserNameLength}"/>
                    <br/>
                    ${loginRequirement}
                    <%--</jsp:attribute>--%>
                    <%--</tags:inputHint>--%>
                </table:tddata>
            </table:tredit>

            <c:if test="${isNew}">
                <table:tredit>
                    <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_LAST_PASSWORD%>" labelFor="${userPasswordControl}" isMandatory="true"/>

                    <table:tddata>
                        <%--<tags:inputHint inputId="${userPasswordControl}" hintTitle_t="Password" hint="${passwordRequirement}<br /><br />${mandatoryText}">--%>
                        <%--<jsp:attribute name="inputField">--%>
                        <html:password fieldId="${userPasswordControl}" fieldValue="${password}"/>
                        <%--</jsp:attribute>--%>
                        <%--</tags:inputHint>--%>
                    </table:tddata>
                </table:tredit>

                <table:tredit>
                    <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_LAST_PASSWORD_REPEAT%>" labelFor="${userConfirmPasswordControl}"
                                  isMandatory="true"/>

                    <table:tddata>
                        <%--<tags:inputHint inputId="${userConfirmPasswordControl}" hintTitle_t="Confirm Password" hint="${confirmPasswordRequirement}<br /><br />${mandatoryText}">--%>
                        <%--<jsp:attribute name="inputField">--%>
                        <html:password fieldId="${userConfirmPasswordControl}" fieldValue="${confirmPassword}"/>
                        <br/>
                        ${passwordRequirement}
                        <%--</jsp:attribute>--%>
                        <%--</tags:inputHint>--%>
                    </table:tddata>
                </table:tredit>
            </c:if>

            <table:tredit>
                <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_MEMBERSHIP_TYPE%>" labelFor="${membershipTypeId1}"/>

                <table:tddata>
                    <form:radiobuttons items="${membershipTypeListValues}" path="userEditDataModel.${membershipTypeControl}" itemLabel="name" itemValue="id"
                                       delimiter="<br/>" htmlEscape="false"/>
                </table:tddata>
            </table:tredit>

            <table:separatorInfo colspan="2" height="${separatorHeight}" title="${eco:translate('User edit data tab: Personal information')}"/>

            <table:tredit>
                <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_GENDER%>" labelFor="${userGenderId1}"/>

                <table:tddata>
                    <form:radiobuttons path="${userGenderControl}" items="${userGenderValues}" itemValue="id" itemLabel="name" delimiter="<br/>"
                                       htmlEscape="false"/>
                </table:tddata>
            </table:tredit>

            <table:tredit>
                <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_NAME%>" labelFor="${filterUserNameControl}" isMandatory="true"/>

                <table:tddata>
                    <%--<tags:inputHint inputId="${filterUserNameControl}" hintTitle_t="Name" hint="${nameRequirement}<br /><br />${mandatoryText}">--%>
                    <%--<jsp:attribute name="inputField">--%>
                    <html:input fieldId="${filterUserNameControl}" fieldValue="${userName}" maxLength="${maxUserNameLength}"/>
                    <br/>
                    ${nameRequirement}
                    <%--</jsp:attribute>--%>
                    <%--</tags:inputHint>--%>
                </table:tddata>
            </table:tredit>

            <table:tredit>
                <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_EMAIL%>" labelFor="${userEmailControl}" isMandatory="true"/>

                <table:tddata>
                    <%--<tags:inputHint inputId="${userEmailControl}" hintTitle_t="Email" hint="${emailRequirement}<br /><br />${mandatoryText}">--%>
                    <%--<jsp:attribute name="inputField">--%>
                    <html:input fieldId="${userEmailControl}" fieldValue="${userEmail}" size="40" maxLength="50"/>
                    <br/>
                    ${emailRequirement}
                    <%--</jsp:attribute>--%>
                    <%--</tags:inputHint>--%>
                </table:tddata>
            </table:tredit>

            <table:tredit>
                <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_BIRTHDAY%>" labelFor="${userDateOfBirthControl}"/>

                <table:tddata>
                    <%--<tags:inputHint inputId="${userDateOfBirthControl}" hintTitle_t="Birthday" hint="${birthdayRequirement}<br /><br />${mandatoryText}">--%>
                    <%--<jsp:attribute name="inputField">--%>
                    <tags:datePicker fieldName="${userDateOfBirthControl}" fieldValue="${userDateOfBirth}"/>
                    <br/>
                    ${birthdayRequirement}
                    <%--</jsp:attribute>--%>
                    <%--</tags:inputHint>--%>
                </table:tddata>
            </table:tredit>

            <table:tredit>
                <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_SITE%>" labelFor="${userHomeSiteControl}"/>

                <table:tddata>
                    <%--<tags:inputHint inputId="${userHomeSiteControl}" hintTitle_t="Personal site" hint="${homeSiteRequirement}<br /><br />${optionalText}">--%>
                    <%--<jsp:attribute name="inputField">--%>
                    <html:input fieldId="${userHomeSiteControl}" fieldValue="${userHomeSite}" size="40" maxLength="255"/>
                    <br/>
                    ${homeSiteRequirement}
                    <%--</jsp:attribute>--%>
                    <%--</tags:inputHint>--%>
                </table:tddata>
            </table:tredit>

            <table:tredit>
                <%--<table:tdtext text_t="Self description" labelFor="${selfDescriptionControl}" />--%>

                <table:td colspan="2">
                    ${eco:translate('User data edit: Any information you wouls like to show about yourself')}
                    <br/>
                    <html:textarea inputId="${selfDescriptionControl}" inputValue="${selfDescription}" cols="80" rows="5"
                                   hint="${eco:translate('User card: Member self description')}"
                                   title="${eco:translate('User card: Member self description')}"/>
                </table:td>
            </table:tredit>

            <table:separatorInfo colspan="2" height="${separatorHeight}" title="${eco:translate('User edit data tab: UI settings')}"/>

            <table:tredit>
                <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_UI_LANGUAGE%>"/>
                <table:tddata>
                    <form:radiobuttons path="userEditDataModel.userUILanguageId" items="${userEditDataModel.usedLanguageTranslatableList.entries}"
                                       itemValue="id" itemLabel="name" htmlEscape="false" delimiter="<br />"/>
                </table:tddata>
            </table:tredit>

            <%--Photos on page--%>
            <table:tredit>
                <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_PHOTOS_ON_PAGE%>" labelFor="${photosOnPageControl}"/>

                <table:tddata>
                    <form:radiobutton path="${photosOnPageControl}" id="photosInuserGenderLine"
                                      label="${eco:translate('User data edit: photos on page - auto')}" value="-1" htmlEscape="false"
                                      delimiter="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                    <form:radiobuttons items="${photosOnPageValues}" path="${photosOnPageControl}" htmlEscape="false" delimiter="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                </table:tddata>

            </table:tredit>
            <%-- / Photos on page--%>

            <table:tr>
                <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_SHOW_NUDE_CONTENT%>"/>
                <table:tddata><form:checkbox path="userEditDataModel.${showNudeContentControl}" value="true"/></table:tddata>
            </table:tr>

            <table:separatorInfo colspan="2" height="${separatorHeight}" title="${eco:translate('User edit data tab: Email notification options')}"/>

            <%-- notification Message About New Photos Of Favorite Members --%>
            <table:tredit>
                <table:tdtext text_t="<%=UserEditDataValidator.USER_DATA_NOTIFICATION_EMAIL%>"/>

                <table:tddata>
                    <form:checkboxes path="userEditDataModel.${emailNotificationOptionIdsControl}" items="${emailNotificationOptionsValues}" itemValue="id"
                                     itemLabel="name" htmlEscape="false" delimiter="<br />"/>
                </table:tddata>

            </table:tredit>
            <%-- / notification Message About New Photos Of Favorite Members --%>

            <table:separatorInfo colspan="2" height="${separatorHeight}" title="${eco:translate('User edit data tab: Default values')}"/>

            <%-- default Photo Comments Allowance --%>
            <table:tredit>
                <table:tdtext text_t="User data edit: Comments allowance by default" labelFor="defaultPhotoCommentsAllowanceId1"/>

                <table:tddata>
                    <form:radiobuttons items="${accessibleCommentAllowances}" path="userEditDataModel.${defaultPhotoCommentsAllowanceIdControl}" itemValue="id"
                                       itemLabel="name" htmlEscape="false" delimiter="<br />"/>
                </table:tddata>

            </table:tredit>
            <%-- / default Photo Comments Allowance --%>

            <%-- default Photo Voting Allowance --%>
            <table:tredit>
                <table:tdtext text_t="User data edit: Photo appraisal allowance by default" labelFor="defaultPhotoVotingAllowanceId1"/>

                <table:tddata>
                    <form:radiobuttons items="${accessibleVotingAllowances}" path="userEditDataModel.${defaultPhotoVotingAllowanceIdControl}" itemValue="id"
                                       itemLabel="name" htmlEscape="false" delimiter="<br />"/>
                </table:tddata>

            </table:tredit>
            <%-- / default Photo Voting Allowance --%>

            <table:trok text_t="${isNew ? 'User data edit: Register' : 'User data edit: Save'}"/>

        </table:table>

    </eco:form>

    <tags:springErrorHighliting bindingResult="${userEditDataModel.bindingResult}"/>

    <div class="footerseparator"></div>

</tags:page>
