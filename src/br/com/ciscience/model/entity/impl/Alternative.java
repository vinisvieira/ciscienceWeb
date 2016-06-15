package br.com.ciscience.model.entity.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.ciscience.model.entity.IEntity;

@Entity
public class Alternative implements IEntity {

	private Long id;
	private String text;
	private boolean answer;
	private boolean status;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isAnswer() {
		return answer;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public boolean validateEmptyFields() {
		if (getText() == null || getText().trim().equals("")) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Alternative [id=" + id + ", text=" + text + ", answer=" + answer + ", status=" + status + "]";
	}

}
