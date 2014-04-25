<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ui.controllers.users.edit.UserEditDataModel" %>
<%@ page import="core.general.user.UserMembershipType" %>
<%@ page import="core.services.utils.UrlUtilsServiceImpl" %>
<%@ page import="core.enums.UserGender" %>
<%@ page import="core.enums.YesNo" %>
<%@ page import="core.general.user.EmailNotificationType" %>
<%@ page import="ui.services.validation.UserRequirement" %>
<%@ page import="ui.services.validation.DataRequirementService" %>
<%@ page import="ui.translatable.GenericTranslatableList" %>
<%@ page import="ui.context.EnvironmentContext" %>
<%@ page import="ui.context.ApplicationContextHelper" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="userEditDataModel" type="ui.controllers.users.edit.UserEditDataModel" scope="request" />

<%
	final Integer[] photosInLineValues = { 1, 2, 3, 4, 5 };
	final Integer[] photoLinesValues = { 3, 4, 5, 6, 7 };
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

<c:set var="photosInLineControl" value="userEditDataModel.photosInLine"/>
<c:set var="photosInLineValues" value="<%=photosInLineValues%>"/>
<c:set var="photoLinesValues" value="<%=photoLinesValues%>"/>
<c:set var="photoLinesControl" value="userEditDataModel.photoLines"/>

<c:set var="membershipTypeListValues" value="<%=GenericTranslatableList.userMembershipTypeTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>
<c:set var="userGenderValues" value="<%=GenericTranslatableList.userGenderTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>
<c:set var="emailNotificationOptionsValues" value="<%=GenericTranslatableList.emailNotificationTypeTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>

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

	<tags:inputHintForm />

	<eco:form action="${eco:baseUrl()}/${userPathPrefix}/${userId}/save/">

		<input type="hidden" name="${userIdControl}" id="${userIdControl}" value="${userId}" />

		<table:table width="800">

			<table:separatorInfo colspan="2" height="${separatorHeight}" title="${eco:translate('Login information')}" />

			<table:tredit>
				<table:tdtext text_t="User data edit: Login" labelFor="${userLoginControl}" isMandatory="true" />

				<table:tddata>
					<tags:inputHint inputId="${userLoginControl}" hintTitle_t="Login" hint="${loginRequirement}<br /><br />${mandatoryText}" focused="true">
						<jsp:attribute name="inputField">
							<html:input fieldId="${userLoginControl}" fieldValue="${userLogin}" maxLength="${maxUserNameLength}" />
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<c:if test="${isNew}">
				<table:tredit>
					<table:tdtext text_t="User data edit: Password" labelFor="${userPasswordControl}" isMandatory="true" />

					<table:tddata>
						<tags:inputHint inputId="${userPasswordControl}" hintTitle_t="Password" hint="${passwordRequirement}<br /><br />${mandatoryText}">
							<jsp:attribute name="inputField">
								<html:password fieldId="${userPasswordControl}" fieldValue="${password}" />
							</jsp:attribute>
						</tags:inputHint>
					</table:tddata>
				</table:tredit>

				<table:tredit>
					<table:tdtext text_t="User data edit: Confirm password" labelFor="${userConfirmPasswordControl}" isMandatory="true" />

					<table:tddata>
						<tags:inputHint inputId="${userConfirmPasswordControl}" hintTitle_t="Confirm Password" hint="${confirmPasswordRequirement}<br /><br />${mandatoryText}">
							<jsp:attribute name="inputField">
								<html:password fieldId="${userConfirmPasswordControl}" fieldValue="${confirmPassword}" />
							</jsp:attribute>
						</tags:inputHint>
					</table:tddata>
				</table:tredit>
			</c:if>

			<table:separatorInfo colspan="2" height="${separatorHeight}" title="${eco:translate('Personal information')}" />

			<table:tredit>
				<table:tdtext text_t="User data edit: Gender" labelFor="${userGenderId1}" />

				<table:tddata>
					<form:radiobuttons path="${userGenderControl}" items="${userGenderValues}" itemValue="id" itemLabel="name" delimiter="<br/>" htmlEscape="false" />
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<table:tdtext text_t="User data edit: Name" labelFor="${filterUserNameControl}" isMandatory="true" />

				<table:tddata>
					<tags:inputHint inputId="${filterUserNameControl}" hintTitle_t="Name" hint="${nameRequirement}<br /><br />${mandatoryText}">
						<jsp:attribute name="inputField">
							<html:input fieldId="${filterUserNameControl}" fieldValue="${userName}" maxLength="${maxUserNameLength}" />
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<table:tdtext text_t="Email" labelFor="${userEmailControl}" isMandatory="true" />

				<table:tddata>
					<tags:inputHint inputId="${userEmailControl}" hintTitle_t="Email" hint="${emailRequirement}<br /><br />${mandatoryText}">
						<jsp:attribute name="inputField">
							<html:input fieldId="${userEmailControl}" fieldValue="${userEmail}" size="40" maxLength="50" />
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<table:tdtext text_t="User data edit: Birthday" labelFor="${userDateOfBirthControl}" />

				<table:tddata>
					<tags:inputHint inputId="${userDateOfBirthControl}" hintTitle_t="Birthday" hint="${birthdayRequirement}<br /><br />${mandatoryText}">
						<jsp:attribute name="inputField">
							<tags:datePicker fieldName="${userDateOfBirthControl}" fieldValue="${userDateOfBirth}"/>
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<table:tdtext text_t="User data edit: Home site" labelFor="${userHomeSiteControl}" />

				<table:tddata>
					<tags:inputHint inputId="${userHomeSiteControl}" hintTitle_t="Personal site" hint="${homeSiteRequirement}<br /><br />${optionalText}">
						<jsp:attribute name="inputField">
							<html:input fieldId="${userHomeSiteControl}" fieldValue="${userHomeSite}" size="40" maxLength="255" />
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<%--<table:tdtext text_t="Self description" labelFor="${selfDescriptionControl}" />--%>

				<table:td colspan="2">
					${eco:translate('User data edit: Any information you wouls like to show about yourself')}
					<br />
					<html:textarea inputId="${selfDescriptionControl}" inputValue="${selfDescription}" cols="80" rows="5" hint="${eco:translate('Self description')}" title="${eco:translate('Self description')}" />
				</table:td>
			</table:tredit>

			<table:separatorInfo colspan="2" height="${separatorHeight}" title="${eco:translate('Account information')}" />

			<table:tredit>
				<table:tdtext text_t="User data edit: Membership type" labelFor="${membershipTypeId1}" />

				<table:tddata>
					<form:radiobuttons items="${membershipTypeListValues}" path="userEditDataModel.${membershipTypeControl}"
									 itemLabel="name" itemValue="id" delimiter="<br/>" htmlEscape="false" />
				</table:tddata>
			</table:tredit>

			<table:separatorInfo colspan="2" height="${separatorHeight}" title="UI settings" />

			<table:tredit>
				<table:tdtext text_t="User data edit: Language" labelFor="${photoLinesControl}" />
				<table:tddata>
					<form:radiobuttons path="userEditDataModel.userUILanguageId" items="${userEditDataModel.usedLanguageTranslatableList.entries}" itemValue="id" itemLabel="name" htmlEscape="false" delimiter="<br />"/>
				</table:tddata>
			</table:tredit>

			<%--Photos in line--%>
			<table:tredit>
				<table:tdtext text_t="User data edit: Photos in line" labelFor="${photosInLineControl}" />

				<table:tddata>
					<form:radiobutton path="${photosInLineControl}" id="photosInuserGenderLine" label="${eco:translate('auto')}" value="-1" htmlEscape="false" delimiter="&nbsp;&nbsp;&nbsp;&nbsp;"/>
					<form:radiobuttons items="${photosInLineValues}" path="${photosInLineControl}" htmlEscape="false" delimiter="&nbsp;&nbsp;&nbsp;&nbsp;"/>
				</table:tddata>

			</table:tredit>
			<%-- / Photos in line--%>

			<%--Photo Lines--%>
			<table:tredit>
				<table:tdtext text_t="User data edit: Photos lines" labelFor="${photoLinesControl}" />

				<table:tddata>
					<form:radiobuttons items="${photoLinesValues}" path="${photoLinesControl}" htmlEscape="false" delimiter="&nbsp;&nbsp;&nbsp;&nbsp;"/>
				</table:tddata>

			</table:tredit>
			<%-- / Photos Lines--%>

			<table:tr>
				<table:tdtext text_t="User data edit: Show nude content" />
				<table:tddata><form:checkbox path="userEditDataModel.${showNudeContentControl}" value="true"/></table:tddata>
			</table:tr>

			<table:separatorInfo colspan="2" height="${separatorHeight}" title="${eco:translate('Email notification options')}" />

			<%-- notification Message About New Photos Of Favorite Members --%>
			<table:tredit>
				<table:tdtext text_t="User data edit: Send notification email about" />

				<table:tddata>
					<form:checkboxes path="userEditDataModel.${emailNotificationOptionIdsControl}" items="${emailNotificationOptionsValues}" itemValue="id" itemLabel="name" htmlEscape="false" delimiter="<br />" />
				</table:tddata>

			</table:tredit>
			<%-- / notification Message About New Photos Of Favorite Members --%>

			<table:separatorInfo colspan="2" height="${separatorHeight}" title="${eco:translate('Default values')}" />

			<%-- default Photo Comments Allowance --%>
			<table:tredit>
				<table:tdtext text_t="User data edit: Comments allowance by default" labelFor="defaultPhotoCommentsAllowanceId1" />

				<table:tddata>
					<form:radiobuttons items="${accessibleCommentAllowances}" path="userEditDataModel.${defaultPhotoCommentsAllowanceIdControl}" itemValue="id" itemLabel="name" htmlEscape="false" delimiter="<br />"/>
				</table:tddata>

			</table:tredit>
			<%-- / default Photo Comments Allowance --%>

			<%-- default Photo Voting Allowance --%>
			<table:tredit>
				<table:tdtext text_t="User data edit: Photo appraisal allowance by default" labelFor="defaultPhotoVotingAllowanceId1" />

				<table:tddata>
					<form:radiobuttons items="${accessibleVotingAllowances}" path="userEditDataModel.${defaultPhotoVotingAllowanceIdControl}" itemValue="id" itemLabel="name" htmlEscape="false" delimiter="<br />"/>
				</table:tddata>

			</table:tredit>
			<%-- / default Photo Voting Allowance --%>

			<table:trok text_t="${isNew ? 'User data edit: Register' : 'User data edit: Save'}" />

		</table:table>

	</eco:form>

	<tags:springErrorHighliting bindingResult="${userEditDataModel.bindingResult}"/>

	<div class="footerseparator"></div>

</tags:page>
