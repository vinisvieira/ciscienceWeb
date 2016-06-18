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
import br.com.ciscience.model.entity.impl.Contest;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.ResponseBuilderGenerator;

@Path("/contest")
public class ContestRestService {

	private JPAUtil simpleEntityManager;
	private ContestDAO contestDAO;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Path("/active")
	@PermitAll
	public Response listActive() {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.contestDAO = new ContestDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();
		List<Contest> contestToJson = new ArrayList<>();
		try {

			this.simpleEntityManager.beginTransaction();

			List<Contest> contests = this.contestDAO.findAll();

			for (Contest contest : contests) {
				if (contest.getStatus()) {
					contestToJson.add(contest);
				}
			}
			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(contestToJson));

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
	@Path("/inactive")
	@PermitAll
	public Response listInactive() {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.contestDAO = new ContestDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();
		List<Contest> contestToJson = new ArrayList<>();
		try {

			this.simpleEntityManager.beginTransaction();

			List<Contest> contests = this.contestDAO.findAll();

			for (Contest contest : contests) {
				if (!contest.getStatus()) {
					contestToJson.add(contest);
				}
			}
			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(contestToJson));

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
	@PermitAll
	public Response getByID(@PathParam("id") String id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.contestDAO = new ContestDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(this.contestDAO.getById(Long.parseLong(id))));

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
	public Response create(@FormParam("name") String name) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.contestDAO = new ContestDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();
		List<Contest> contestList = this.contestDAO.findAll();
		this.simpleEntityManager.beginTransaction();

		try {

			Contest contestNew = new Contest();

			contestNew.setName(name);
			contestNew.setStatus(Constants.ACTIVE_ENTITY);

			for (Contest contest : contestList) {
				if (contest.getName().equals(contestNew.getName())) {
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
					return responseBuilder.build();
				}
			}
			if (contestNew.validateEmptyFields()) {
				this.contestDAO.save(contestNew);
				this.simpleEntityManager.commit();

				responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
			} else {
				System.out.println("erro na validasao dos campos");
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
	@Path("/{idcontest}")
	@PermitAll
	public Response delete(@PathParam("idcontest") Long idcontest) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.contestDAO = new ContestDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Contest contest = this.contestDAO.getById(idcontest);

			if (contest != null) {

				contest.setStatus(!contest.getStatus());

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

	@PUT
	@Path("/{id}")
	@PermitAll
	public Response update(@PathParam("id") Long id, @FormParam("name") String name) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.contestDAO = new ContestDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();
		List<Contest> contestList = this.contestDAO.findAll();
		this.simpleEntityManager.beginTransaction();

		try {

			Contest contestOld = this.contestDAO.getById(id);
			for (Contest contest : contestList) {
				if (contest.getName().equals(name)) {
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
					System.out.println("nome Igual ao do banco ----------------------------------------------------");
					return responseBuilder.build();
				}
			}

			if (contestOld != null) {
				contestOld.setName(name);
			
				if (contestOld.validateEmptyFields()) {

					this.contestDAO.update(contestOld);
					this.simpleEntityManager.commit();

					responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
				} else {
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
				}

			} else {
				System.out.println("contesteOLD ------------------------------------------------ Null");
				responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
			}

		} catch (Exception e) {
			this.simpleEntityManager.rollBack();
			System.out.println("--------------------------------------------------------------------------Cath");
			e.printStackTrace();
			responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
		} finally {
			this.simpleEntityManager.close();
		}

		return responseBuilder.build();

	}

}
