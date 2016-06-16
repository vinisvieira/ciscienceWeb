package br.com.ciscience.controll.rest.service.impl;

import java.util.List;

import javax.annotation.security.PermitAll;
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
import br.com.ciscience.model.dao.impl.StudentDAO;
import br.com.ciscience.model.entity.impl.Student;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.MyDateGenerator;
import br.com.ciscience.util.ResponseBuilderGenerator;

@Path("/student")
public class StudentRestService {

	private JPAUtil simpleEntityManager;
	private StudentDAO studentDAO;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@PermitAll
	public Response list() {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();

			List<Student> students = this.studentDAO.findAll();

			for (int i = 0; i < students.size(); i++) {
				if (students.get(i).getStatus()) {
					students.get(i).setPassword(null);
				}
			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(students));

		} catch (Exception e) {
			// Caso haja uma exceção, desfaz qualquer transação realizada
			// com o
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
	@PermitAll
	public Response getByID(@PathParam("id") String id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(this.studentDAO.getById(Long.parseLong(id))));

		} catch (Exception e) {
			// Caso haja uma exceção, desfaz qualquer transação realizada
			// com o
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
	@PermitAll
	public Response create(@FormParam("name") String name, @FormParam("email") String email,
			@FormParam("password") String password, @FormParam("confirmPassword") String confirmPassword) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {
			
			Student student = new Student();
			student.setName(name);
			student.setEmail(email);
			student.setPassword(password);
			student.setUserSince(MyDateGenerator.getCurrentDate());
			student.setStatus(Constants.ACTIVE_ENTITY);
			

			if (!this.studentDAO.emailExists(student)) {

				if (student.validateEmptyFields() && student.passwordsMatch(student.getPassword(), confirmPassword)) {
					this.studentDAO.save(student);
					this.simpleEntityManager.commit();

					responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
				} else {
					System.out.println("erro na validas�o dos campos");
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
				}

			} else {
				System.out.println("erro no estudande existe");
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
	@PermitAll
	public Response delete(@PathParam("id") Long id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Student student = this.studentDAO.getById(id);

			if (student != null) {

				student.setStatus(student.getStatus() == false);
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
	@PermitAll
	public Response update(@PathParam("id") Long id, @FormParam("name") String name, @FormParam("email") String email) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Student student = this.studentDAO.getById(id);

			if (student != null) {
				student.setEmail(email);
				student.setName(name);

				if (student.validateEmptyFields()) {
					this.studentDAO.update(student);
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