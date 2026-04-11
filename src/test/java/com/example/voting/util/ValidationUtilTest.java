package com.example.voting.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для ValidationUtil.
 * Покрывают валидацию данных голосования и пользователя.
 */
@DisplayName("ValidationUtil — тесты валидации")
class ValidationUtilTest {

    // ===== Тесты validateVote =====

    @Test
    @DisplayName("Валидация голосования — все поля корректны")
    void testValidateVote_Valid() {
        Map<String, String> errors = ValidationUtil.validateVote(
            "Тестовое голосование",
            "2026-04-01T09:00",
            "2026-04-30T23:59",
            "ACTIVE"
        );
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Валидация голосования — пустое название")
    void testValidateVote_EmptyTitle() {
        Map<String, String> errors = ValidationUtil.validateVote(
            "",
            "2026-04-01T09:00",
            "2026-04-30T23:59",
            "ACTIVE"
        );
        assertTrue(errors.containsKey("title"));
        assertTrue(errors.get("title").contains("Название голосования обязательно"));
    }

    @Test
    @DisplayName("Валидация голосования — null название")
    void testValidateVote_NullTitle() {
        Map<String, String> errors = ValidationUtil.validateVote(
            null,
            "2026-04-01T09:00",
            "2026-04-30T23:59",
            "ACTIVE"
        );
        assertTrue(errors.containsKey("title"));
    }

    @Test
    @DisplayName("Валидация голосования — название слишком длинное")
    void testValidateVote_TitleTooLong() {
        String longTitle = "A".repeat(256);
        Map<String, String> errors = ValidationUtil.validateVote(
            longTitle,
            "2026-04-01T09:00",
            "2026-04-30T23:59",
            "ACTIVE"
        );
        assertTrue(errors.containsKey("title"));
        assertTrue(errors.get("title").contains("255"));
    }

    @Test
    @DisplayName("Валидация голосования — пустая дата начала")
    void testValidateVote_EmptyDateStart() {
        Map<String, String> errors = ValidationUtil.validateVote(
            "Тест",
            "",
            "2026-04-30T23:59",
            "ACTIVE"
        );
        assertTrue(errors.containsKey("dateStart"));
    }

    @Test
    @DisplayName("Валидация голосования — null дата окончания")
    void testValidateVote_NullDateFinish() {
        Map<String, String> errors = ValidationUtil.validateVote(
            "Тест",
            "2026-04-01T09:00",
            null,
            "ACTIVE"
        );
        assertTrue(errors.containsKey("dateFinish"));
    }

    @Test
    @DisplayName("Валидация голосования — пустой статус")
    void testValidateVote_EmptyStatus() {
        Map<String, String> errors = ValidationUtil.validateVote(
            "Тест",
            "2026-04-01T09:00",
            "2026-04-30T23:59",
            ""
        );
        assertTrue(errors.containsKey("status"));
    }

    @Test
    @DisplayName("Валидация голосования — все поля пустые")
    void testValidateVote_AllEmpty() {
        Map<String, String> errors = ValidationUtil.validateVote("", "", "", "");
        assertEquals(4, errors.size());
        assertTrue(errors.containsKey("title"));
        assertTrue(errors.containsKey("dateStart"));
        assertTrue(errors.containsKey("dateFinish"));
        assertTrue(errors.containsKey("status"));
    }

    // ===== Тесты validateUser =====

    @Test
    @DisplayName("Валидация пользователя — все поля корректны")
    void testValidateUser_Valid() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "ivan@test.ru",
            "+79001234567",
            "NOT_VOTED"
        );
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Валидация пользователя — пустое имя")
    void testValidateUser_EmptyFirstName() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "",
            "Иванов",
            "ivan@test.ru",
            null,
            "NOT_VOTED"
        );
        assertTrue(errors.containsKey("firstName"));
    }

    @Test
    @DisplayName("Валидация пользователя — null фамилия")
    void testValidateUser_NullLastName() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            null,
            "ivan@test.ru",
            null,
            "NOT_VOTED"
        );
        assertTrue(errors.containsKey("lastName"));
    }

    @Test
    @DisplayName("Валидация пользователя — пустой email")
    void testValidateUser_EmptyEmail() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "",
            null,
            "NOT_VOTED"
        );
        assertTrue(errors.containsKey("email"));
    }

    @Test
    @DisplayName("Валидация пользователя — некорректный email")
    void testValidateUser_InvalidEmail() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "not-an-email",
            null,
            "NOT_VOTED"
        );
        assertTrue(errors.containsKey("email"));
        assertTrue(errors.get("email").contains("Некорректный формат"));
    }

    @Test
    @DisplayName("Валидация пользователя — корректный email с поддоменом")
    void testValidateUser_ValidSubdomainEmail() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "user@subdomain.example.com",
            null,
            "NOT_VOTED"
        );
        assertFalse(errors.containsKey("email"));
    }

    @Test
    @DisplayName("Валидация пользователя — корректный телефон")
    void testValidateUser_ValidPhone() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "ivan@test.ru",
            "+79001234567",
            "NOT_VOTED"
        );
        assertFalse(errors.containsKey("phone"));
    }

    @Test
    @DisplayName("Валидация пользователя — некорректный телефон")
    void testValidateUser_InvalidPhone() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "ivan@test.ru",
            "abc123",
            "NOT_VOTED"
        );
        assertTrue(errors.containsKey("phone"));
        assertTrue(errors.get("phone").contains("Некорректный формат"));
    }

    @Test
    @DisplayName("Валидация пользователя — пустой телефон (допустимо)")
    void testValidateUser_EmptyPhone() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "ivan@test.ru",
            "",
            "NOT_VOTED"
        );
        assertFalse(errors.containsKey("phone"));
    }

    @Test
    @DisplayName("Валидация пользователя — null телефон (допустимо)")
    void testValidateUser_NullPhone() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "ivan@test.ru",
            null,
            "NOT_VOTED"
        );
        assertFalse(errors.containsKey("phone"));
    }

    @Test
    @DisplayName("Валидация пользователя — пустой статус")
    void testValidateUser_EmptyStatus() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "ivan@test.ru",
            null,
            ""
        );
        assertTrue(errors.containsKey("status"));
    }

    @Test
    @DisplayName("Валидация пользователя — все поля пустые")
    void testValidateUser_AllEmpty() {
        Map<String, String> errors = ValidationUtil.validateUser("", "", "", "", "");
        assertTrue(errors.containsKey("firstName"));
        assertTrue(errors.containsKey("lastName"));
        assertTrue(errors.containsKey("email"));
        assertTrue(errors.containsKey("status"));
    }

    @Test
    @DisplayName("Валидация пользователя — email с плюсом")
    void testValidateUser_EmailWithPlus() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "user+tag@example.com",
            null,
            "NOT_VOTED"
        );
        assertFalse(errors.containsKey("email"));
    }

    @Test
    @DisplayName("Валидация пользователя — короткий телефон")
    void testValidateUser_PhoneTooShort() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "ivan@test.ru",
            "+123",
            "NOT_VOTED"
        );
        assertTrue(errors.containsKey("phone"));
    }

    @Test
    @DisplayName("Валидация пользователя — телефон только цифры 10 символов")
    void testValidateUser_PhoneMinLength() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "ivan@test.ru",
            "1234567890",
            "NOT_VOTED"
        );
        assertFalse(errors.containsKey("phone"));
    }

    @Test
    @DisplayName("Валидация пользователя — телефон 15 символов")
    void testValidateUser_PhoneMaxLength() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван",
            "Иванов",
            "ivan@test.ru",
            "123456789012345",
            "NOT_VOTED"
        );
        assertFalse(errors.containsKey("phone"));
    }
}
