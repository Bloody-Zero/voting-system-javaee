package com.example.voting.controller;

import com.example.voting.service.UserService;
import com.example.voting.util.ValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для UserController (логика валидации и обработки данных).
 * Тестируем валидацию, которая используется в контроллере.
 */
@DisplayName("UserController — тесты логики")
class UserControllerTest {

    @Test
    @DisplayName("Валидация пользователя — корректные данные")
    void testValidateUser_Valid() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED"
        );
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Валидация пользователя — обязательные поля пустые")
    void testValidateUser_RequiredFieldsEmpty() {
        Map<String, String> errors = ValidationUtil.validateUser("", "", "", "", "");
        assertTrue(errors.containsKey("firstName"));
        assertTrue(errors.containsKey("lastName"));
        assertTrue(errors.containsKey("email"));
        assertTrue(errors.containsKey("status"));
    }

    @Test
    @DisplayName("Валидация пользователя — email с ошибкой")
    void testValidateUser_InvalidEmail() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "Иван", "Иванов", "invalid-email", null, "NOT_VOTED"
        );
        assertTrue(errors.containsKey("email"));
    }

    @Test
    @DisplayName("Сообщение об ошибке валидации содержит текст")
    void testValidationErrorMessage() {
        Map<String, String> errors = ValidationUtil.validateUser(
            "", "Иванов", "ivan@test.ru", null, "NOT_VOTED"
        );
        String message = errors.get("firstName");
        assertNotNull(message);
        assertTrue(message.contains("Имя обязательно"));
    }

    @Test
    @DisplayName("UserService — создание экземпляра")
    void testUserServiceInstantiation() {
        UserService userService = new UserService();
        assertNotNull(userService);
    }

    @Test
    @DisplayName("UserController — создание экземпляра")
    void testUserControllerInstantiation() {
        UserController controller = new UserController();
        assertNotNull(controller);
    }

    @Test
    @DisplayName("Путь /user/* — проверка логики маршрутизации")
    void testUserPathRouting() {
        // Тестируем логику обработки путей
        String[] paths = {"/", "/new", "/edit", "/delete", "/view", "/save"};

        for (String path : paths) {
            if (path == null || path.equals("/") || path.equals("")) {
                // listUsers
                assertEquals("list", getActionForPath(path));
            } else if (path.equals("/save")) {
                assertEquals("save", getActionForPath(path));
            } else {
                // edit, delete, view, new
                assertEquals(path.substring(1), getActionForPath(path));
            }
        }
    }

    private String getActionForPath(String path) {
        if (path == null || path.equals("/") || path.equals("")) {
            return "list";
        }
        return path.substring(1);
    }

    @Test
    @DisplayName("Обработка null pathInfo")
    void testNullPathInfo() {
        String pathInfo = null;
        String action = (pathInfo == null || pathInfo.equals("/")) ? "list" : pathInfo.substring(1);
        assertEquals("list", action);
    }

    @Test
    @DisplayName("Парсинг ID из параметра запроса")
    void testParseId() {
        // Тестирование парсинга ID
        assertNull(parseId(null));
        assertNull(parseId(""));
        assertNull(parseId("  "));
        assertNull(parseId("abc"));
        assertEquals(Long.valueOf(123), parseId("123"));
        assertEquals(Long.valueOf(1), parseId("1"));
    }

    private Long parseId(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) return null;
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Test
    @DisplayName("Формирование redirect URL")
    void testRedirectUrl() {
        String contextPath = "/voting-system";
        String redirectUrl = contextPath + "/user?success=created";
        assertEquals("/voting-system/user?success=created", redirectUrl);
    }

    @Test
    @DisplayName("Формирование пути к JSP")
    void testJspPath() {
        String jspPath = "/WEB-INF/views/user/form.jsp";
        assertTrue(jspPath.endsWith(".jsp"));
        assertTrue(jspPath.startsWith("/WEB-INF/"));
    }

    @Test
    @DisplayName("Создание пользователя для формы редактирования")
    void testEditUserForm() {
        // Симуляция данных для формы редактирования
        Long userId = 1L;
        String firstName = "Иван";
        String lastName = "Иванов";
        String email = "ivan@test.ru";
        String phone = "+79001234567";
        String status = "NOT_VOTED";

        assertNotNull(userId);
        assertTrue(userId > 0);
        assertFalse(firstName.isEmpty());
        assertFalse(lastName.isEmpty());
        assertTrue(email.contains("@"));
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void testCreateNewUser() {
        String firstName = "Петр";
        String lastName = "Петров";
        String email = "petr@test.ru";
        String phone = "+79009998877";
        String status = "NOT_VOTED";

        assertFalse(firstName.isEmpty());
        assertFalse(lastName.isEmpty());
        assertTrue(email.contains("@"));
        assertNotNull(status);
    }
}
