package com.example.voting.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "date_vote", nullable = false)
    private LocalDateTime dateVote;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Choice> choices;

    // Конструкторы
    public Question() {}

    public Question(Vote vote, String content, LocalDateTime dateVote) {
        this.vote = vote;
        this.content = content;
        this.dateVote = dateVote;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateVote() {
        return dateVote;
    }

    public void setDateVote(LocalDateTime dateVote) {
        this.dateVote = dateVote;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}