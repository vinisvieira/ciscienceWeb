package br.com.ciscience.model.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Level;
import br.com.ciscience.util.Constants;

public class LevelDAO extends GenericDAO<Long, Level> {

	public LevelDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

	public boolean levelExist(String name) {
		EntityManager entityManager = super.getEntityManager();
		Query query = entityManager
				.createQuery("SELECT u FROM Level u WHERE u.name = :name AND u.status = :status");
		query.setParameter("name", name);
		query.setParameter("status", Constants.ACTIVE_ENTITY);
		return (query.getResultList().size() > 0);
	}

}