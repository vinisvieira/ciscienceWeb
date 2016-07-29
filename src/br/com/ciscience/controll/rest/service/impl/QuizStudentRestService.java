package br.com.ciscience.controll.rest.service.impl;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;

import br.com.ciscience.model.dao.impl.QuizDAO;
import br.com.ciscience.model.dao.impl.QuizStudentDAO;
import br.com.ciscience.model.dao.impl.StudentDAO;
import br.com.ciscience.model.entity.impl.Quiz;
import br.com.ciscience.model.entity.impl.QuizStudent;
import br.com.ciscience.model.entity.impl.Student;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.ResponseBuilderGenerator;

@Path("/quizstudent")
public class QuizStudentRestService {

	private QuizStudentDAO quizStudentDAO;
	private JPAUtil simpleEntityManager;
	private StudentDAO studentDAO;
	private QuizDAO quizDAO;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@RolesAllowed({ "Administrator" })
	public Response list() {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.quizStudentDAO = new QuizStudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();

			List<QuizStudent> quizStudents = this.quizStudentDAO.findAll();

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(quizStudents));

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
	@RolesAllowed({ "Administrator" })
	public Response getByID(@PathParam("id") String id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.quizStudentDAO = new QuizStudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(this.quizStudentDAO.getById(Long.parseLong(id))));

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
	@Path("/student/{id}")
	@RolesAllowed({ "Administrator" })
	public Response getByStudent(@PathParam("id") long id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.quizStudentDAO = new QuizStudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();
			Student student = studentDAO.getById(id);

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(this.quizStudentDAO.getByStudent(student)));

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
	public Response create(@FormParam("quizStudent") String quizStudentJSON, @HeaderParam("token") String token) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.quizStudentDAO = new QuizStudentDAO(this.simpleEntityManager.getEntityManager());
		this.quizDAO = new QuizDAO(this.simpleEntityManager.getEntityManager());
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();
		QuizStudent quizStudent = new Gson().fromJson(quizStudentJSON, QuizStudent.class);
		Student student = studentDAO.getByToken(token);
		Quiz quiz = quizDAO.getById(quizStudent.getQuiz().getId());

		try {

			if (student != null) {

				if (quiz != null) {
					quizStudent.setStudent(student);
					if (quizStudent.validateEmptyFields()) {
						quizStudent.getStudent().setScore((quizStudent.getStudent().getScore() + quizStudent.getTotalScore()));
						this.quizStudentDAO.save(quizStudent);
						this.simpleEntityManager.commit();

						responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
					} else {
						System.out.println("Erro na validação dos campos");
						responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
					}

				} else {
					System.out.println("Quiz inexistente");
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
				}

			} else {
				System.out.println("Aluno inexistente");
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
