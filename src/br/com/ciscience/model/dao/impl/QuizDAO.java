package br.com.ciscience.model.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Quiz;

public class QuizDAO extends GenericDAO<Long, Quiz> {

	public QuizDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

	public List<Quiz> getByDate(Date date) {
		EntityManager entityManager = super.getEntityManager();
			
		TypedQuery<Quiz> query = entityManager.createQuery(
				"SELECT u FROM Quiz u WHERE u.date = :date", Quiz.class);
		query.setParameter("date", date);
		return query.getResultList();
	}
}