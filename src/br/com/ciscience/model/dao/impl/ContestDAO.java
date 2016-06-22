package br.com.ciscience.model.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Contest;

public class ContestDAO extends GenericDAO<Long, Contest>{

	public ContestDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}
	public boolean nameExists(String name) {
		EntityManager entityManager = super.getEntityManager();

		Query query = entityManager.createQuery("SELECT u FROM Contest u WHERE u.name = :name");
		query.setParameter("name", name);
		return (query.getResultList().size() > 0);
	}

}
