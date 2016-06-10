package br.com.ciscience.model.jpa;

import javax.persistence.EntityManager;

public interface IJPAUtil {
	
	public void beginTransaction();
	public void commit();
	public void close();
	public void rollBack();
	public EntityManager getEntityManager();

}
