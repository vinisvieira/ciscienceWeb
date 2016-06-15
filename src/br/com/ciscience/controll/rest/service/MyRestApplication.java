package br.com.ciscience.controll.rest.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import br.com.ciscience.controll.rest.service.impl.AdministratorRestService;
<<<<<<< HEAD
import br.com.ciscience.controll.rest.service.impl.AlternativeRestService;
=======
import br.com.ciscience.controll.rest.service.impl.LevelRestService;
>>>>>>> 6e2c1677acf72d36eef760a99a27534d3e43c29f
import br.com.ciscience.controll.rest.service.impl.QuestionRestService;
import br.com.ciscience.controll.rest.service.impl.StudentRestService;

public class MyRestApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>();

	public MyRestApplication() {

		// Serviços REST
		singletons.add(new AdministratorRestService());
		singletons.add(new StudentRestService() );
		singletons.add(new QuestionRestService());
<<<<<<< HEAD
		singletons.add(new AlternativeRestService());
=======
		singletons.add(new LevelRestService());
>>>>>>> 6e2c1677acf72d36eef760a99a27534d3e43c29f
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}