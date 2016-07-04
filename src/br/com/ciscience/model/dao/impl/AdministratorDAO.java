package br.com.ciscience.model.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.Administrator;

public class AdministratorDAO extends GenericDAO<Long, Administrator> {

	public AdministratorDAO(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
	}

	public boolean emailExists(Administrator administrator) {
		EntityManager entityManager = super.getEntityManager();

		Query query = entityManager.createQuery("SELECT u FROM Administrator u WHERE u.email = :email");
		query.setParameter("email", administrator.getEmail());
		return (query.getResultList().size() > 0);
	}

}
