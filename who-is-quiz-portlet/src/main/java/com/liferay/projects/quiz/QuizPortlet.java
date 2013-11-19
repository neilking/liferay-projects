package com.liferay.projects.quiz;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * Portlet implementation class QuizPortlet
 */
public class QuizPortlet extends MVCPortlet {
 
	public void startQuiz(ActionRequest request, ActionResponse response) {
		//String redirect = ParamUtil.getString(request, "redirect");
		String quizFilter = ParamUtil.getString(request, "quizFilter");
		String numQuestions = ParamUtil.getString(request, "numQuestions");
		System.out.println(numQuestions);
		
		//your logic
		
		
		
		response.setRenderParameter("quizFilter", quizFilter);
		response.setRenderParameter("numQuestions", numQuestions);
		response.setRenderParameter("jspPage", "/question.jsp");
	}
}
