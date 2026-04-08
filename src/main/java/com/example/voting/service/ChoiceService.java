package com.example.voting.service;

import com.example.voting.dao.ChoiceDAO;
import com.example.voting.dao.QuestionDAO;
import com.example.voting.dao.UserDAO;
import com.example.voting.entity.Choice;
import com.example.voting.entity.Question;
import com.example.voting.entity.User;
import java.util.List;

public class ChoiceService {
    private final ChoiceDAO choiceDAO;
    private final QuestionDAO questionDAO;
    private final UserDAO userDAO;
    private final UserService userService;

    public ChoiceService() {
        this.choiceDAO = new ChoiceDAO();
        this.questionDAO = new QuestionDAO();
        this.userDAO = new UserDAO();
        this.userService = new UserService(userDAO);
    }

    public ChoiceService(ChoiceDAO choiceDAO, QuestionDAO questionDAO, UserDAO userDAO) {
        this.choiceDAO = choiceDAO;
        this.questionDAO = questionDAO;
        this.userDAO = userDAO;
        this.userService = new UserService(userDAO);
    }

    public List<Choice> getAllChoices() {
        return choiceDAO.findAll();
    }

    public Choice getChoice(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID выбора не может быть null");
        }
        Choice choice = choiceDAO.findById(id);
        if (choice == null) {
            throw new IllegalArgumentException("Выбор с ID " + id + " не найден");
        }
        return choice;
    }

    public List<Choice> getChoicesByQuestion(Long questionId) {
        if (questionId == null) {
            throw new IllegalArgumentException("ID вопроса не может быть null");
        }
        return choiceDAO.findByQuestionId(questionId);
    }

    public List<Choice> getChoicesByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }
        return choiceDAO.findByUserId(userId);
    }

    public Choice getChoiceByQuestionAndUser(Long questionId, Long userId) {
        if (questionId == null || userId == null) {
            throw new IllegalArgumentException("ID вопроса и пользователя не могут быть null");
        }
        return choiceDAO.findByQuestionAndUser(questionId, userId);
    }

    public void saveChoice(Choice choice, Long questionId, Long userId) {
        if (choice == null) {
            throw new IllegalArgumentException("Выбор не может быть null");
        }
        if (questionId == null || userId == null) {
            throw new IllegalArgumentException("ID вопроса и пользователя не могут быть null");
        }

        Question question = questionDAO.findById(questionId);
        if (question == null) {
            throw new IllegalArgumentException("Вопрос с ID " + questionId + " не найден");
        }

        User user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с ID " + userId + " не найден");
        }

        // Проверка, не голосовал ли уже пользователь за этот вопрос
        Choice existing = choiceDAO.findByQuestionAndUser(questionId, userId);
        if (existing != null) {
            throw new IllegalArgumentException("Пользователь уже голосовал за этот вопрос");
        }

        choice.setQuestion(question);
        choice.setUser(user);
        validateChoice(choice);

        choiceDAO.save(choice);

        // Обновляем статус пользователя (если ранее не голосовал)
        if (!"Голосовал".equals(user.getStatus())) {
            userService.markUserAsVoted(userId);
        }
    }

    public void updateChoice(Choice choice) {
        if (choice == null || choice.getId() == null) {
            throw new IllegalArgumentException("Выбор и его ID не могут быть null");
        }
        Choice existing = choiceDAO.findById(choice.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Выбор с ID " + choice.getId() + " не найден");
        }
        // Сохраняем связи из существующей записи
        choice.setQuestion(existing.getQuestion());
        choice.setUser(existing.getUser());
        validateChoice(choice);
        choiceDAO.save(choice);
    }

    public void deleteChoice(Long id) {
        Choice choice = getChoice(id); // проверка существования
        Long userId = choice.getUser().getId();

        choiceDAO.delete(id);

        // Если у пользователя больше нет голосов, меняем статус на "Не голосовал"
        List<Choice> remainingChoices = choiceDAO.findByUserId(userId);
        if (remainingChoices.isEmpty()) {
            userService.markUserAsNotVoted(userId);
        }
    }

    public long getVoteCountForQuestion(Long questionId) {
        if (questionId == null) {
            throw new IllegalArgumentException("ID вопроса не может быть null");
        }
        return choiceDAO.countByQuestion(questionId);
    }

    private void validateChoice(Choice choice) {
        if (choice.getChoiceUser() == null || choice.getChoiceUser().trim().isEmpty()) {
            throw new IllegalArgumentException("Значение выбора обязательно");
        }
        if (choice.getQuestion() == null) {
            throw new IllegalArgumentException("Выбор должен быть связан с вопросом");
        }
        if (choice.getUser() == null) {
            throw new IllegalArgumentException("Выбор должен быть связан с пользователем");
        }
    }
}