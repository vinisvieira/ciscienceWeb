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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import br.com.ciscience.model.dao.impl.ContestDAO;
import br.com.ciscience.model.dao.impl.MyFileDAO;
import br.com.ciscience.model.dao.impl.StudentDAO;
import br.com.ciscience.model.entity.impl.Contest;
import br.com.ciscience.model.entity.impl.MyFile;
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

		MyFileDAO myFileDAO = new MyFileDAO(this.simpleEntityManager.getEntityManager());

		MyFile studentPicture = new MyFile();

		try {

			this.simpleEntityManager.beginTransaction();

			studentPicture.setName(null);
			studentPicture.setDate(null);
			studentPicture.setStatus(Constants.ACTIVE_ENTITY);

			myFileDAO.save(studentPicture);

			Student student = new Student();
			student.setName(name);
			student.setCpf(StringUtil.setCpfUnformatted(cpf));
			student.setEmail(email);
			student.setBirthday(MyDateGenerator.dateStringToSql(birthday));
			student.setPassword(StringUtil.SHA1(student.getCpf()));
			student.setScore(0L);
			student.setMyFile(studentPicture);
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
							myMail.setBody("O cadastro de sua conta foi realizado.<br> " + "Login: <b> "
									+ student.getEmail() + "<br>" + "Senha: <b> " + student.getCpf());

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

	@POST
	@Path("/avatar/mobile")
	@PermitAll
	public Response updateAvata(@Context HttpServletRequest request, @HeaderParam("token") String token) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		File f = null;
		FileOutputStream fos = null;
		byte[] media = null;
		String mediaName = String.valueOf(System.currentTimeMillis());

		Student student = studentDAO.getByToken(token);

		try {
			this.simpleEntityManager.beginTransaction();

			if (student != null) {

				if (!ServletFileUpload.isMultipartContent(request)) {
					System.err.println("Não é um Conteúdo multipart/form-data");
				} else {
					List<FileItem> fields = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

					HashMap<String, String> parameters = new HashMap<>();

					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).isFormField()) {
							parameters.put(fields.get(i).getFieldName(), fields.get(i).getString());
						} else {
							parameters.put(fields.get(i).getFieldName(), fields.get(i).getName());
							f = new File(System.getProperty(Constants.CATALINA_BASE) + Constants.UPLOAD_PATH + mediaName
									+ Constants.MEDIA_JPG);
							media = fields.get(i).get();

							student.getMyFile().setName(mediaName + Constants.MEDIA_JPG);
							student.getMyFile().setDate(MyDateGenerator
									.dateStringToSql(new SimpleDateFormat("dd/mm/yyyy").format(new Date())));
							student.getMyFile().setStatus(Constants.ACTIVE_ENTITY);

							if (media != null) {
								fos = new FileOutputStream(f);

								fos.write(media);
								fos.flush();
								fos.close();
							}
						}
					}
					this.simpleEntityManager.commit();
					responseBuilder = ResponseBuilderGenerator.createOKResponseTextPlain(responseBuilder);
				}

			} else {

				responseBuilder = ResponseBuilderGenerator.createUnauthorizedResponse(responseBuilder);
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
	@Path("/ranking/mobile")
	@PermitAll
	public Response listByRankingMobile(@HeaderParam("token") String token) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Student student = this.studentDAO.getByToken(token);

			if (student != null) {

				List<Student> students = this.studentDAO.listarRanking(student.getContest());

				if (students != null) {

					for (Student s : students) {
						s.setBirthday(null);
						s.setUserSince(null);
						if (s.getMyFile() != null)
							s.getMyFile().setDate(null);
						s.setQuiz(null);
					}

					responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
							JSONUtil.objectToJSON(students));
				} else {
					responseBuilder = ResponseBuilderGenerator.createErrorResponse(responseBuilder);
				}

			} else {
				responseBuilder = ResponseBuilderGenerator.createUnauthorizedResponse(responseBuilder);
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
	@Path("/katana")
	@PermitAll
	public Response getStudentByToken(@HeaderParam("token") String token) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		System.out.println("token -> " + token);

		try {

			Student student = this.studentDAO.getByToken(token);

			if (student != null) {

				System.out.println(student.toString());
				student.setPassword(null);
				student.setBirthday(null);
				student.setUserSince(null);
				if (student.getMyFile() != null)
					student.getMyFile().setDate(null);

				responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
						JSONUtil.objectToJSON(student));

			} else {
				responseBuilder = ResponseBuilderGenerator.createUnauthorizedResponse(responseBuilder);
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
	@Path("/recovery")
	@PermitAll
	public Response recoveryPassword(@FormParam("email") String email, @HeaderParam("recoveryKey") String recoveryKey) {

		this.simpleEntityManager = new JPAUtil(Constants.PERSISTENCE_UNIT_NAME);
		this.studentDAO = new StudentDAO(this.simpleEntityManager.getEntityManager());
		ResponseBuilder responseBuilder = Response.noContent();

		this.simpleEntityManager.beginTransaction();

		try {

			Student student = this.studentDAO.getByEmail(email);

			if (student != null && recoveryKey.equals(Constants.RECOVERY_KEY)) {

				student.setPassword(StringUtil.SHA1(student.getCpf()));
				
				this.simpleEntityManager.commit();
				
				MyMail myMail = new MyMail();
				myMail.setTo(student.getEmail());
				myMail.setSubject("RECUPERAÇÃO DE SENHA");
				myMail.setBody("Sua nova senha é: <b>" + student.getCpf() + "</b>");
				
				MyMailService.send(myMail);

				responseBuilder = ResponseBuilderGenerator.createOKResponseJSON(responseBuilder,
						JSONUtil.objectToJSON(student));

			} else {
				responseBuilder = ResponseBuilderGenerator.createUnauthorizedResponse(responseBuilder);
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