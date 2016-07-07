package br.com.ciscience.controll.rest.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;

import br.com.ciscience.model.dao.impl.AlternativeDAO;
import br.com.ciscience.model.dao.impl.ContestDAO;
import br.com.ciscience.model.dao.impl.LevelDAO;
import br.com.ciscience.model.dao.impl.MyFileDAO;
import br.com.ciscience.model.dao.impl.QuestionDAO;
import br.com.ciscience.model.entity.impl.Alternative;
import br.com.ciscience.model.entity.impl.Contest;
import br.com.ciscience.model.entity.impl.Level;
import br.com.ciscience.model.entity.impl.MyFile;
import br.com.ciscience.model.entity.impl.Question;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.MyDateGenerator;
import br.com.ciscience.util.ResponseBuilderGenerator;

@Path("/question")
public class QuestionRestService {

	private JPAUtil simpleEntityManager;
	private QuestionDAO mQuestionDAO;
	private LevelDAO mLevelDAO;
	private ContestDAO mContestDAO;
	private AlternativeDAO mAlternativeDAO;

	@Context
	private HttpServletRequest servletRequest;

	@Context
	private HttpServletResponse servletResponse;

	@POST
	@PermitAll
	public Response create(@Context HttpServletRequest request) {
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);

		this.mQuestionDAO = new QuestionDAO(
				this.simpleEntityManager.getEntityManager());

		this.mLevelDAO = new LevelDAO(
				this.simpleEntityManager.getEntityManager());

		this.mContestDAO = new ContestDAO(
				this.simpleEntityManager.getEntityManager());

		this.mAlternativeDAO = new AlternativeDAO(
				this.simpleEntityManager.getEntityManager());

		ResponseBuilder mResponseBuilder = Response.noContent();

		MyFileDAO myFileDAO = new MyFileDAO(
				this.simpleEntityManager.getEntityManager());

		MyFile questionPicture = new MyFile();
		Question quest = null;
		File f = null;
		FileOutputStream fos = null;
		byte[] media = null;
		String mediaName = String.valueOf(System.currentTimeMillis());

		try {

			this.simpleEntityManager.beginTransaction();
			if (!ServletFileUpload.isMultipartContent(request)) {
				System.err.println("Não é um Conteúdo multipart/form-data");
			} else {
				List<FileItem> fields = new ServletFileUpload(
						new DiskFileItemFactory()).parseRequest(request);

				HashMap<String, String> parameters = new HashMap<>();

				for (int i = 0; i < fields.size(); i++) {
					if (fields.get(i).isFormField()) {
						parameters.put(fields.get(i).getFieldName(), fields
								.get(i).getString());
					} else {
						parameters.put(fields.get(i).getFieldName(), fields
								.get(i).getName());
						f = new File(
								System.getProperty(Constants.CATALINA_BASE)
										+ Constants.UPLOAD_PATH
										+ mediaName + Constants.MEDIA_JPG);
						media = fields.get(i).get();

						questionPicture.setName(mediaName + Constants.MEDIA_JPG);
						questionPicture.setDate(MyDateGenerator
								.dateStringToSql(new SimpleDateFormat(
										"dd/mm/yyyy").format(new Date())));
						questionPicture.setStatus(Constants.ACTIVE_ENTITY);

						if (media != null) {
							fos = new FileOutputStream(f);

							fos.write(media);
							fos.flush();
							fos.close();
						}
					}
				}

				myFileDAO.save(questionPicture);

				quest = new Gson().fromJson(parameters.get("question"),
						Question.class);

				quest.setMyFile(questionPicture);

				Contest contest = this.mContestDAO.getById(quest.getContest()
						.getId());
				Level level = this.mLevelDAO
						.getById(quest.getContest().getId());
				for (Alternative alternative : quest.getAlternatives()) {
					this.mAlternativeDAO.save(alternative);
				}
				if (level != null && contest != null) {
					if (!quest.validateFields()) {
						this.mQuestionDAO.save(quest);
						this.simpleEntityManager.commit();
						responseBuilder = ResponseBuilderGenerator
								.createOKResponseTextPlain(mResponseBuilder);

					} else {
						responseBuilder = ResponseBuilderGenerator
								.createErrorResponse(mResponseBuilder);
						System.out.println("Erro na Validação");
					}
				} else {
					responseBuilder = ResponseBuilderGenerator
							.createErrorResponse(mResponseBuilder);
					System.out.println("level ou contest nulo");
				}
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

	/*@POST
	@PermitAll
	public Response create(@FormParam("question") String questionJson) {
		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(
				this.simpleEntityManager.getEntityManager());
		this.mLevelDAO = new LevelDAO(
				this.simpleEntityManager.getEntityManager());
		this.mContestDAO = new ContestDAO(
				this.simpleEntityManager.getEntityManager());
		this.mAlternativeDAO = new AlternativeDAO(
				this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Question question = new Gson().fromJson(questionJson,
					Question.class);
			Contest contest = this.mContestDAO.getById(question.getContest()
					.getId());
			Level level = this.mLevelDAO.getById(question.getLevel().getId());
			for (Alternative alternative : question.getAlternatives()) {
				this.mAlternativeDAO.save(alternative);
			}

			if (level != null && contest != null) {

				if (!question.validateFields()) {

					this.mQuestionDAO.save(question);
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
		} catch (Exception e) {
			this.simpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator
					.createErrorResponse(responseBuilder);
		} finally {
			this.simpleEntityManager.close();
		}

		return responseBuilder.build();
	}*/

	/*@PUT
	@PermitAll
	@Path("/{id}")
	public Response update(@PathParam("id") String id,
			@FormParam("idContest") Long idContest,
			@FormParam("idLevel") Long idLevel, @FormParam("text") String text,
			@FormParam("score") int score) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(
				this.simpleEntityManager.getEntityManager());
		this.mLevelDAO = new LevelDAO(
				this.simpleEntityManager.getEntityManager());
		this.mContestDAO = new ContestDAO(
				this.simpleEntityManager.getEntityManager());

		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

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

					if (!question.validateFields()) {
						this.mQuestionDAO.save(question);
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
		} catch (Exception e) {
			this.simpleEntityManager.rollBack();
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator
					.createErrorResponse(responseBuilder);
		} finally {
			this.simpleEntityManager.close();
		}
		return responseBuilder.build();
	}*/

	/*@DELETE
	@PermitAll
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(
				this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Question question = this.mQuestionDAO.getById(Long.parseLong(id));

			if (question != null) {

				question.setStatus(question.getStatus() == false);
				this.simpleEntityManager.commit();
				responseBuilder = ResponseBuilderGenerator
						.createOKResponseTextPlain(responseBuilder);

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
*/
	@GET
	@PermitAll
	public Response listQuestion() {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(
				this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			List<Question> questions = this.mQuestionDAO.findAll();
			List<Question> questionsToJson = new ArrayList<Question>();

			for (Question question : questions) {

				if (question.getStatus() == true) {

					questionsToJson.add(question);

				}

			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(
					responseBuilder, JSONUtil.objectToJSON(questionsToJson));
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

	@GET
	@Path("/filter/{contest}/{level}")
	@PermitAll
	public Response listFilter(@PathParam("contest") Long idContest,
			@PathParam("level") Long idLevel) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(
				this.simpleEntityManager.getEntityManager());
		this.mLevelDAO = new LevelDAO(
				this.simpleEntityManager.getEntityManager());
		this.mContestDAO = new ContestDAO(
				this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			List<Question> questions = this.mQuestionDAO.findAll();
			List<Question> questionsToJson = new ArrayList<Question>();

			for (Question question : questions) {

				if (question.getStatus() == true) {

					// populando lista com contest true e level false
					if (question.getContest().equals(
							mContestDAO.getById(idContest))
							&& idLevel == 0) {
						questionsToJson.add(question);
					}
					// populando lista com level true e contest false
					if (question.getLevel().equals(mLevelDAO.getById(idLevel))
							&& idContest == 0) {
						questionsToJson.add(question);
					}
					// populando lista com level true e contest true
					if (question.getLevel().equals(mLevelDAO.getById(idLevel))
							&& question.getContest().equals(
									mContestDAO.getById(idContest))) {
						questionsToJson.add(question);
					}

				}

			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(
					responseBuilder, JSONUtil.objectToJSON(questionsToJson));
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

	@GET
	@PermitAll
	@Path("/{id}")
	public Response getById(@PathParam("id") String id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(
				this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Question question = this.mQuestionDAO.getById(Long.parseLong(id));

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(
					responseBuilder, JSONUtil.objectToJSON(question));

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