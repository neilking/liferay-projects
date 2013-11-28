package com.liferay.projects.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.liferay.portal.model.User;

public class Question {

	private User user;
	private List<User> choices;

	public Question(User user) {
		this.user = user;
		this.choices = new ArrayList<User>();
		this.choices.add(user);
	}

	public void addChoice(User choice) {
		this.choices.add(choice);
	}

	public User getUser() {
		return user;
	}

	public List<User> getChoices() {
		long seed = System.nanoTime();
		Collections.shuffle(this.choices, new Random(seed));
		
		return this.choices;
	}

}
