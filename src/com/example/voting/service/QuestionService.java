package com.example.voting.service;

import com.example.voting.dao.QuestionDAO;
import com.example.voting.dao.VoteDAO;
import com.example.voting.entity.Question;
import com.example.voting.entity.Vote;
import java.util.List;

public class QuestionService {
    private final QuestionDAO questionDAO;
    private final VoteDAO voteDAO;

    public QuestionService() {
        this.questionDAO = new QuestionDAO();
        this.voteDAO = new VoteDAO();
    }

    public QuestionService(QuestionDAO questionDAO, VoteDAO voteDAO) {
        this.questionDAO = questionDAO;
        this.voteDAO = voteDAO;
    }

    public List<Question> getAllQuestions() {
        return questionDAO.findAll();
    }

    public Question getQuestion(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID вопроса не может быть null");
        }
        Question question = questionDAO.findById(id);
        if (question == null) {
            throw new IllegalArgumentException("Вопрос с ID " + id + " не найден");
        }
        return question;
    }

    public List<Question> getQuestionsByVote(Long voteId) {
        if (voteId == null) {
            throw new IllegalArgumentException("ID голосования не может быть null");
        }
        return questionDAO.findByVoteId(voteId);
    }

    public void saveQuestion(Question question, Long voteId) {
        if (question == null) {
            throw new IllegalArgumentException("Вопрос не может быть null");
        }
        if (voteId == null) {
            throw new IllegalArgumentException("ID голосования не может быть null");
        }
        Vote vote = voteDAO.findById(voteId);
        if (vote == null) {
            throw new IllegalArgumentException("Голосование с ID " + voteId + " не найдено");
        }
        question.setVote(vote);
        validateQuestion(question);
        questionDAO.save(question);
    }

    public void updateQuestion(Question question) {
        if (question == null || question.getId() == null) {
            throw new IllegalArgumentException("Вопрос и его ID не могут быть null");
        }
        // Проверяем существование вопроса
        Question existing = questionDAO.findById(question.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Вопрос с ID " + question.getId() + " не найден");
        }
        // Сохраняем связь с голосованием из существующей записи
        question.setVote(existing.getVote());
        validateQuestion(question);
        questionDAO.save(question);
    }

    public void deleteQuestion(Long id) {
        getQuestion(id); // проверка существования
        questionDAO.delete(id);
    }

    private void validateQuestion(Question question) {
        if (question.getContent() == null || question.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Содержание вопроса обязательно");
        }
        if (question.getDateVote() == null) {
            throw new IllegalArgumentException("Дата голосования обязательна");
        }
        if (question.getVote() == null) {
            throw new IllegalArgumentException("Вопрос должен быть связан с голосованием");
        }
    }
}