package br.com.ciscience.controll.rest.service.impl;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;

import br.com.ciscience.controll.rest.service.MyHttpSessionManager;
import br.com.ciscience.model.dao.impl.UserDAO;
import br.com.ciscience.model.entity.impl.Administrator;
import br.com.ciscience.model.entity.impl.User;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;

@Path("/login")
public class LoginRestService {

	private UserDAO userDao;
	private JPAUtil jpaUtil;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Path("/get-user-loged")
	@PermitAll
	public Response getUserLoged() {

		// Captura a session do servletRequest do contexto de request atual
		HttpSession session = servletRequest.getSession();

		// Captura o usuario presente na session em contexto
		User userLoged = MyHttpSessionManager.getInstance().getSessionUserLogged(session);

		ResponseBuilder rb = Response.noContent();

		/*
		 * Se não existir nenhum usuário na session, abortar todas as requests
		 */
		if (userLoged == null) {
			System.out.println("usuario loged null");
			rb.status(Response.Status.UNAUTHORIZED);
			rb.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN, Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);

		} else if (userLoged.getStatus() == false) {
			System.out.println("usuario null");
			rb.status(Response.Status.UNAUTHORIZED);
			rb.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN, Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);

		} else {

			User userToJson = new User();
			System.out.println("nome----------------->"+userLoged.getName());
			userToJson.setName(userLoged.getName());
			userToJson.setEmail(userLoged.getEmail());
			userToJson.setProfile(userLoged.getClass().getSimpleName().toString());

			String json = new Gson().toJson(userToJson);

			rb.status(Response.Status.ACCEPTED).entity(json);
			rb.type(MediaType.APPLICATION_JSON);
			rb.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN, Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);

		}

		return rb.build();

	}

	@POST
	@Path("/logout")
	@PermitAll
	public Response logOut() {

		// Captura a session do servletRequest do contexto de request atual
		HttpSession httpSession = servletRequest.getSession();

		// Destroi a session
		MyHttpSessionManager.getInstance().destoySessionUserLogged(httpSession);

		// Response
		ResponseBuilder rb = Response.noContent();
		rb.status(Response.Status.OK);
		rb.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN, Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);

		return rb.build();

	}

	@POST
	@Path("/administrator")
	@PermitAll
	public Response onPostRequestAdministrador(@FormParam("email") String email,
			@FormParam("password") String password) {

		System.out.println("email -> " + email);
		System.out.println("password -> " + password);

		Administrator administrator = new Administrator();
		administrator.setEmail(email);
		administrator.setPassword(password);

		// Verificar existencia
		this.jpaUtil = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.userDao = new UserDAO(this.jpaUtil.getEntityManager());
		ResponseBuilder rb = Response.noContent();

		HttpSession session = servletRequest.getSession();

		try {

			this.jpaUtil.beginTransaction();

			List<User> resultList = this.userDao.getByEmailAndPassword(administrator);

			if (resultList.isEmpty()) {
				MyHttpSessionManager.getInstance().destoySessionUserLogged(session);
				rb = rb.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN, Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);
				rb = rb.status(Response.Status.UNAUTHORIZED);
			} else {
				User userLoged = resultList.get(0);
				userLoged.setPassword(null);
				MyHttpSessionManager.getInstance().setSessionUserLogged(session, userLoged);
				rb = rb.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN, Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);
				rb = rb.status(Response.Status.OK);
			}

		} catch (Exception e) {

			this.jpaUtil.rollBack();
			MyHttpSessionManager.getInstance().destoySessionUserLogged(session);

			rb = rb.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN, Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);
			rb = rb.type(MediaType.TEXT_PLAIN);
			rb = rb.status(Response.Status.INTERNAL_SERVER_ERROR);

		} finally {
			this.jpaUtil.close();
		}

		return rb.build();

	}
}
