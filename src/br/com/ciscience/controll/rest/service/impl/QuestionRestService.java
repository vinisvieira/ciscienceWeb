package br.com.ciscience.controll.rest.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.com.ciscience.model.dao.impl.LevelDAO;
import br.com.ciscience.model.dao.impl.QuestionDAO;
import br.com.ciscience.model.entity.impl.Level;
import br.com.ciscience.model.entity.impl.Question;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;

@Path("/question")
public class QuestionRestService {

	private JPAUtil mSimpleEntityManager;
	private QuestionDAO mQuestionDAO;
	private LevelDAO mLevelDAO;
	
	@Context
	private HttpServletRequest servletRequest;

	public Response create(@FormParam("idLevel") String idLevel, @FormParam("text") String text,@FormParam("score") String score){
		
		this.mSimpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.mQuestionDAO = new QuestionDAO(
				this.mSimpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.mSimpleEntityManager.beginTransaction();
		
		try {
			
			Question question = new Question();
			Level level = this.mLevelDAO.getById(Long.parseLong(idLevel));
			
			if(level != null && question != null){
			
			question.setText(text);
			question.setScore(Integer.parseInt(score));
			question.setStatus(Constants.ACTIVE_ENTITY);
			question.setLevel(level);
			
			}
		} catch (Exception e) {
			
		}
		
		return responseBuilder.build();
	}

}
