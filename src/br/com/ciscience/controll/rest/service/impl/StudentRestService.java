package br.com.ciscience.controll.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
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
import br.com.ciscience.model.dao.impl.StudentDAO;
import br.com.ciscience.model.entity.impl.Contest;
import br.com.ciscience.model.entity.impl.MyMail;
import br.com.ciscience.model.entity.impl.Quiz;
import br.com.ciscience.model.entity.impl.Student;
import br.com.ciscience.model.jpa.impl.JPAUtil;
import br.com.ciscience.util.Constants;
import br.com.ciscience.util.JSONUtil;
import br.com.ciscience.util.MyDateGenerator;
import br.com.ciscience.util.MyMailService;
import br.com.ciscience.util.ResponseBuilderGenerator;
import br.com.ciscience.util.StringUtil;
import br.com.ciscience.util.ValidarCPF;

@Path("/student")
public class StudentRestService {

	private JPAUtil simpleEntityManager;
	private StudentDAO studentDAO;
	private ContestDAO contestDAO;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Path("/active")
	@PermitAll
	public Response listActive() {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();

			List<Student> students = this.studentDAO.findAll();
			List<Student> studentsToJson = new ArrayList<>();

			for (int i = 0; i < students.size(); i++) {
				if (students.get(i).getStatus()) {
					students.get(i).setPassword(null);
					students.get(i).setQuiz(students.get(i).getQuiz());

					for (Quiz quiz : students.get(i).getQuiz()) {
						quiz.setQuestions(null);
					}

					studentsToJson.add(students.get(i));
				}
			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(studentsToJson));

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
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		try {

			this.simpleEntityManager.beginTransaction();

			List<Student> students = this.studentDAO.findAll();
			List<Student> studentsToJson = new ArrayList<>();

			for (int i = 0; i < students.size(); i++) {
				if (!students.get(i).getStatus()) {
					students.get(i).setPassword(null);
					studentsToJson.add(students.get(i));
				}
			}

			responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
					JSONUtil.objectToJSON(studentsToJson));

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
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Student student = this.studentDAO.getById(Long.parseLong(id));

			if (student != null) {

				student.setContest(student.getContest());
				student.setQuiz(null);
				student.setPassword(null);

				responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
						JSONUtil.objectToJSON(student));

			} else {
				System.out.println("O aluno informado não existe");
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

	@POST
	@PermitAll
	public Response create(@FormParam("name") String name, @FormParam("cpf") String cpf,
			@FormParam("email") String email, @FormParam("birthday") String birthday,
			@FormParam("idContest") Long idContest) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		this.contestDAO = new ContestDAO(this.simpleEntityManager.getEntityManager());

		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Student student = new Student();
			student.setName(name);
			student.setCpf(StringUtil.setCpfUnformatted(cpf));
			student.setEmail(email);
			student.setBirthday(MyDateGenerator.dateStringToSql(birthday));
			student.setPassword(StringUtil.generateRandomicPassword(student.getCpf(), student.getEmail()));
			student.setScore(0L);
			student.setUserSince(MyDateGenerator.getCurrentDate());
			student.setStatus(Constants.ACTIVE_ENTITY);

			if (!this.studentDAO.emailExists(student)) {

				if (student.validateEmptyFields() && StringUtil.validEmail(student.getEmail())) {

					if (ValidarCPF.validaCPF(student.getCpf())) {

						Contest contest = this.contestDAO.getById(idContest);

						if (contest != null) {

							student.setContest(contest);

							this.studentDAO.save(student);
							this.simpleEntityManager.commit();

							MyMail myMail = new MyMail();
							myMail.setTo(student.getEmail());
							myMail.setSubject("SCIENCE - CADASTRO REALIZADO");
							myMail.setBody(
									"O cadastro de sua conta foi realizado.<br> " + "Login: <b> " + student.getEmail()
											+ "<br>" + "Senha: <b> " + student.getCpf() + student.getEmail());

							MyMailService.send(myMail);

							responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);

						} else {
							responseBuilder = ResponseBuilderGenerator.createErrorResponseJSON(responseBuilder,
									JSONUtil.objectToJSON("Concurso informado não existe"));
						}
					} else {
						responseBuilder = ResponseBuilderGenerator.createErrorResponseJSON(responseBuilder,
								JSONUtil.objectToJSON("CPF informado é inválido"));
					}

				} else {
					responseBuilder = ResponseBuilderGenerator.createErrorResponseJSON(responseBuilder,
							JSONUtil.objectToJSON("Erro na validação dos campos"));
				}
			} else {
				System.out.println("Email ja existe");
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
	@PermitAll
	public Response delete(@PathParam("id") Long id) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Student student = this.studentDAO.getById(id);

			if (student != null) {

				student.setStatus(student.getStatus() == false);
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
	public Response update(@PathParam("id") Long id, @FormParam("name") String name, @FormParam("cpf") String cpf,
			@FormParam("email") String email, @FormParam("birthday") String birthday,
			@FormParam("password") String password, @FormParam("confirmPassword") String confirmPassword) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Student student = this.studentDAO.getById(id);

			if (student != null) {
				student.setEmail(email);
				student.setName(name);
				student.setCpf(StringUtil.setCpfUnformatted(cpf));
				student.setBirthday(MyDateGenerator.dateStringToSql(birthday));
				student.setPassword(StringUtil.SHA1(password));

				if (student.validateEmptyFields()
						&& student.passwordsMatch(student.getPassword(), StringUtil.SHA1(confirmPassword))) {

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

	@GET
	@Path("/by-email")
	@PermitAll
	public Response searchByEmail(@HeaderParam("email") String email) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			List<Student> students = this.studentDAO.listarPorEmail(email);

			if (students != null) {

				responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
						JSONUtil.objectToJSON(students));
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
	@Path("/ranking")
	@PermitAll
	public Response listByRanking() {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			List<Student> students = this.studentDAO.listarRanking();

			if (students != null) {

				responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
						JSONUtil.objectToJSON(students));
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