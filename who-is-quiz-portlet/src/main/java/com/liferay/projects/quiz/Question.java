package com.liferay.projects.quiz;

import com.liferay.portal.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Question {

	public Question(User user) {
		_user = user;
		_choices = new ArrayList<User>();
		_choices.add(user);
	}

	public void addChoice(User choice) {
		_choices.add(choice);
	}

	public User getUser() {
		return _user;
	}

	public List<User> getChoices() {
		long seed = System.nanoTime();
		Collections.shuffle(_choices, new Random(seed));

		return _choices;
	}
	private User _user;
	private List<User> _choices;
}
