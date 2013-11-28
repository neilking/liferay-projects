
package com.liferay.projects.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
//import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class QuizPortlet
 */
public class QuizPortlet extends MVCPortlet {

	public void startQuiz( ActionRequest request, ActionResponse response)
			throws PortalException, SystemException {
//		final String quizFilter = ParamUtil.getString( request, "quizFilter" );
		int numQuestions = ParamUtil.getInteger(request, "numQuestions");

		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(
				WebKeys.THEME_DISPLAY);
		long companyId = themeDisplay.getCompanyId();

		Organization dalianOrg = OrganizationLocalServiceUtil.getOrganization(
				companyId, "Liferay Dalian");

		List<User> orgUsers = UserLocalServiceUtil.getOrganizationUsers(
				dalianOrg.getOrganizationId());

		List<User> pictureUsers = removeUsersWithoutProfilePicture(
				orgUsers, themeDisplay);

		List<User> users = Collections.unmodifiableList(pictureUsers);

		List<User> quizUsers = pickAtRandom(users, numQuestions);

		Question[] questions = generateQuestions(
				quizUsers, users, numQuestions);

		User[] answers = new User[questions.length];

		PortletSession portletSession = request.getPortletSession(true);
		portletSession.setAttribute("questions", questions);
		portletSession.setAttribute("answers", answers);

		response.setRenderParameter("numQuestions", StringUtil.valueOf(numQuestions));
		response.setRenderParameter("questionIndex", "0");
		response.setRenderParameter("jspPage", "/html/question.jsp");

	}

	public void continueQuiz( ActionRequest request, ActionResponse response )
			throws PortalException, SystemException {

		PortletSession portletSession = request.getPortletSession(true);
		Question[] questions = (Question[]) portletSession.getAttribute("questions");

		User[] answers = (User[]) portletSession.getAttribute("answers");

		String numQuestions = ParamUtil.getString(request, "numQuestions");

		int questionIndex = ParamUtil.getInteger(request, "questionIndex");

		long choiceUserId = ParamUtil.getLong(request, "choice");

		final User choiceUser = UserLocalServiceUtil.getUser(choiceUserId);
		answers[questionIndex] = choiceUser;

		// last page? 
		if ((questions.length - 1) == questionIndex) {
			response.setRenderParameter("jspPage", "/html/results.jsp");
		}
		else {
			response.setRenderParameter("numQuestions", numQuestions);
			response.setRenderParameter("questionIndex", StringUtil.valueOf(questionIndex + 1));
			response.setRenderParameter( "jspPage", "/html/question.jsp");
		}
	}

	private Question[] generateQuestions(
			List<User> quizUsers, List<User> poolUsers, int numQuestions )
					throws PortalException, SystemException {

		List<Question> retval = new ArrayList<Question>();

		int poolSize = poolUsers.size();

		for (User user : quizUsers) {
			Question question = new Question(user);

			// find 3 other users in the pool of users that are the same gender of question user
			for (int i = 0; i < 3; i++) {
				User choice = poolUsers.get(_rand.nextInt(poolSize));

				while (
						choice == user 
						|| (!isSameGender(choice, user))
						|| question.getChoices().contains(choice)) {

					choice = poolUsers.get(_rand.nextInt(poolSize));
				}

				question.addChoice(choice);
			}

			retval.add(question);
		}

		return retval.toArray(new Question[0]);
	}

	private boolean isSameGender(User choice, User user)
			throws PortalException, SystemException
	{
		return choice.getMale() == user.getMale();
	}

	private List<User> pickAtRandom(List<User> users, int numOfPicks) {
		List<User> retval = new ArrayList<User>();

		int size = users.size();

		for (int i = 0; i < numOfPicks; i++) {
			User nextUser = users.get(_rand.nextInt( size ));

			//while( retval.contains( nextUser ) )
			//{
				//nextUser = users.get( _rand.nextInt( size ) );
			//}

			retval.add(nextUser);
		}

		return Collections.unmodifiableList(retval);
	}

	private List<User> removeUsersWithoutProfilePicture(
			List<User> users, ThemeDisplay themeDisplay ) {
		List<User> retval = new ArrayList<User>();

		for (User user : users) {
			long portraitId = user.getPortraitId();

			if (portraitId == 0) {
				continue;
			}

			retval.add(user);
		}

		return retval;
	}

	private final Random _rand = new Random();
}
