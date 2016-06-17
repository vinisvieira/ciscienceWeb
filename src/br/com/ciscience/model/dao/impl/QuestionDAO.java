package br.com.ciscience.model.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Question;
import br.com.ciscience.util.Constants;

public class QuestionDAO extends GenericDAO<Long, Question> {

	public QuestionDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}
}
