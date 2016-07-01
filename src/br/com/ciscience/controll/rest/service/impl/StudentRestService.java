package br.com.ciscience.controll.rest.service.impl;

import java.util.ArrayList;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.com.ciscience.model.dao.impl.StudentDAO;
import br.com.ciscience.model.entity.impl.Quiz;
import br.com.ciscience.model.entity.impl.Student;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.MyDateGenerator;
import br.com.ciscience.util.ResponseBuilderGenerator;
import br.com.ciscience.util.StringUtil;

@Path("/student")
public class StudentRestService {

	private JPAUtil simpleEntityManager;
	private StudentDAO studentDAO;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Path("/active")
	@PermitAll
	public Response listActive() {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();

			List<Student> students = this.studentDAO.findAll();
			List<Student> studentsToJson = new ArrayList<>();

			for (int i = 0; i < students.size(); i++) {
				if (students.get(i).getStatus()) {
					students.get(i).setPassword(null);
					students.get(i).setQuiz(students.get(i).getQuiz());

					for (Quiz quiz : students.get(i).getQuiz()) {
						quiz.setQuestions(null);
					}

					studentsToJson.add(students.get(i));
				}
			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(studentsToJson));

		} catch (Exception e) {
			this.simpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
			this.simpleEntityManager.close();
		}

		return responseBuilder.build();

	}

	@GET
	@Path("/inactive")
	@PermitAll
	public Response listInactive() {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();

			List<Student> students = this.studentDAO.findAll();
			List<Student> studentsToJson = new ArrayList<>();

			for (int i = 0; i < students.size(); i++) {
				if (!students.get(i).getStatus()) {
					students.get(i).setPassword(null);
					studentsToJson.add(students.get(i));
				}
			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(studentsToJson));

		} catch (Exception e) {
			this.simpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
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
			this.simpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
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
			student.setPassword(StringUtil.SHA1(password));
			student.setScore(0L);
			student.setUserSince(MyDateGenerator.getCurrentDate());
			student.setStatus(Constants.ACTIVE_ENTITY);

			if (!this.studentDAO.emailExists(student)) {

				if (student.validateEmptyFields()
						&& student.passwordsMatch(student.getPassword(), StringUtil.SHA1(confirmPassword))
						&& StringUtil.validEmail(student.getEmail())) {
					this.studentDAO.save(student);
					this.simpleEntityManager.commit();

					responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
				} else {
					System.out.println("erro na validação dos campos");
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
				}

			} else {
				System.out.println("erro no estudante existe");
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
	public Response update(@PathParam("id") Long id, @FormParam("name") String name, @FormParam("email") String email,
			@FormParam("password") String password, @FormParam("confirmPassword") String confirmPassword) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();
		List<Student> studentList = this.studentDAO.findAll();
		this.simpleEntityManager.beginTransaction();

		try {

			Student student = this.studentDAO.getById(id);

			for (Student studentL : studentList) {
				if (studentL.getEmail().equals(email)) {
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
					System.out.println("nome Igual no  banco ----------------------------------------------------");
					return responseBuilder.build();
				}
			}

			if (student != null) {
				student.setEmail(email);
				student.setName(name);
				student.setPassword(StringUtil.SHA1(password));

				if (student.validateEmptyFields()
						&& student.passwordsMatch(student.getPassword(), StringUtil.SHA1(confirmPassword))) {

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
	
	@GET
	@Path("/by-email")
	@PermitAll
	public Response searchByName(@HeaderParam("student") String student) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {
			
			List<Student> students = this.studentDAO.listarPorEmail(student);

			if (students != null) {
				
				responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
						JSONUtil.objectToJSON(students));
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
