package br.com.ciscience.model.dao.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ciscience.model.dao.GenericDAO;
import br.com.ciscience.model.entity.impl.User;
import br.com.ciscience.util.StringUtil;

public class UserDAO extends GenericDAO<Long, User> {

	public UserDAO(EntityManager entityManager) {
		super(entityManager);
	}

	/**
	 * Retorna um {@link User} por email e senha
	 * 
	 * @param user
	 * @return {@link List}
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public List<User> getByEmailAndPassword(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		EntityManager entityManagger = super.getEntityManager();

		String emailReceived = user.getEmail();
		String passwordReceived = StringUtil.SHA1(user.getPassword());

		// Lista que será retornada no metódo
		List<User> returnListForMethod = new ArrayList<User>();
		
		String myQuery = "SELECT u FROM "+ user.getClass().getSimpleName().toString() +" u WHERE u.email = :email and u.password = :password";
		
		Query query = entityManagger.createQuery(myQuery);
		query.setParameter("email", emailReceived);
		query.setParameter("password", passwordReceived);
		List<User> resultList = (List<User>) query.getResultList();

		if (!resultList.isEmpty()) {
			returnListForMethod.addAll(resultList);
		}

		return returnListForMethod;

	}

	@SuppressWarnings("unchecked")
	public List<User> getByEmail(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		EntityManager entityManagger = super.getEntityManager();

		String emailReceived = user.getEmail();
		System.out.println("Email received -> " + emailReceived);
		// Lista que será retornada no metódo
		List<User> returnListForMethod = new ArrayList<User>();

		// Administrador
		Query query = entityManagger.createQuery("SELECT u FROM Administrador u WHERE u.email = :email");
		query.setParameter("email", emailReceived);
		List<User> resultListAdministrator = (List<User>) query.getResultList();

		if (!resultListAdministrator.isEmpty()) {
			returnListForMethod.addAll(resultListAdministrator);
		}

		return returnListForMethod;

	}

}
