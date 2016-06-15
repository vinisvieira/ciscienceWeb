package br.com.ciscience.controll.rest.service.impl;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.com.ciscience.model.dao.impl.QuestionDAO;
import br.com.ciscience.model.entity.impl.Question;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.ResponseBuilderGenerator;

@Path("/question")
public class QuestionRestService {

	private JPAUtil mSimpleEntityManager;
	private QuestionDAO mQuestionDAO;

	@GET
	@PermitAll
	public Response readQuestions() {

		this.mSimpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(this.mSimpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.mSimpleEntityManager.beginTransaction();

		try {

			List<Question> questions = this.mQuestionDAO.findAll();

			for (Question question : questions) {
				question.setAlternatives(question.getAlternatives());
			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(questions));

		} catch (Exception e) {
			this.mSimpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
			this.mSimpleEntityManager.close();
		}

		return responseBuilder.build();

	}

}
