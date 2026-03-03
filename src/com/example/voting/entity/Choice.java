package com.example.voting.entity;

import javax.persistence.*;

@Entity
@Table(name = "choice", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"question_id", "user_id"})
})
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "choice_user", nullable = false)
    private String choiceUser; // значение выбора пользователя

    // Конструкторы
    public Choice() {}

    public Choice(Question question, User user, String choiceUser) {
        this.question = question;
        this.user = user;
        this.choiceUser = choiceUser;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getChoiceUser() {
        return choiceUser;
    }

    public void setChoiceUser(String choiceUser) {
        this.choiceUser = choiceUser;
    }
}