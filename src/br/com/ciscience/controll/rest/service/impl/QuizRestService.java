package br.com.ciscience.controll.rest.service.impl;

import javax.annotation.security.PermitAll;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;

import br.com.ciscience.model.dao.impl.QuizDAO;
import br.com.ciscience.model.dao.impl.StudentDAO;
import br.com.ciscience.model.entity.impl.Quiz;

import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
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
				if (quizJson.getQuestionAnswers() != null
						&& quizJson.getQuestionAnswers().size() == 12) {
					if (this.studentDAO.studentExists(quizJson.getStudent())) {
						if (!this.quizDAO.dateVerification(quizJson)) {

							this.quizDAO.save(quizJson);
							this.simpleEntityManager.commit();

							responseBuilder = ResponseBuilderGenerator
									.createOKResponseTextPlain(responseBuilder);
						} else {

							responseBuilder = ResponseBuilderGenerator
									.createErrorResponse(responseBuilder);

						}

					} else {
						responseBuilder = ResponseBuilderGenerator
								.createErrorResponse(responseBuilder);
					}

				} else {
					responseBuilder = ResponseBuilderGenerator
							.createErrorResponse(responseBuilder);
				}

			} else {
				responseBuilder = ResponseBuilderGenerator
						.createErrorResponse(responseBuilder);
			}

		} catch (Exception e) {
			this.simpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator
					.createErrorResponse(responseBuilder);
		} finally {
			this.simpleEntityManager.close();
		}

		return responseBuilder.build();
	}
}