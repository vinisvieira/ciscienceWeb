package br.com.ciscience.model.entity.impl;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Student extends User {

	private List<Quiz> quiz;
	private Contest contest;
	private Long score;
	private MyFile myFile;
	private String token;

	@ManyToMany
	public List<Quiz> getQuiz() {
		return quiz;
	}

	public void setQuiz(List<Quiz> quiz) {
		this.quiz = quiz;
	}

	@OneToOne
	@JoinColumn(name = "contest_id")
	public Contest getContest() {
		return contest;
	}

	public void setContest(Contest contest) {
		this.contest = contest;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	@OneToOne
	@JoinColumn(name = "id_file")
	public MyFile getMyFile() {
		return myFile;
	}

	public void setMyFile(MyFile myFile) {
		this.myFile = myFile;
	}

	@Column(nullable = true)
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "Student [quiz=" + quiz + ", contest=" + contest + ", score=" + score + ", myFile=" + myFile + ", token="
				+ token + "]";
	}

}