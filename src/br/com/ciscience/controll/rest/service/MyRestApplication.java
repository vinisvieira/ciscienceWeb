package br.com.ciscience.controll.rest.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import br.com.ciscience.controll.rest.service.impl.AdministratorRestService;
import br.com.ciscience.controll.rest.service.impl.AlternativeRestService;
import br.com.ciscience.controll.rest.service.impl.ContestRestService;
import br.com.ciscience.controll.rest.service.impl.DataFileRestService;
import br.com.ciscience.controll.rest.service.impl.LevelRestService;
import br.com.ciscience.controll.rest.service.impl.LoginRestService;
import br.com.ciscience.controll.rest.service.impl.QuestionRestService;
import br.com.ciscience.controll.rest.service.impl.QuizRestService;
import br.com.ciscience.controll.rest.service.impl.QuizStudentRestService;
import br.com.ciscience.controll.rest.service.impl.StudentRestService;

public class MyRestApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>();

	public MyRestApplication() {

		// Serviços REST
		singletons.addAll(Arrays.asList(new AdministratorRestService(), new StudentRestService(),
				new QuestionRestService(), new AlternativeRestService(), new LevelRestService(),
				new ContestRestService(), new QuizRestService(), new QuizStudentRestService(), new LoginRestService(),
				new DataFileRestService()));

	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}