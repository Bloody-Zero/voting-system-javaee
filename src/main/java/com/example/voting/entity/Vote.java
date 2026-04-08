package com.example.voting.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "date_start", nullable = false)
    private LocalDateTime dateStart;

    @Column(name = "date_finish", nullable = false)
    private LocalDateTime dateFinish;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    public Vote() {}

    public Vote(String title, LocalDateTime dateStart, LocalDateTime dateFinish, String status) {
        this.title = title;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.status = status;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDateTime getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(LocalDateTime dateFinish) {
        this.dateFinish = dateFinish;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    // Методы форматирования дат для JSP
    public String getDateStartFormatted() {
        if (dateStart == null) return "";
        return dateStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    public String getDateFinishFormatted() {
        if (dateFinish == null) return "";
        return dateFinish.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}