package com.example.voting.service;

import com.example.voting.dao.UserDAO;
import com.example.voting.entity.User;
import java.util.List;
import java.util.regex.Pattern;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public User getUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }
        User user = userDAO.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с ID " + id + " не найден");
        }
        return user;
    }

    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        return userDAO.findByEmail(email);
    }

    public void saveUser(User user) {
        validateUser(user);
        // Проверка уникальности email
        User existing = userDAO.findByEmail(user.getEmail());
        if (existing != null && !existing.getId().equals(user.getId())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        userDAO.save(user);
    }

    public void deleteUser(Long id) {
        getUser(id); // проверка существования
        userDAO.delete(id);
    }

    public void updateUserStatus(Long id, String status) {
        User user = getUser(id);
        user.setStatus(status);
        userDAO.save(user);
    }

    public void markUserAsVoted(Long id) {
        updateUserStatus(id, "Голосовал");
    }

    public void markUserAsNotVoted(Long id) {
        updateUserStatus(id, "Не голосовал");
    }

    public List<User> getUsersByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Статус не может быть пустым");
        }
        return userDAO.findByStatus(status);
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null");
        }
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя обязательно");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Фамилия обязательна");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email обязателен");
        }
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Некорректный формат email");
        }
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty() && !isValidPhone(user.getPhone())) {
            throw new IllegalArgumentException("Некорректный формат телефона");
        }
        if (user.getStatus() == null || user.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Статус обязателен");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^\\+?[0-9]{10,15}$";
        return Pattern.compile(phoneRegex).matcher(phone).matches();
    }
}