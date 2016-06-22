package br.com.ciscience.model.entity.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Student extends User {

	private List<Quiz> quiz;
	private Long score;

	@ManyToMany
	public List<Quiz> getQuiz() {
		return quiz;
	}

	public void setQuiz(List<Quiz> quiz) {
		this.quiz = quiz;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Student [quiz=" + quiz + ", score=" + score + "]";
	}

}
