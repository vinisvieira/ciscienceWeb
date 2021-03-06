package br.com.ciscience.controll.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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

import br.com.ciscience.model.dao.impl.ContestDAO;
import br.com.ciscience.model.dao.impl.LevelDAO;
import br.com.ciscience.model.entity.impl.Contest;
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
	@RolesAllowed({ "Administrator" })
	public Response create(@FormParam("name") String name, @FormParam("time") String time) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.levelDAO = new LevelDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			if (!levelDAO.levelExist(name) && !time.equals(null) && !time.trim().equals("")) {

				Level level = new Level();
				level.setName(name);
				level.setTime(Integer.parseInt(time));
				level.setStatus(Constants.ACTIVE_ENTITY);

				if (!level.validateFields() && level.getTime() > 0) {

					this.levelDAO.save(level);
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

	@PUT
	@Path("/{id}")
	@RolesAllowed({ "Administrator" })
	public Response update(@PathParam("id") String id, @FormParam("name") String name, @FormParam("time") String time) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.levelDAO = new LevelDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Level level = this.levelDAO.getById(Long.parseLong(id));

			if (level != null) {
				if (!time.equals(null) && !time.trim().equals("")) {

					level.setName(name);
					level.setTime(Integer.parseInt(time));

					if (level.getTime() > 0 && !level.validateFields()) {

						this.levelDAO.save(level);
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
	@RolesAllowed({ "Administrator" })
	public Response delete(@PathParam("id") String id) {
		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.levelDAO = new LevelDAO(simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Level level = levelDAO.getById(Long.parseLong(id));

			if (level != null) {

				level.setStatus(level.isStatus() == false);
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

	@GET
	@RolesAllowed({ "Administrator" })
	public Response listLevel() {
		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.levelDAO = new LevelDAO(simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			List<Level> listLevel = this.levelDAO.findAll();
			List<Level> listLevelToJson = new ArrayList<Level>();

			for (Level level : listLevel) {

				if (level.isStatus() == true) {

					listLevelToJson.add(level);

				}
			}
			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(listLevelToJson));

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
	public Response getById(@PathParam("id") String id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.levelDAO = new LevelDAO(simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Level level = this.levelDAO.getById(Long.parseLong(id));

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(level));

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
	@Path("/by-name")
	@RolesAllowed({ "Administrator" })
	public Response search(@HeaderParam("name") String name) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.levelDAO = new LevelDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();
		List<Level> levels;

		this.simpleEntityManager.beginTransaction();

		try {
			levels = this.levelDAO.listForName(name);

			if (levels != null) {

				responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
						JSONUtil.objectToJSON(levels));

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