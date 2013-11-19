<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.projects.quiz.Question"%>
<%@ include file="/init.jsp" %>

<%
final String numQuestions = ParamUtil.getString(request, "numQuestions");
final String questionIndexParam = ParamUtil.getString( request, "questionIndex");
		 
final Question[] questions = (Question[])session.getAttribute("questions");

final int index = Integer.parseInt( questionIndexParam );
final Question currentQuestion = questions[index];

final User currentUser = currentQuestion.getUser();
final String userImage = currentUser.getPortraitURL(themeDisplay);
final User[] choices = currentQuestion.getChoices().toArray(new User[0]);
%>

<portlet:actionURL name="continueQuiz" var="actionURL" />

<p>
Who is this? <img align="top" width="200" height="200" src="<%=userImage %>" />
</p>

<aui:form action="<%= actionURL %>" method="post" name="quiz" >
    <aui:input type="hidden" name="numQuestions" value="<%=numQuestions %>" />
    <aui:input type="hidden" name="questionIndex" value="<%=questionIndexParam %>" />
    <aui:input type="radio" name="choice" label="<%=choices[0].getFullName() %>" value="<%=choices[0].getUserId() %>" />
    <aui:input type="radio" name="choice" label="<%=choices[1].getFullName() %>" value="<%=choices[1].getUserId() %>" />
    <aui:input type="radio" name="choice" label="<%=choices[2].getFullName() %>" value="<%=choices[2].getUserId() %>" />
    <aui:input type="radio" name="choice" label="<%=choices[3].getFullName() %>" value="<%=choices[3].getUserId() %>" />
    <aui:spacer />
    <aui:fieldset>
        <aui:button type="submit" value="Next question" />
    </aui:fieldset>
</aui:form>

