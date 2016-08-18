package br.com.ciscience.model.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Contest;
import br.com.ciscience.model.entity.impl.Student;

public class StudentDAO extends GenericDAO<Long, Student> {

	public StudentDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

	public boolean emailExists(Student student) {
		EntityManager entityManager = super.getEntityManager();

		Query query = entityManager.createQuery("SELECT u FROM Student u WHERE u.email = :email");
		query.setParameter("email", student.getEmail());
		return (query.getResultList().size() > 0);
	}

	public boolean studentExists(Student student) {
		EntityManager entityManager = super.getEntityManager();

		Query query = entityManager.createQuery("SELECT u FROM Student u WHERE u.id = :id");
		query.setParameter("id", student.getId());
		return (query.getResultList().size() > 0);
	}

	public List<Student> listarPorEmail(String email) {
		EntityManager entityManager = super.getEntityManager();
		TypedQuery<Student> query = entityManager.createQuery("SELECT u FROM Student u WHERE u.email = :email",
				Student.class);
		query.setParameter("email", email);

		return query.getResultList();
	}

	public List<Student> listarRanking() {
		EntityManager entityManager = super.getEntityManager();

		TypedQuery<Student> query = entityManager
				.createQuery("SELECT u FROM Student u WHERE u.status = true ORDER BY u.score DESC ", Student.class);
		return query.getResultList();
	}
	
	public List<Student> listarRanking(Contest contest) {
		EntityManager entityManager = super.getEntityManager();

		TypedQuery<Student> query = entityManager
				.createQuery("SELECT u FROM Student u WHERE u.status = true AND u.contest.id = :idContest ORDER BY u.score DESC ", Student.class);
		query.setParameter("idContest", contest.getId());
		return query.getResultList();
	}

	public List<Student> getByEmailAndPassword(Student student) {
		EntityManager entityManager = super.getEntityManager();
		TypedQuery<Student> query = entityManager.createQuery(
				"SELECT s FROM Student s WHERE s.email = :email AND s.password = :password", Student.class);
		query.setParameter("email", student.getEmail());
		query.setParameter("password", student.getPassword());

		return query.getResultList();
	}
	
	public Student getByToken(String token) {
		EntityManager entityManager = super.getEntityManager();
		
		TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s WHERE s.token = :token", Student.class);
		query.setParameter("token", token);
		
		return query.getSingleResult();
	}
	
	public Student getByEmail(String email) {
		EntityManager entityManager = super.getEntityManager();
		
		TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s WHERE s.email = :email", Student.class);
		query.setParameter("email", email);
		
		return query.getSingleResult();
	}
	
}