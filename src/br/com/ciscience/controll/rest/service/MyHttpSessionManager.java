package br.com.ciscience.controll.rest.service;

import javax.servlet.http.HttpSession;

import br.com.ciscience.model.entity.impl.User;

public class MyHttpSessionManager {

	private static MyHttpSessionManager instance;
	
	public static String SESSION_USER_LOGED = "userLoged";
	
	private MyHttpSessionManager() {
	}

	public static MyHttpSessionManager getInstance() {
		if (instance == null) {
			instance = new MyHttpSessionManager();
		}
		return instance;
	}

	public void setSessionUserLogged(HttpSession httpSession, User user) {
		this.destoySessionUserLogged(httpSession);
		httpSession.setAttribute(SESSION_USER_LOGED, user);
	}

	public User getSessionUserLogged(HttpSession httpSession) {
		return (User) httpSession.getAttribute(SESSION_USER_LOGED);
	}
	
	public void destoySessionUserLogged(HttpSession httpSession) {
		httpSession.removeAttribute(SESSION_USER_LOGED);
	}

}
