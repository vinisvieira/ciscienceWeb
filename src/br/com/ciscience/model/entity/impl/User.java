package br.com.ciscience.model.entity.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.ciscience.model.entity.IEntity;
import br.com.ciscience.model.entity.IUser;

/**
 * Classe abstrata que representa as entidades Usuario
 * 
 * @author PedroFelipe
 *
 */

@MappedSuperclass
public class User implements IEntity, IUser {

	private Long id;
	private String name;
	private String cpf;
	private String email;
	private String password;
	private Date birthday;
	private Date userSince;
	private boolean status;
	private String profile;

	public String getCpf() {
		return cpf;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Column(unique = true, nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Temporal(TemporalType.DATE)
	public Date getUserSince() {
		return userSince;
	}

	public void setUserSince(Date userSince) {
		this.userSince = userSince;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Transient
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", cpf=" + cpf
				+ ", email=" + email + ", password=" + password + ", birthday="
				+ birthday + ", userSince=" + userSince + ", status=" + status
				+ ", profile=" + profile + "]";
	}

	@Override
	public boolean validateEmptyFields() {
		if (getEmail() == null || getEmail().trim().equals("")) {
			return false;
		} else if (getName() == null || getName().trim().equals("")) {
			return false;
		} else if (getPassword() == null || getPassword().trim().equals("")) {
			return false;
		}
		return true;
	}

	@Override
	public boolean passwordsMatch(String password, String confirmPassword) {
		// TODO Auto-generated method stub
		return (password.equals(confirmPassword));
	}
}