package br.com.ciscience.controll.rest.service.impl;

import java.util.ArrayList;
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
import br.com.ciscience.model.entity.impl.Question;
import br.com.ciscience.model.entity.impl.Quiz;
import br.com.ciscience.model.entity.impl.QuizStudent;
import br.com.ciscience.model.entity.impl.Student;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.MyDateGenerator;
import br.com.ciscience.util.ResponseBuilderGenerator;

@Path("/quiz")
public class QuizRestService {

	private QuizDAO quizDAO;
	private JPAUtil simpleEntityManager;

	@Context
	private HttpServletRequest servletRequest;

	@POST
	@RolesAllowed({ "Administrator" })
	public Response create(@FormParam("quiz") String quizJson) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.quizDAO = new QuizDAO(this.simpleEntityManager.getEntityManager());

		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {
			Quiz quiz = new Gson().fromJson(quizJson, Quiz.class);

			if (quiz.getQuestions().size() >= 10) {
				if (!quiz.validateFields()) {

					System.out.println(quiz.toString());

					this.quizDAO.save(quiz);
					this.simpleEntityManager.commit();

					responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
				} else {

					responseBuilder = ResponseBuilderGenerator.createErrorResponseJSON(responseBuilder,
							JSONUtil.objectToJSON("Nome Invalido"));
				}
			} else {

				responseBuilder = ResponseBuilderGenerator.createErrorResponseJSON(responseBuilder,
						JSONUtil.objectToJSON("Selecione no minimo 10 Questões"));
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
	@RolesAllowed({ "Administrator" })
	public Response read() {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.quizDAO = new QuizDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			List<Quiz> quizs = this.quizDAO.findAll();

			for (Quiz quiz : quizs) {
				quiz.setQuestions(quiz.getQuestions());

				for (Question question : quiz.getQuestions()) {
					question.setAlternatives(question.getAlternatives());
					question.setContest(question.getContest());
					question.setLevel(question.getLevel());
					question.setMyFile(null);
				}

			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(quizs));

		} catch (Exception e) {
			System.out.println("Exception");
			this.simpleEntityManager.rollBack();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
			e.printStackTrace();
		} finally {
			this.simpleEntityManager.close();
		}

		return responseBuilder.build();

	}

	@GET
	@Path("/{idQuiz}")
	@RolesAllowed({ "Administrator" })
	public Response readByID(@PathParam("idQuiz") Long idQuiz) {
		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.quizDAO = new QuizDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Quiz quiz = this.quizDAO.getById(idQuiz);

			if (quiz != null) {

				responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
						JSONUtil.objectToJSON(quiz));

			} else {
				System.out.println("Quiz não existe");
				responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
			}

		} catch (Exception e) {
			this.simpleEntityManager.rollBack();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
			e.printStackTrace();
		} finally {
			this.simpleEntityManager.close();
		}

		return responseBuilder.build();
	}

	@GET
	@Path("/current")
	@PermitAll
	public Response readByDate(@HeaderParam("token") String token) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.quizDAO = new QuizDAO(this.simpleEntityManager.getEntityManager());
		StudentDAO studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		QuizStudentDAO quizStudentDAO = new QuizStudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();
		List<Quiz> currentQuiz = new ArrayList<>();
		
		boolean respondido = false;

		this.simpleEntityManager.beginTransaction();

		try {

			Student student = studentDAO.getByToken(token);

			List<QuizStudent> quizStudent = quizStudentDAO.getByStudent(student);

			if (student != null) {

				List<Quiz> quiz = this.quizDAO.getByDateAndContest(MyDateGenerator.getCurrentDate(),
						student.getContest());

				if (quiz != null) {

					for (Quiz q : quiz) {
						q.setDate(null);
						for (Question qs : q.getQuestions()) {
							qs.getMyFile().setDate(null);
						}

					}

					if (quizStudent.size() > 0) {

						for (Quiz q : quiz) {
							for (QuizStudent qs : quizStudent) {
								if (qs.getQuiz().getId() == q.getId()) respondido = true;
							}

							if (!respondido) currentQuiz.add(q);
							respondido = false;

						}

					} else {
						System.out.println("Quantidade de Quiz Respondidos é menor que Zero -> " + quizStudent.size());
						currentQuiz.addAll(quiz);
					}

					System.out.println("Quantidade de Quiz não respondidos -> " + currentQuiz.size());

					responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
							JSONUtil.objectToJSON(currentQuiz));

				} else {
					System.out.println("Quiz não existe");
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
				}

			} else {
				responseBuilder = ResponseBuilderGenerator.createUnauthorizedResponse(responseBuilder);
			}

		} catch (Exception e) {
			this.simpleEntityManager.rollBack();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
			e.printStackTrace();
		} finally {
			this.simpleEntityManager.close();
		}

		return responseBuilder.build();

	}
}