
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

	public void startQuiz( ActionRequest request, ActionResponse response )
			throws PortalException, SystemException {
//		final String quizFilter = ParamUtil.getString( request, "quizFilter" );
		final int numQuestions = ParamUtil.getInteger( request, "numQuestions" );

		final ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute( WebKeys.THEME_DISPLAY );
		final long companyId = themeDisplay.getCompanyId();

		//try
		//{
			final Organization dalianOrg = OrganizationLocalServiceUtil.getOrganization( companyId, "Liferay Dalian" );

			final List<User> orgUsers = UserLocalServiceUtil.getOrganizationUsers( dalianOrg.getOrganizationId() );

			final List<User> pictureUsers = removeUsersWithoutProfilePicture( orgUsers, themeDisplay );

			final List<User> users = Collections.unmodifiableList( pictureUsers );

			final List<User> quizUsers = pickAtRandom( users, numQuestions );

			final Question[] questions = generateQuestions(quizUsers, users, numQuestions);

			final User[] answers = new User[questions.length];

			PortletSession portletSession = request.getPortletSession(true);
			portletSession.setAttribute("questions", questions);
			portletSession.setAttribute("answers", answers);
			//SessionMessages.add(portletSession, "questions", questions);
			//SessionMessages.add(portletSession, "answers", answers);
			//HttpServletRequest httpRequest = PortalUtil.getHttpServletRequest( request );
			//final HttpSession session = httpRequest.getSession();
			//session.setAttribute( "questions", questions );
			//session.setAttribute( "answers", answers );

			response.setRenderParameter( "numQuestions", StringUtil.valueOf(numQuestions) );
			response.setRenderParameter( "questionIndex", "0" );
			response.setRenderParameter( "jspPage", "/html/question.jsp" );
		//}
		//catch( Exception e )
		//{
		//}
	}

	public void continueQuiz( ActionRequest request, ActionResponse response )
			throws PortalException, SystemException {
		//final HttpServletRequest httpRequest = PortalUtil.getHttpServletRequest( request );
		//final HttpSession session = httpRequest.getSession();

		PortletSession portletSession = request.getPortletSession(true);
		final Question[] questions = (Question[]) portletSession.getAttribute("questions");

		final User[] answers = (User[]) portletSession.getAttribute("answers");

		final String numQuestions = ParamUtil.getString(request, "numQuestions");

		final int questionIndex = ParamUtil.getInteger(request, "questionIndex");

		final long choiceUserId = ParamUtil.getLong(request, "choice");

//		try
//		{
			final User choiceUser = UserLocalServiceUtil.getUser( choiceUserId );
			answers[questionIndex] = choiceUser;
			
			// last page? 
			if( ( questions.length - 1 ) == questionIndex ) {
				response.setRenderParameter( "jspPage", "/html/results.jsp");
			}
			else {
				response.setRenderParameter( "numQuestions", numQuestions);
				response.setRenderParameter( "questionIndex", ( questionIndex + 1 ) + "");
				response.setRenderParameter( "jspPage", "/html/question.jsp");
			}
//		}
//		catch( Exception e )
//		{
//		}
	}

	private Question[] generateQuestions( List<User> quizUsers, List<User> poolUsers, int numQuestions )
	{
		List<Question> retval = new ArrayList<Question>();

		final int poolSize = poolUsers.size();

		for( User user : quizUsers )
		{
			Question q = new Question( user );

			// find 3 other users in the pool of users that are the same gender of question user
			for( int i = 0; i < 3; i++ )
			{
				User choice = poolUsers.get( _rand.nextInt( poolSize ) );

				while( choice == user || ( !isSameGender( choice, user ) ) || q.getChoices().contains( choice ) )
				{
					choice = poolUsers.get( _rand.nextInt( poolSize ) );
				}

				q.addChoice( choice );
			}

			retval.add( q );
		}

		return retval.toArray( new Question[0] );
	}

	private boolean isSameGender( User choice, User user )
	{
		try
		{
			return choice.getMale() == user.getMale();
		}
		catch( Exception e )
		{
		}
		return false;
	}

	private List<User> pickAtRandom( List<User> users, int numOfPicks )
	{
		final List<User> retval = new ArrayList<User>();

		final int size = users.size();

		for( int i = 0; i < numOfPicks; i++ )
		{
			User nextUser = users.get( _rand.nextInt( size ) );

			//while( retval.contains( nextUser ) )
			//{
				//nextUser = users.get( _rand.nextInt( size ) );
			//}

			retval.add( nextUser );
		}

		return Collections.unmodifiableList( retval );
	}

	private List<User> removeUsersWithoutProfilePicture( List<User> users, ThemeDisplay themeDisplay )
	{
		List<User> retval = new ArrayList<User>();

		for( User user : users )
		{
			try
			{
				String portrait = user.getPortraitURL( themeDisplay );

				if( portrait.contains( "img_id=0" ) )
				{
					continue;
				}
			}
			catch( Exception e )
			{
			}

			retval.add( user );
		}

		return retval;
	}

	private final Random _rand = new Random();
}
