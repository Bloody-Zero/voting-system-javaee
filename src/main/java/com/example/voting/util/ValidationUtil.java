package com.example.voting.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static Map<String, String> validateVote(String title, String dateStart, String dateFinish, String status) {
        Map<String, String> errors = new HashMap<>();

        if (title == null || title.trim().isEmpty()) {
            errors.put("title", "Название голосования обязательно");
        } else if (title.length() > 255) {
            errors.put("title", "Название не должно превышать 255 символов");
        }

        if (dateStart == null || dateStart.trim().isEmpty()) {
            errors.put("dateStart", "Дата начала обязательна");
        }

        if (dateFinish == null || dateFinish.trim().isEmpty()) {
            errors.put("dateFinish", "Дата окончания обязательна");
        }

        if (status == null || status.trim().isEmpty()) {
            errors.put("status", "Статус обязателен");
        }

        return errors;
    }

    public static Map<String, String> validateUser(String firstName, String lastName, String email, String phone, String status) {
        Map<String, String> errors = new HashMap<>();

        if (firstName == null || firstName.trim().isEmpty()) {
            errors.put("firstName", "Имя обязательно");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            errors.put("lastName", "Фамилия обязательна");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email обязателен");
        } else if (!isValidEmail(email)) {
            errors.put("email", "Некорректный формат email");
        }

        if (phone != null && !phone.trim().isEmpty() && !isValidPhone(phone)) {
            errors.put("phone", "Некорректный формат телефона");
        }

        if (status == null || status.trim().isEmpty()) {
            errors.put("status", "Статус обязателен");
        }

        return errors;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private static boolean isValidPhone(String phone) {
        String phoneRegex = "^\\+?[0-9]{10,15}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phone).matches();
    }
}