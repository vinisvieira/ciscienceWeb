package br.com.ciscience.model.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Student;

public class StudentDAO extends GenericDAO<Long, Student> {

	public StudentDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

	public boolean emailExists(Student student) {
		EntityManager entityManager = super.getEntityManager();

		Query query = entityManager
				.createQuery("SELECT u FROM Student u WHERE u.email = :email");
		query.setParameter("email", student.getEmail());
		return (query.getResultList().size() > 0);
	}

	public boolean studentExists(Student student) {
		EntityManager entityManager = super.getEntityManager();

		Query query = entityManager
				.createQuery("SELECT u FROM Student u WHERE u.id = :id");
		query.setParameter("id", student.getId());
		return (query.getResultList().size() > 0);
	}

	public List<Student> listarPorEmail(String email) {
		EntityManager entityManager = super.getEntityManager();
		TypedQuery<Student> query = entityManager
				.createQuery("SELECT u FROM Student u WHERE u.email = :email",
						Student.class);
		query.setParameter("email", email);

		return query.getResultList();
	}

}