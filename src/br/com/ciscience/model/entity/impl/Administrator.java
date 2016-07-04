package br.com.ciscience.model.entity.impl;

import javax.persistence.Entity;

/**
 * Classe representativa da entidade Administrador
 * 
 * @author PedroFelipe
 * 
 */
@Entity
public class Administrator extends User {

	public Administrator() {

	}

	public Administrator(Long id) {
		setId(id);
	}

	public Administrator(Long id, String name, String email, boolean status) {
		setId(id);
		setName(name);
		setEmail(email);
		setStatus(status);
	}

	public Administrator(Long id, String name, String email, String password, boolean status) {
		setId(id);
		setName(name);
		setEmail(email);
		setPassword(password);
		setStatus(status);
	}

	public Administrator(String name, String email, String password, boolean status) {
		setName(name);
		setEmail(email);
		setPassword(password);
		setStatus(status);
	}

	@Override
	public boolean validateEmptyFields() {
		return super.validateEmptyFields();
	}

	@Override
	public boolean passwordsMatch(String password, String confirmPassword) {
		return super.passwordsMatch(password, confirmPassword);
	}

}
