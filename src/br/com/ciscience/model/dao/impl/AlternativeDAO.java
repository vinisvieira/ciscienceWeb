package br.com.ciscience.model.dao.impl;

import javax.persistence.EntityManager;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Alternative;

public class AlternativeDAO extends GenericDAO<Long, Alternative> {

	public AlternativeDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

}
