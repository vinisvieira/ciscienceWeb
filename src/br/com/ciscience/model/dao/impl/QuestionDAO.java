package br.com.ciscience.model.dao.impl;

import javax.persistence.EntityManager;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Question;

public class QuestionDAO extends GenericDAO<Long, Question> {

	public QuestionDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

}
