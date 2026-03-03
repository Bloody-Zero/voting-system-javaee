package com.example.voting.dao;

import com.example.voting.entity.Question;
import com.example.voting.util.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class QuestionDAO {

    public Question findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Question.class, id);
        } finally {
            em.close();
        }
    }

    public List<Question> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery("SELECT q FROM Question q ORDER BY q.id", Question.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Question> findByVoteId(Long voteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                "SELECT q FROM Question q WHERE q.vote.id = :voteId ORDER BY q.id", Question.class);
            query.setParameter("voteId", voteId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Question question) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (question.getId() == null) {
                em.persist(question);
            } else {
                em.merge(question);
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
            Question question = em.find(Question.class, id);
            if (question != null) {
                em.remove(question);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}