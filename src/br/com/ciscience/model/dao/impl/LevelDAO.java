package br.com.ciscience.model.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Contest;
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
	
	public List<Level> listForName(String name) {
		EntityManager entityManager = super.getEntityManager();
		TypedQuery<Level> query = entityManager.createQuery("SELECT u FROM Level u WHERE u.name LIKE :name", Level.class);
		query.setParameter("name", "%" + name + "%");

		return query.getResultList();
	}

}