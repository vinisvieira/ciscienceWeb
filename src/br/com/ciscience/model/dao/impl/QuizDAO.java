package br.com.ciscience.model.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Quiz;
import br.com.ciscience.model.entity.impl.Student;

public class QuizDAO extends GenericDAO<Long, Quiz> {

	public QuizDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

	public boolean dateVerification(Quiz quiz) {
		EntityManager entityManager = super.getEntityManager();

		Query query = entityManager
				.createQuery("SELECT u FROM Quiz u WHERE u.student.id = :id_student AND date = :date ");
		query.setParameter("id_student", quiz.getStudent().getId());
		query.setParameter("date", quiz.getDate());
		return (query.getResultList().size() > 0);
	}

}