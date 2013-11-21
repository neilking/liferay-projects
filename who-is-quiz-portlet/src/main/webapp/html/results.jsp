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
<%@ include file="/html/init.jsp" %>

<%
final Question[] questions = (Question[])portletSession.getAttribute("questions");

final User[] answers = (User[])portletSession.getAttribute("answers");

int correctAnswers = 0;

for( int i = 0; i < questions.length; i++)
{
	if( questions[i].getUser().equals(answers[i]) )
	{
		correctAnswers++;
	}
}

final double percent = correctAnswers / (double)questions.length;
final String score = new DecimalFormat("#%").format(percent);
%>

<portlet:renderURL var="startURL">
	<portlet:param name="mvcPath" value="/view.jsp" />
</portlet:renderURL>

<p>
    Thanks for participating in the Liferay 'Who is' quiz.  Below are results:
</p>

<aui:fieldset>
    <aui:input name="numQuestions" label="Number of questions" value="<%=questions.length %>" disabled="true" />
    <aui:input name="correct" label="Correct answers" value="<%=correctAnswers %>" disabled="true" />
    <aui:input name="percent" label="Score" value="<%=score %>" disabled="true" />
</aui:fieldset>

<aui:form action="<%= startURL %>" method="get" name="quiz" >
	<aui:button-row>
        <aui:button type="submit" value="Take another quiz" />
    </aui:button-row>
</aui:form>
