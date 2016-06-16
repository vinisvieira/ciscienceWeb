package br.com.ciscience.controll.rest.service.impl;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.com.ciscience.model.dao.impl.LevelDAO;
import br.com.ciscience.model.entity.impl.Level;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.ResponseBuilderGenerator;

@Path("/level")
public class LevelRestService {

	private LevelDAO levelDAO;
	private JPAUtil simpleEntityManager;

	@Context
	private HttpServletRequest servletRequest;

	@POST
	@PermitAll
	public Response create(@FormParam("name") String name,
			@FormParam("time") String time) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.levelDAO = new LevelDAO(
				this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Level level = new Level();
			level.setName(name);
			level.setTime(Integer.parseInt(time));
			level.setStatus(Constants.ACTIVE_ENTITY);

			if (level != null && level.getTime() > 0) {

				this.levelDAO.save(level);
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

	@PUT
	@PermitAll
	@Path("/{id}")
	public Response update(@FormParam("id") String id,
			@FormParam("name") String name, @FormParam("time") String time) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.levelDAO = new LevelDAO(
				this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Level level = this.levelDAO.getById(Long.parseLong(id));
			if (level != null) {

				level.setName(name);
				level.setTime(Integer.parseInt(time));

				if (level.getTime() > 0) {
					this.levelDAO.save(level);
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
	}

	@DELETE
	@PermitAll
	@Path("/{id}")
	public Response delete(@FormParam("id") String id) {
		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.levelDAO = new LevelDAO(simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Level level = levelDAO.getById(Long.parseLong(id));

			if (level != null) {

				level.setStatus(level.isStatus() == false);
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

	@GET
	@PermitAll
	public Response listlevel() {
		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.levelDAO = new LevelDAO(simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			List<Level> listLevel = this.levelDAO.findAll();

			for (Level level : listLevel) {

				if (level.isStatus()) {

					listLevel.add(level);

					responseBuilder = ResponseBuilderGenerator
							.createOKResponseJSON(responseBuilder,
									JSONUtil.objectToJSON(listLevel));

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

	@GET
	@PermitAll
	@Path("/{id}")
	public Response getById(@FormParam("id") String id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.levelDAO = new LevelDAO(simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(
					responseBuilder, JSONUtil.objectToJSON(this.levelDAO
							.getById(Long.parseLong(id))));

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