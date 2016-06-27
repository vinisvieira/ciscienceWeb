package br.com.ciscience.model.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.QuizStudent;
import br.com.ciscience.model.entity.impl.Student;

public class QuizStudentDAO extends GenericDAO<Long, QuizStudent> {

	public QuizStudentDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

	public List<QuizStudent> getByStudent(Student student) {
		EntityManager entityManager = super.getEntityManager();

		TypedQuery<QuizStudent> query = entityManager
				.createQuery("SELECT u FROM QuizStudent u WHERE u.student.id = :idStudent", QuizStudent.class);
		query.setParameter("idStudent", student.getId());
		return (query.getResultList());
	}

}
