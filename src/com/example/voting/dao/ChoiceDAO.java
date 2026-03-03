package com.example.voting.dao;

import com.example.voting.entity.Choice;
import com.example.voting.util.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class ChoiceDAO {

    public Choice findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Choice.class, id);
        } finally {
            em.close();
        }
    }

    public List<Choice> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Choice> query = em.createQuery("SELECT c FROM Choice c ORDER BY c.id", Choice.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Choice> findByQuestionId(Long questionId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Choice> query = em.createQuery(
                "SELECT c FROM Choice c WHERE c.question.id = :questionId ORDER BY c.id", Choice.class);
            query.setParameter("questionId", questionId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Choice> findByUserId(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Choice> query = em.createQuery(
                "SELECT c FROM Choice c WHERE c.user.id = :userId ORDER BY c.id", Choice.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Choice findByQuestionAndUser(Long questionId, Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Choice> query = em.createQuery(
                "SELECT c FROM Choice c WHERE c.question.id = :questionId AND c.user.id = :userId", 
                Choice.class);
            query.setParameter("questionId", questionId);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void save(Choice choice) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (choice.getId() == null) {
                em.persist(choice);
            } else {
                em.merge(choice);
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
            Choice choice = em.find(Choice.class, id);
            if (choice != null) {
                em.remove(choice);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public long countByQuestion(Long questionId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(c) FROM Choice c WHERE c.question.id = :questionId", Long.class);
            query.setParameter("questionId", questionId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}