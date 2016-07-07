package br.com.ciscience.model.dao.impl;

import javax.persistence.EntityManager;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.MyFile;

public class MyFileDAO extends GenericDAO<Long, MyFile> {

	public MyFileDAO(EntityManager entityManager) {
		super(entityManager);
	}

}
