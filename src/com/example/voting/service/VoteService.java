package com.example.voting.service;

import com.example.voting.dao.VoteDAO;
import com.example.voting.entity.Vote;
import java.util.List;

public class VoteService {
    private final VoteDAO voteDAO;

    public VoteService() {
        this.voteDAO = new VoteDAO();
    }

    // Для тестирования с возможностью внедрения mock
    public VoteService(VoteDAO voteDAO) {
        this.voteDAO = voteDAO;
    }

    public List<Vote> getAllVotes() {
        return voteDAO.findAll();
    }

    public Vote getVote(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID голосования не может быть null");
        }
        Vote vote = voteDAO.findById(id);
        if (vote == null) {
            throw new IllegalArgumentException("Голосование с ID " + id + " не найдено");
        }
        return vote;
    }

    public void saveVote(Vote vote) {
        validateVote(vote);
        voteDAO.save(vote);
    }

    public void deleteVote(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID голосования не может быть null");
        }
        // Проверка существования (опционально)
        getVote(id); // бросит исключение, если не найдено
        voteDAO.delete(id);
    }

    public List<Vote> getVotesByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Статус не может быть пустым");
        }
        return voteDAO.findByStatus(status);
    }

    public void updateVoteStatus(Long id, String status) {
        Vote vote = getVote(id);
        vote.setStatus(status);
        voteDAO.save(vote);
    }

    private void validateVote(Vote vote) {
        if (vote == null) {
            throw new IllegalArgumentException("Голосование не может быть null");
        }
        if (vote.getTitle() == null || vote.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Название голосования обязательно");
        }
        if (vote.getDateStart() == null) {
            throw new IllegalArgumentException("Дата начала обязательна");
        }
        if (vote.getDateFinish() == null) {
            throw new IllegalArgumentException("Дата окончания обязательна");
        }
        if (vote.getDateStart().isAfter(vote.getDateFinish())) {
            throw new IllegalArgumentException("Дата начала не может быть позже даты окончания");
        }
        if (vote.getStatus() == null || vote.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Статус обязателен");
        }
    }
}