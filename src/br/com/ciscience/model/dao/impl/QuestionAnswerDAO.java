package br.com.ciscience.model.dao.impl;

import javax.persistence.EntityManager;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.QuestionAnswer;

public class QuestionAnswerDAO extends GenericDAO<Long, QuestionAnswer> {

	public QuestionAnswerDAO(EntityManager entityManager) {
		super(entityManager);
	}
	
	

}
