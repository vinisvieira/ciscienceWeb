package br.com.ciscience.model.entity.impl;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Contest {

	private Long id;
	private String name;
	private boolean status;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Contest [id=" + id + ", name=" + name + ", status=" + status + "]";
	}

	public boolean validateEmptyFields() {
		if (getName() == null || getName().trim().equals("")) {
			return false;
		}
		return true;
	}

}
