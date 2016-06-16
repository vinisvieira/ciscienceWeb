package br.com.ciscience.model.dao.impl;

import javax.persistence.EntityManager;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Contest;

public class ContestDAO extends GenericDAO<Long, Contest>{

	public ContestDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

}
