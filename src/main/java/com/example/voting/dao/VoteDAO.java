package com.example.voting.dao;

import com.example.voting.entity.Vote;
import com.example.voting.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class VoteDAO {

    public Vote findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Vote.class, id);
        } finally {
            em.close();
        }
    }

    public List<Vote> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Vote> query = em.createQuery("SELECT v FROM Vote v ORDER BY v.id", Vote.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    public Vote findByIdWithQuestions(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Vote> query = em.createQuery(
                "SELECT v FROM Vote v LEFT JOIN FETCH v.questions WHERE v.id = :id", Vote.class);
            query.setParameter("id", id);
            List<Vote> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }
    public List<Vote> findByStatus(String status) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Vote> query = em.createQuery(
                "SELECT v FROM Vote v WHERE v.status = :status ORDER BY v.id", Vote.class);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Vote vote) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (vote.getId() == null) {
                em.persist(vote);
            } else {
                em.merge(vote);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Vote vote = em.find(Vote.class, id);
            if (vote != null) {
                em.remove(vote);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}