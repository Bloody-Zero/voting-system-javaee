package com.example.voting.service;

import com.example.voting.dao.QuestionDAO;
import com.example.voting.dao.VoteDAO;
import com.example.voting.entity.Question;
import com.example.voting.entity.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тесты для QuestionService.
 * Покрывают все основные операции: CRUD, валидацию, поиск.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionService — тесты")
class QuestionServiceTest {

    @Mock
    private QuestionDAO questionDAO;

    @Mock
    private VoteDAO voteDAO;

    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        questionService = new QuestionService(questionDAO, voteDAO);
    }

    @Test
    @DisplayName("Получение всех вопросов")
    void testGetAllQuestions() {
        Vote vote = new Vote("Тестовое голосование", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);

        Question q1 = new Question(vote, "Вопрос 1", LocalDateTime.now());
        q1.setId(1L);
        Question q2 = new Question(vote, "Вопрос 2", LocalDateTime.now());
        q2.setId(2L);

        when(questionDAO.findAll()).thenReturn(Arrays.asList(q1, q2));

        List<Question> questions = questionService.getAllQuestions();
        assertEquals(2, questions.size());
        assertEquals("Вопрос 1", questions.get(0).getContent());
        assertEquals("Вопрос 2", questions.get(1).getContent());

        verify(questionDAO).findAll();
    }

    @Test
    @DisplayName("Получение вопроса по ID — успешно")
    void testGetQuestion_Success() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Тестовый вопрос", LocalDateTime.now());
        question.setId(1L);

        when(questionDAO.findById(1L)).thenReturn(question);

        Question found = questionService.getQuestion(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Тестовый вопрос", found.getContent());

        verify(questionDAO).findById(1L);
    }

    @Test
    @DisplayName("Получение вопроса — null ID")
    void testGetQuestion_NullId() {
        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestion(null));
    }

    @Test
    @DisplayName("Получение вопроса — не найден")
    void testGetQuestion_NotFound() {
        when(questionDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestion(999L));
    }

    @Test
    @DisplayName("Получение вопроса с выборами — успешно")
    void testGetQuestionWithChoices_Success() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос с выборами", LocalDateTime.now());
        question.setId(1L);

        when(questionDAO.findByIdWithChoices(1L)).thenReturn(question);

        Question found = questionService.getQuestionWithChoices(1L);
        assertNotNull(found);
        assertEquals("Вопрос с выборами", found.getContent());

        verify(questionDAO).findByIdWithChoices(1L);
    }

    @Test
    @DisplayName("Получение вопроса с выборами — null ID")
    void testGetQuestionWithChoices_NullId() {
        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestionWithChoices(null));
    }

    @Test
    @DisplayName("Получение вопросов по голосованию")
    void testGetQuestionsByVote() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question q1 = new Question(vote, "Вопрос 1", LocalDateTime.now());
        Question q2 = new Question(vote, "Вопрос 2", LocalDateTime.now());

        when(questionDAO.findByVoteId(1L)).thenReturn(Arrays.asList(q1, q2));

        List<Question> questions = questionService.getQuestionsByVote(1L);
        assertEquals(2, questions.size());

        verify(questionDAO).findByVoteId(1L);
    }

    @Test
    @DisplayName("Получение вопросов по голосованию — null ID")
    void testGetQuestionsByVote_NullId() {
        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestionsByVote(null));
    }

    @Test
    @DisplayName("Сохранение вопроса — успешно")
    void testSaveQuestion_Success() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(null, "Новый вопрос", LocalDateTime.now());

        when(voteDAO.findById(1L)).thenReturn(vote);

        questionService.saveQuestion(question, 1L);

        verify(questionDAO).save(question);
        assertEquals(vote, question.getVote());
    }

    @Test
    @DisplayName("Сохранение вопроса — null вопрос")
    void testSaveQuestion_NullQuestion() {
        assertThrows(IllegalArgumentException.class, () -> questionService.saveQuestion(null, 1L));
    }

    @Test
    @DisplayName("Сохранение вопроса — null voteId")
    void testSaveQuestion_NullVoteId() {
        Question question = new Question(null, "Вопрос", LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> questionService.saveQuestion(question, null));
    }

    @Test
    @DisplayName("Сохранение вопроса — голосование не найдено")
    void testSaveQuestion_VoteNotFound() {
        Question question = new Question(null, "Вопрос", LocalDateTime.now());
        when(voteDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> questionService.saveQuestion(question, 999L));
    }

    @Test
    @DisplayName("Сохранение вопроса — пустое содержание")
    void testSaveQuestion_EmptyContent() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "", LocalDateTime.now());

        when(voteDAO.findById(1L)).thenReturn(vote);

        assertThrows(IllegalArgumentException.class, () -> questionService.saveQuestion(question, 1L));
    }

    @Test
    @DisplayName("Сохранение вопроса — null дата")
    void testSaveQuestion_NullDate() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос без даты", null);

        when(voteDAO.findById(1L)).thenReturn(vote);

        assertThrows(IllegalArgumentException.class, () -> questionService.saveQuestion(question, 1L));
    }

    @Test
    @DisplayName("Обновление вопроса — успешно")
    void testUpdateQuestion_Success() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question existing = new Question(vote, "Старый текст", LocalDateTime.now());
        existing.setId(1L);
        Question updated = new Question(vote, "Новый текст", LocalDateTime.now());
        updated.setId(1L);

        when(questionDAO.findById(1L)).thenReturn(existing);

        questionService.updateQuestion(updated);

        verify(questionDAO).save(updated);
    }

    @Test
    @DisplayName("Обновление вопроса — null или без ID")
    void testUpdateQuestion_NullId() {
        Question question = new Question(null, "Вопрос", LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> questionService.updateQuestion(question));
    }

    @Test
    @DisplayName("Обновление вопроса — не найден")
    void testUpdateQuestion_NotFound() {
        Question question = new Question(null, "Вопрос", LocalDateTime.now());
        question.setId(999L);
        when(questionDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> questionService.updateQuestion(question));
    }

    @Test
    @DisplayName("Удаление вопроса — успешно")
    void testDeleteQuestion_Success() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        question.setId(1L);

        when(questionDAO.findById(1L)).thenReturn(question);

        questionService.deleteQuestion(1L);

        verify(questionDAO).delete(1L);
    }

    @Test
    @DisplayName("Удаление вопроса — не найден")
    void testDeleteQuestion_NotFound() {
        when(questionDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> questionService.deleteQuestion(999L));
    }

    @Test
    @DisplayName("Валидация — содержание только из пробелов")
    void testSaveQuestion_BlankContent() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "   ", LocalDateTime.now());

        when(voteDAO.findById(1L)).thenReturn(vote);

        assertThrows(IllegalArgumentException.class, () -> questionService.saveQuestion(question, 1L));
    }

    @Test
    @DisplayName("Валидация — вопрос без голосования")
    void testSaveQuestion_NoVote() {
        Question question = new Question(null, "Вопрос", LocalDateTime.now());

        // voteDAO.findById не замокан, поэтому vote будет null
        when(voteDAO.findById(anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> questionService.saveQuestion(question, 1L));
    }
}
