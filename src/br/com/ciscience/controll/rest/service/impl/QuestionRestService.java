package br.com.ciscience.controll.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
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

import br.com.ciscience.model.dao.impl.ContestDAO;
import br.com.ciscience.model.dao.impl.LevelDAO;
import br.com.ciscience.model.dao.impl.QuestionDAO;
import br.com.ciscience.model.entity.impl.Contest;
import br.com.ciscience.model.entity.impl.Level;
import br.com.ciscience.model.entity.impl.Question;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.ResponseBuilderGenerator;

@Path("/question")
public class QuestionRestService {

	private JPAUtil mSimpleEntityManager;
	private QuestionDAO mQuestionDAO;
	private LevelDAO mLevelDAO;
	private ContestDAO mContestDAO;

	@Context
	private HttpServletRequest servletRequest;

	@POST
	@PermitAll
	public Response create(@FormParam("idContest") Long idContest, @FormParam("idLevel") Long idLevel,
			@FormParam("text") String text, @FormParam("score") int score) {

		this.mSimpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(this.mSimpleEntityManager.getEntityManager());
		this.mLevelDAO = new LevelDAO(this.mSimpleEntityManager.getEntityManager());
		this.mContestDAO = new ContestDAO(this.mSimpleEntityManager.getEntityManager());

		ResponseBuilder responseBuilder = Response.noContent();

		this.mSimpleEntityManager.beginTransaction();

		try {

			Question question = new Question();
			Level level = this.mLevelDAO.getById(idLevel);
			Contest contest = this.mContestDAO.getById(idContest);

			if (level != null && contest != null) {

				question.setText(text);
				question.setScore(score);
				question.setStatus(Constants.ACTIVE_ENTITY);
				question.setLevel(level);
				question.setContest(contest);

				if (question != null) {

					this.mQuestionDAO.save(question);
					this.mSimpleEntityManager.commit();

					responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
				}
			} else {

				responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
			}
		} catch (Exception e) {
			this.mSimpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
			this.mSimpleEntityManager.close();
		}

		return responseBuilder.build();
	}

	@PUT
	@PermitAll
	@Path("/{id}")
	public Response update(@PathParam("id") String id, @FormParam("idContest") Long idContest,
			@FormParam("idLevel") Long idLevel, @FormParam("text") String text, @FormParam("score") int score) {

		this.mSimpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(this.mSimpleEntityManager.getEntityManager());
		this.mLevelDAO = new LevelDAO(this.mSimpleEntityManager.getEntityManager());
		this.mContestDAO = new ContestDAO(this.mSimpleEntityManager.getEntityManager());

		ResponseBuilder responseBuilder = Response.noContent();

		this.mSimpleEntityManager.beginTransaction();

		try {

			Question question = this.mQuestionDAO.getById(Long.parseLong(id));

			if (question != null) {

				Level level = this.mLevelDAO.getById(idLevel);
				Contest contest = this.mContestDAO.getById(idContest);

				if (level != null && contest != null) {

					question.setText(text);
					question.setScore(score);
					question.setLevel(level);
					question.setContest(contest);

					this.mQuestionDAO.save(question);
					this.mSimpleEntityManager.commit();

					responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
				} else {
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
				}
			} else {
				responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
			}
		} catch (Exception e) {
			this.mSimpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
			this.mSimpleEntityManager.close();
		}
		return responseBuilder.build();
	}

	@DELETE
	@PermitAll
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {

		this.mSimpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(this.mSimpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.mSimpleEntityManager.beginTransaction();

		try {

			Question question = this.mQuestionDAO.getById(Long.parseLong(id));

			if (question != null) {

				question.setStatus(question.getStatus() == false);
				this.mSimpleEntityManager.commit();
				responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);

			} else {
				responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
			}
		} catch (Exception e) {
			this.mSimpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
			this.mSimpleEntityManager.close();
		}

		return responseBuilder.build();
	}

	@GET
	@PermitAll
	public Response listQuestion() {

		this.mSimpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(this.mSimpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.mSimpleEntityManager.beginTransaction();

		try {

			List<Question> questions = this.mQuestionDAO.findAll();
			List<Question> questionsToJson = new ArrayList<Question>();

			for (Question question : questions) {

				if (question.getStatus() == true) {

					questionsToJson.add(question);

				}

			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(questionsToJson));
		} catch (Exception e) {

			this.mSimpleEntityManager.rollBack();

			e.printStackTrace();

			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {

			this.mSimpleEntityManager.close();
		}
		return responseBuilder.build();

	}

	@GET
	@PermitAll
	@Path("/{id}")
	public Response getById(@PathParam("id") String id) {

		this.mSimpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(this.mSimpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.mSimpleEntityManager.beginTransaction();

		try {

			Question question = this.mQuestionDAO.getById(Long.parseLong(id));

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(question));

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