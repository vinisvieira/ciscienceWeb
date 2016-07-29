package br.com.ciscience.model.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Contest;
import br.com.ciscience.model.entity.impl.Quiz;

public class QuizDAO extends GenericDAO<Long, Quiz> {

	public QuizDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

	public List<Quiz> getByDateAndContest(Date date, Contest contest) {
		EntityManager entityManager = super.getEntityManager();

		TypedQuery<Quiz> query = entityManager.createQuery("SELECT u FROM Quiz u WHERE u.date = :date AND u.contest.id = :idContest", Quiz.class);
		query.setParameter("date", date);
		query.setParameter("idContest", contest.getId());
		
		
		return query.getResultList();
	}
}