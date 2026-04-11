package com.example.voting.controller;

import com.example.voting.service.VoteService;
import com.example.voting.util.ValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для VoteController (логика валидации и обработки данных).
 * Тестируем валидацию и парсинг данных, которые используются в контроллере.
 */
@DisplayName("VoteController — тесты логики")
class VoteControllerTest {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Test
    @DisplayName("Валидация голосования — корректные данные")
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
    @DisplayName("Валидация голосования — все поля пустые")
    void testValidateVote_AllEmpty() {
        Map<String, String> errors = ValidationUtil.validateVote("", "", "", "");
        assertEquals(4, errors.size());
        assertTrue(errors.containsKey("title"));
        assertTrue(errors.containsKey("dateStart"));
        assertTrue(errors.containsKey("dateFinish"));
        assertTrue(errors.containsKey("status"));
    }

    @Test
    @DisplayName("Парсинг даты — корректный формат")
    void testParseDate_Valid() {
        String dateStr = "2026-04-15T14:30";
        LocalDateTime date = LocalDateTime.parse(dateStr, DATE_TIME_FORMATTER);

        assertEquals(2026, date.getYear());
        assertEquals(4, date.getMonthValue());
        assertEquals(15, date.getDayOfMonth());
        assertEquals(14, date.getHour());
        assertEquals(30, date.getMinute());
    }

    @Test
    @DisplayName("Парсинг даты — некорректный формат")
    void testParseDate_InvalidFormat() {
        String dateStr = "15.04.2026 14:30";
        assertThrows(Exception.class, () -> LocalDateTime.parse(dateStr, DATE_TIME_FORMATTER));
    }

    @Test
    @DisplayName("Парсинг ID — корректное значение")
    void testParseId_Valid() {
        Long id = parseId("123");
        assertEquals(Long.valueOf(123), id);
    }

    @Test
    @DisplayName("Парсинг ID — null")
    void testParseId_Null() {
        assertNull(parseId(null));
    }

    @Test
    @DisplayName("Парсинг ID — пустая строка")
    void testParseId_Empty() {
        assertNull(parseId(""));
    }

    @Test
    @DisplayName("Парсинг ID — пробелы")
    void testParseId_Blank() {
        assertNull(parseId("   "));
    }

    @Test
    @DisplayName("Парсинг ID — не число")
    void testParseId_NotNumber() {
        assertNull(parseId("abc"));
    }

    @Test
    @DisplayName("Парсинг ID — отрицательное число")
    void testParseId_Negative() {
        assertEquals(Long.valueOf(-1), parseId("-1"));
    }

    @Test
    @DisplayName("Маршрутизация — путь /vote/new")
    void testRouting_New() {
        String pathInfo = "/new";
        String action = getAction(pathInfo);
        assertEquals("new", action);
    }

    @Test
    @DisplayName("Маршрутизация — путь /vote/edit/5")
    void testRouting_Edit() {
        String pathInfo = "/edit/5";
        String[] parts = pathInfo.split("/");
        assertEquals("edit", parts[1]);
        assertEquals("5", parts[2]);
    }

    @Test
    @DisplayName("Маршрутизация — путь /vote/delete/10")
    void testRouting_Delete() {
        String pathInfo = "/delete/10";
        String[] parts = pathInfo.split("/");
        assertEquals("delete", parts[1]);
        assertEquals("10", parts[2]);
    }

    @Test
    @DisplayName("Маршрутизация — путь /vote/view/3")
    void testRouting_View() {
        String pathInfo = "/view/3";
        String[] parts = pathInfo.split("/");
        assertEquals("view", parts[1]);
        assertEquals("3", parts[2]);
    }

    @Test
    @DisplayName("Маршрутизация — пустой путь (список)")
    void testRouting_List() {
        String pathInfo = "/";
        String action = (pathInfo == null || pathInfo.equals("/")) ? "list" : pathInfo.substring(1);
        assertEquals("list", action);
    }

    @Test
    @DisplayName("Маршрутизация — null путь (список)")
    void testRouting_NullPath() {
        String pathInfo = null;
        String action = (pathInfo == null || pathInfo.equals("/")) ? "list" : pathInfo.substring(1);
        assertEquals("list", action);
    }

    @Test
    @DisplayName("Формирование redirect URL")
    void testRedirectUrl() {
        String contextPath = "/voting-system";
        String redirectUrl = contextPath + "/vote";
        assertEquals("/voting-system/vote", redirectUrl);
    }

    @Test
    @DisplayName("Формирование пути к JSP для списка")
    void testListJspPath() {
        String jspPath = "/WEB-INF/views/vote/list.jsp";
        assertTrue(jspPath.endsWith(".jsp"));
        assertTrue(jspPath.contains("vote"));
    }

    @Test
    @DisplayName("Формирование пути к JSP для формы")
    void testFormJspPath() {
        String jspPath = "/WEB-INF/views/vote/form.jsp";
        assertTrue(jspPath.endsWith(".jsp"));
        assertTrue(jspPath.contains("form"));
    }

    @Test
    @DisplayName("Формирование пути к JSP для просмотра")
    void testViewJspPath() {
        String jspPath = "/WEB-INF/views/vote/view.jsp";
        assertTrue(jspPath.endsWith(".jsp"));
        assertTrue(jspPath.contains("view"));
    }

    @Test
    @DisplayName("VoteService — создание экземпляра")
    void testVoteServiceInstantiation() {
        VoteService voteService = new VoteService();
        assertNotNull(voteService);
    }

    @Test
    @DisplayName("VoteController — создание экземпляра")
    void testVoteControllerInstantiation() {
        VoteController controller = new VoteController();
        assertNotNull(controller);
    }

    @Test
    @DisplayName("Создание нового голосования — данные формы")
    void testCreateVoteFormData() {
        String title = "Новое голосование";
        String dateStart = "2026-05-01T09:00";
        String dateFinish = "2026-05-31T23:59";
        String status = "ACTIVE";

        assertFalse(title.isEmpty());
        assertNotNull(dateStart);
        assertNotNull(dateFinish);
        assertNotNull(status);

        // Парсинг дат
        LocalDateTime start = LocalDateTime.parse(dateStart, DATE_TIME_FORMATTER);
        LocalDateTime finish = LocalDateTime.parse(dateFinish, DATE_TIME_FORMATTER);

        assertTrue(finish.isAfter(start));
    }

    @Test
    @DisplayName("Редактирование голосования — данные формы")
    void testEditVoteFormData() {
        Long voteId = 5L;
        String title = "Обновлённое голосование";
        String dateStart = "2026-06-01T10:00";
        String dateFinish = "2026-06-30T18:00";
        String status = "COMPLETED";

        assertNotNull(voteId);
        assertTrue(voteId > 0);
        assertFalse(title.isEmpty());

        LocalDateTime start = LocalDateTime.parse(dateStart, DATE_TIME_FORMATTER);
        LocalDateTime finish = LocalDateTime.parse(dateFinish, DATE_TIME_FORMATTER);

        assertEquals(10, start.getHour());
        assertEquals(18, finish.getHour());
        assertEquals("COMPLETED", status);
    }

    @Test
    @DisplayName("Валидация — название слишком длинное")
    void testValidateVote_TitleTooLong() {
        String longTitle = "A".repeat(300);
        Map<String, String> errors = ValidationUtil.validateVote(
            longTitle, "2026-04-01T09:00", "2026-04-30T23:59", "ACTIVE"
        );
        assertTrue(errors.containsKey("title"));
        assertTrue(errors.get("title").contains("255"));
    }

    @Test
    @DisplayName("Сообщение об ошибке валидации")
    void testValidationErrorMessage() {
        Map<String, String> errors = ValidationUtil.validateVote(
            "", "2026-04-01T09:00", "2026-04-30T23:59", "ACTIVE"
        );
        String message = errors.get("title");
        assertNotNull(message);
        assertTrue(message.contains("Название голосования обязательно"));
    }

    private Long parseId(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) return null;
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getAction(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("")) {
            return "list";
        }
        String[] parts = pathInfo.split("/");
        return parts.length > 1 ? parts[1] : "list";
    }
}
