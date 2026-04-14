package com.example.voting.service;

import com.example.voting.dao.VoteDAO;
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
        Vote vote1 = new Vote("Выборы 2024", LocalDateTime.of(2024, 1, 1, 0, 0),
                              LocalDateTime.of(2024, 12, 31, 23, 59), "ACTIVE");
        vote1.setId(1L);
        Vote vote2 = new Vote("Референдум", LocalDateTime.of(2024, 6, 1, 0, 0),
                              LocalDateTime.of(2024, 6, 30, 23, 59), "COMPLETED");
        vote2.setId(2L);

        when(voteDAO.findAll()).thenReturn(Arrays.asList(vote1, vote2));

        List<Vote> votes = voteService.getAllVotes();

        assertNotNull(votes);
        assertEquals(2, votes.size());
        verify(voteDAO).findAll();
    }

    @Test
    void testGetVote_Success() {
        Vote vote = new Vote("Выборы 2024", LocalDateTime.of(2024, 1, 1, 0, 0),
                             LocalDateTime.of(2024, 12, 31, 23, 59), "ACTIVE");
        vote.setId(1L);

        when(voteDAO.findById(1L)).thenReturn(vote);

        Vote found = voteService.getVote(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Выборы 2024", found.getTitle());
        verify(voteDAO).findById(1L);
    }

    @Test
    void testGetVote_NullId() {
        assertThrows(IllegalArgumentException.class, () -> voteService.getVote(null));
        verify(voteDAO, never()).findById(any());
    }

    @Test
    void testGetVote_NotFound() {
        when(voteDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> voteService.getVote(999L));
    }

    @Test
    void testSaveVote_Success() {
        Vote vote = new Vote("Новое голосование", LocalDateTime.of(2024, 1, 1, 0, 0),
                             LocalDateTime.of(2024, 12, 31, 23, 59), "ACTIVE");

        voteService.saveVote(vote);

        verify(voteDAO).save(vote);
    }

    @Test
    void testSaveVote_NullVote() {
        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(null));
        verify(voteDAO, never()).save(any());
    }

    @Test
    void testSaveVote_EmptyTitle() {
        Vote vote = new Vote("", LocalDateTime.of(2024, 1, 1, 0, 0),
                             LocalDateTime.of(2024, 12, 31, 23, 59), "ACTIVE");

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
        verify(voteDAO, never()).save(any());
    }

    @Test
    void testSaveVote_NullTitle() {
        Vote vote = new Vote(null, LocalDateTime.of(2024, 1, 1, 0, 0),
                             LocalDateTime.of(2024, 12, 31, 23, 59), "ACTIVE");

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
        verify(voteDAO, never()).save(any());
    }

    @Test
    void testSaveVote_NullDateStart() {
        Vote vote = new Vote("Голосование", null,
                             LocalDateTime.of(2024, 12, 31, 23, 59), "ACTIVE");

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
        verify(voteDAO, never()).save(any());
    }

    @Test
    void testSaveVote_NullDateFinish() {
        Vote vote = new Vote("Голосование", LocalDateTime.of(2024, 1, 1, 0, 0),
                             null, "ACTIVE");

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
        verify(voteDAO, never()).save(any());
    }

    @Test
    void testSaveVote_DateStartAfterDateFinish() {
        Vote vote = new Vote("Голосование", LocalDateTime.of(2024, 12, 31, 23, 59),
                             LocalDateTime.of(2024, 1, 1, 0, 0), "ACTIVE");

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
        verify(voteDAO, never()).save(any());
    }

    @Test
    void testSaveVote_EmptyStatus() {
        Vote vote = new Vote("Голосование", LocalDateTime.of(2024, 1, 1, 0, 0),
                             LocalDateTime.of(2024, 12, 31, 23, 59), "");

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
        verify(voteDAO, never()).save(any());
    }

    @Test
    void testSaveVote_NullStatus() {
        Vote vote = new Vote("Голосование", LocalDateTime.of(2024, 1, 1, 0, 0),
                             LocalDateTime.of(2024, 12, 31, 23, 59), null);

        assertThrows(IllegalArgumentException.class, () -> voteService.saveVote(vote));
        verify(voteDAO, never()).save(any());
    }

    @Test
    void testDeleteVote_Success() {
        Vote vote = new Vote("Выборы 2024", LocalDateTime.of(2024, 1, 1, 0, 0),
                             LocalDateTime.of(2024, 12, 31, 23, 59), "ACTIVE");
        vote.setId(1L);

        when(voteDAO.findById(1L)).thenReturn(vote);

        voteService.deleteVote(1L);

        verify(voteDAO).findById(1L);
        verify(voteDAO).delete(1L);
    }

    @Test
    void testDeleteVote_NullId() {
        assertThrows(IllegalArgumentException.class, () -> voteService.deleteVote(null));
        verify(voteDAO, never()).delete(any());
    }

    @Test
    void testDeleteVote_NotFound() {
        when(voteDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> voteService.deleteVote(999L));
        verify(voteDAO, never()).delete(any());
    }

    @Test
    void testGetVotesByStatus_Success() {
        Vote vote1 = new Vote("Выборы 2024", LocalDateTime.of(2024, 1, 1, 0, 0),
                              LocalDateTime.of(2024, 12, 31, 23, 59), "ACTIVE");
        vote1.setId(1L);
        Vote vote2 = new Vote("Референдум", LocalDateTime.of(2024, 6, 1, 0, 0),
                              LocalDateTime.of(2024, 6, 30, 23, 59), "ACTIVE");
        vote2.setId(2L);

        when(voteDAO.findByStatus("ACTIVE")).thenReturn(Arrays.asList(vote1, vote2));

        List<Vote> votes = voteService.getVotesByStatus("ACTIVE");

        assertNotNull(votes);
        assertEquals(2, votes.size());
        verify(voteDAO).findByStatus("ACTIVE");
    }

    @Test
    void testGetVotesByStatus_EmptyStatus() {
        assertThrows(IllegalArgumentException.class, () -> voteService.getVotesByStatus(""));
        assertThrows(IllegalArgumentException.class, () -> voteService.getVotesByStatus(null));
    }

    @Test
    void testGetVoteWithQuestions_Success() {
        Vote vote = new Vote("Выборы 2024", LocalDateTime.of(2024, 1, 1, 0, 0),
                             LocalDateTime.of(2024, 12, 31, 23, 59), "ACTIVE");
        vote.setId(1L);

        when(voteDAO.findByIdWithQuestions(1L)).thenReturn(vote);

        Vote found = voteService.getVoteWithQuestions(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(voteDAO).findByIdWithQuestions(1L);
    }

    @Test
    void testGetVoteWithQuestions_NullId() {
        assertThrows(IllegalArgumentException.class, () -> voteService.getVoteWithQuestions(null));
    }

    @Test
    void testGetVoteWithQuestions_NotFound() {
        when(voteDAO.findByIdWithQuestions(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> voteService.getVoteWithQuestions(999L));
    }

    @Test
    void testUpdateVoteStatus_Success() {
        Vote vote = new Vote("Выборы 2024", LocalDateTime.of(2024, 1, 1, 0, 0),
                             LocalDateTime.of(2024, 12, 31, 23, 59), "ACTIVE");
        vote.setId(1L);

        when(voteDAO.findById(1L)).thenReturn(vote);

        voteService.updateVoteStatus(1L, "COMPLETED");

        assertEquals("COMPLETED", vote.getStatus());
        verify(voteDAO).save(vote);
    }

    @Test
    void testUpdateVoteStatus_VoteNotFound() {
        when(voteDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> voteService.updateVoteStatus(999L, "COMPLETED"));
        verify(voteDAO, never()).save(any());
    }
}
