package br.com.ciscience.controll.rest.service.impl;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;

import br.com.ciscience.model.dao.impl.QuizDAO;
import br.com.ciscience.model.dao.impl.StudentDAO;
import br.com.ciscience.model.entity.impl.QuestionAnswer;
import br.com.ciscience.model.entity.impl.Quiz;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.ResponseBuilderGenerator;

@Path("/quiz")
public class QuizRestService {

	private QuizDAO quizDAO;
	private StudentDAO studentDAO;
	private JPAUtil simpleEntityManager;

	@Context
	private HttpServletRequest servletRequest;

	@POST
	@PermitAll
	public Response create(@FormParam("quiz") String quiz) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.quizDAO = new QuizDAO(this.simpleEntityManager.getEntityManager());

		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Quiz quizJson = new Gson().fromJson(quiz, Quiz.class);

			if (quizJson.getScore() < 0) {
				if (quizJson.getQuestionAnswers() != null && quizJson.getQuestionAnswers().size() == 12) {
					if (this.studentDAO.studentExists(quizJson.getStudent())) {
						if (!this.quizDAO.dateVerification(quizJson)) {

							this.quizDAO.save(quizJson);
							this.simpleEntityManager.commit();

							responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
						} else {

							responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);

						}

					} else {
						responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
					}

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
	@PermitAll
	public Response read() {
		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.quizDAO = new QuizDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			List<Quiz> quizs = this.quizDAO.findAll();

			for (Quiz quiz : quizs) {
				quiz.setQuestionAnswers(null);
				quiz.setStudent(quiz.getStudent());
				quiz.getStudent().setPassword(null);
				quiz.getStudent().setQuiz(null);
			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(quizs));

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
	@Path("/{idQuiz}")
	@PermitAll
	public Response readByID(@PathParam("idQuiz") Long idQuiz) {
		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.quizDAO = new QuizDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Quiz quiz = this.quizDAO.getById(idQuiz);

			if (quiz != null) {

				quiz.setQuestionAnswers(quiz.getQuestionAnswers());

				for (QuestionAnswer questionAnswer : quiz.getQuestionAnswers()) {
					questionAnswer.setQuestion(questionAnswer.getQuestion());
					questionAnswer.getQuestion().setAlternatives(null);
					questionAnswer.getQuestion().setContest(null);
					questionAnswer.setStudent(null);
				}

				quiz.setStudent(quiz.getStudent());
				quiz.getStudent().setPassword(null);
				quiz.getStudent().setQuiz(null);

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

}