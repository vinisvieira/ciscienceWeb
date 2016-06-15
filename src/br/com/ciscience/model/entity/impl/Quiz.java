package br.com.ciscience.model.entity.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Quiz {

	private Long id;
	private Student student;
	private List<QuestionAnswer> questionAnswers;
	private Date date;
	private int score;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "id_student")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@OneToMany
	public List<QuestionAnswer> getQuestionAnswers() {
		return questionAnswers;
	}

	public void setQuestionAnswers(List<QuestionAnswer> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Quiz [id=" + id + ", student=" + student + ", questionAnswers=" + questionAnswers + ", date=" + date
				+ ", score=" + score + "]";
	}

}
