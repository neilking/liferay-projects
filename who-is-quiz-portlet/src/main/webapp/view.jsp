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

<%@ include file="/init.jsp" %>

<portlet:actionURL name="startQuiz" var="startURL" />

<p>
    Welcome to the Liferay 'Who is' quiz.  This will test to see how well you know 
    your co-workers. Please select your options to begin your quiz.
</p>

<aui:form action="<%= startURL %>" method="post" name="quiz" >
	<aui:fieldset>
	    <aui:select label="Quick Filter" name="quizFilter">
	        <aui:option label="All employees" selected="true" value="all" />
	        <aui:option label="Liferay Dalian" value="dalian" />
	        <aui:option label="Liferay US" value="us" />
	        <aui:option label="New hires" value="newhires" />
	        <aui:option label="Interns" value="interns" />
	    </aui:select>
	    <aui:input label="Number of Questions" name="numQuestions" value="20" />
	</aui:fieldset>
	<aui:button-row>
        <aui:button type="submit" value="Start Quiz" />
    </aui:button-row>
</aui:form>