package com.example.voting.service;

import com.example.voting.dao.VoteDAO;
import com.example.voting.entity.Question;
import com.example.voting.entity.Vote;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteDAO voteDAO;

    private VoteService voteService;

    @BeforeEach
    void setUp() {
        voteService = new VoteService(voteDAO);
    }

    @Test
    void testGetAllVotes() {
        Vote vote1 = new Vote("Голосование 1", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote1.setId(1L);
        Vote vote2 = new Vote("Голосование 2", LocalDateTime.now(), LocalDateTime.now().plusDays(14), "ACTIVE");
        vote2.setId(2L);

        when(voteDAO.findAll()).thenReturn(Arrays.asList(vote1, vote2));

        List<Vote> votes = voteService.getAllVotes();
        assertEquals(2, votes.size());
        assertEquals("Голосование 1", votes.get(0).getTitle());
        assertEquals("Голосование 2", votes.get(1).getTitle());

        verify(voteDAO).findAll();
    }

    @Test
    void testGetVote_Success() {
        Vote vote = new Vote("Тестовое голосование", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);

        when(voteDAO.findById(1L)).thenReturn(vote);

        Vote found = voteService.getVote(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Тестовое голосование", found.getTitle());

        verify(voteDAO).findById(1L);
    }

    @Test
    void testGetVote_NullId() {
        assertThrows(IllegalArgumentException.class, () -> voteService.getVote(null));
    }

    @Test
    void testGetVote_NotFound() {
        when(voteDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> voteService.getVote(999L));
    }

    @Test
    void testGetVoteWithQuestions_Success() {
        Vote vote = new Vote("Голосование с вопросами", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);

        Question q1 = new Question(vote, "Вопрос 1", LocalDateTime.now());
        q1.setId(1L);
        Question q2 = new Question(vote, "Вопрос 2", LocalDateTime.now());
        q2.setId(2L);
        vote.setQuestions(Arrays.asList(q1, q2));

        when(voteDAO.findByIdWithQuestions(1L)).thenReturn(vote);

        Vote found = voteService.getVoteWithQuestions(1L);
        assertNotNull(found);
        assertEquals(2, found.getQuestions().size());

        verify(voteDAO).findByIdWithQuestions(1L);
    }

    @Test
    void testGetVoteWithQuestions_NotFound() {
        when(voteDAO.findByIdWithQuestions(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> voteService.getVoteWithQuestions(999L));
    }

    @Test
    void testGetVoteWithQuestions_NullId() {
        assertThrows(IllegalArgumentException.class, () -> voteService.getVoteWithQuestions(null));
    }

    @Test
    void testSaveVote_Success() {
        Vote vote = new Vote("Новое голосование",
                             LocalDateTime.now(),
                             LocalDateTime.now().plusDays(7),
                             "ACTIVE");

        voteService.saveVote(vote);

        verify(voteDAO).save(vote);
    }

    @Test
    void testSaveVote_EmptyTitle() {
        Vote vote = new Vote("", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
    }

    @Test
    void testSaveVote_NullDateStart() {
        Vote vote = new Vote("Тест", null, LocalDateTime.now().plusDays(7), "ACTIVE");

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
    }

    @Test
    void testSaveVote_NullDateFinish() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), null, "ACTIVE");

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
    }

    @Test
    void testSaveVote_StartAfterFinish() {
        Vote vote = new Vote("Тест",
                             LocalDateTime.now().plusDays(10),
                             LocalDateTime.now(),
                             "ACTIVE");

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
    }

    @Test
    void testSaveVote_NullStatus() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), null);

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
    }

    @Test
    void testSaveVote_NullVote() {
        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(null));
    }

    @Test
    void testDeleteVote_Success() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        when(voteDAO.findById(1L)).thenReturn(vote);

        voteService.deleteVote(1L);

        verify(voteDAO).delete(1L);
    }

    @Test
    void testDeleteVote_NullId() {
        assertThrows(IllegalArgumentException.class, () -> voteService.deleteVote(null));
    }

    @Test
    void testDeleteVote_NotFound() {
        when(voteDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> voteService.deleteVote(999L));
    }

    @Test
    void testGetVotesByStatus() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        when(voteDAO.findByStatus("ACTIVE")).thenReturn(List.of(vote));

        List<Vote> votes = voteService.getVotesByStatus("ACTIVE");
        assertEquals(1, votes.size());
        assertEquals("ACTIVE", votes.get(0).getStatus());

        verify(voteDAO).findByStatus("ACTIVE");
    }

    @Test
    void testGetVotesByStatus_EmptyStatus() {
        assertThrows(IllegalArgumentException.class, () -> voteService.getVotesByStatus(""));
        assertThrows(IllegalArgumentException.class, () -> voteService.getVotesByStatus(null));
    }

    @Test
    void testUpdateVoteStatus() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        when(voteDAO.findById(1L)).thenReturn(vote);

        voteService.updateVoteStatus(1L, "COMPLETED");

        assertEquals("COMPLETED", vote.getStatus());
        verify(voteDAO).save(vote);
    }

    @Test
    void testVoteDateHelpers() {
        LocalDateTime start = LocalDateTime.of(2026, 4, 1, 9, 0);
        LocalDateTime finish = LocalDateTime.of(2026, 4, 30, 23, 59);
        Vote vote = new Vote("Тест", start, finish, "ACTIVE");

        assertEquals("2026-04-01T09:00", vote.getDateStartFormatted());
        assertEquals("2026-04-30T23:59", vote.getDateFinishFormatted());
    }
}
