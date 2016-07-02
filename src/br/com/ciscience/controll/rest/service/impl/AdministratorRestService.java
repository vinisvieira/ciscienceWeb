package br.com.ciscience.controll.rest.service.impl;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.com.ciscience.model.dao.impl.AdministratorDAO;
import br.com.ciscience.model.entity.impl.Administrador;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.MyDateGenerator;
import br.com.ciscience.util.ResponseBuilderGenerator;

@Path("/admin")
public class AdministratorRestService {

	private AdministratorDAO administratorDAO;
	private JPAUtil simpleEntityManager;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@RolesAllowed({ "Administrator" })
	public Response list() {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.administratorDAO = new AdministratorDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();

			List<Administrador> administrators = this.administratorDAO.findAll();

			for (int i = 0; i < administrators.size(); i++) {
				if (administrators.get(i).getStatus()) {
					administrators.get(i).setPassword(null);
				}
			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(administrators));

		} catch (Exception e) {
			// Caso haja uma exceção, desfaz qualquer transação realizada com o
			// banco de dados...
			this.simpleEntityManager.rollBack();
			// Imprime no console o stack da exceção...
			e.printStackTrace();
			// gera um response de erro interno
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
			// Método close é chamado obrigatoriamente no final do tratamento
			// try/catch
			this.simpleEntityManager.close();
		}

		return responseBuilder.build();

	}

	@GET
	@Path("/{id}")
	@RolesAllowed({ "Administrador" })
	public Response getByID(@PathParam("id") String id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.administratorDAO = new AdministratorDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(this.administratorDAO.getById(Long.parseLong(id))));

		} catch (Exception e) {
			// Caso haja uma exceção, desfaz qualquer transação realizada com o
			// banco de dados...
			this.simpleEntityManager.rollBack();
			// Imprime no console o stack da exceção...
			e.printStackTrace();
			// gera um response de erro interno
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
			// Método close é chamado obrigatoriamente no final do tratamento
			// try/catch
			this.simpleEntityManager.close();
		}
		return responseBuilder.build();
	}

	@POST
	@RolesAllowed({ "Administrator" })
	public Response create(@FormParam("name") String name, @FormParam("email") String email,
			@FormParam("password") String password, @FormParam("confirmPassword") String confirmPassword) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.administratorDAO = new AdministratorDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Administrador admin = new Administrador();
			admin.setEmail(email);
			admin.setPassword(password);
			admin.setUserSince(MyDateGenerator.getCurrentDate());
			admin.setStatus(Constants.ACTIVE_ENTITY);

			if (!this.administratorDAO.emailExists(admin)) {

				if (admin.validateEmptyFields() && admin.passwordsMatch(admin.getPassword(), confirmPassword)) {
					this.administratorDAO.save(admin);
					this.simpleEntityManager.commit();

					responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
				} else {
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
				}

			} else {
				responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
			}

		} catch (Exception e) {
			this.simpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
			this.simpleEntityManager.close();
		}

		return responseBuilder.build();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") Long id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.administratorDAO = new AdministratorDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Administrador administrator = this.administratorDAO.getById(id);

			if (administrator != null) {

				administrator.setStatus(administrator.getStatus() == false);
				this.simpleEntityManager.commit();

				responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
			} else {
				responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
			}

		} catch (Exception e) {
			this.simpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
			this.simpleEntityManager.close();
		}

		return responseBuilder.build();

	}

	@PUT
	@Path("/{id}")
	public Response update(@PathParam("id") Long id, @FormParam("name") String name, @FormParam("email") String email) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.administratorDAO = new AdministratorDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Administrador administrator = this.administratorDAO.getById(id);

			if (administrator != null) {
				administrator.setEmail(email);
				administrator.setName(name);

				if (administrator.validateEmptyFields()) {
					this.administratorDAO.update(administrator);
					this.simpleEntityManager.commit();

					responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);

				} else {
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
				}
			} else {
				responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
			}

		} catch (Exception e) {
			this.simpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
			this.simpleEntityManager.close();
		}

		return responseBuilder.build();

	}

}
