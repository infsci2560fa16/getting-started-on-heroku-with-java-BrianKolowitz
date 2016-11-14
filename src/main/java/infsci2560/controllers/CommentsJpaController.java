/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infsci2560.controllers;

import infsci2560.controllers.exceptions.NonexistentEntityException;
import infsci2560.controllers.exceptions.PreexistingEntityException;
import infsci2560.data.Comments;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import infsci2560.data.Posts;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author kolobj
 */
public class CommentsJpaController implements Serializable {

    public CommentsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comments comments) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Posts postUuid = comments.getPostUuid();
            if (postUuid != null) {
                postUuid = em.getReference(postUuid.getClass(), postUuid.getPostUuid());
                comments.setPostUuid(postUuid);
            }
            em.persist(comments);
            if (postUuid != null) {
                postUuid.getCommentsCollection().add(comments);
                postUuid = em.merge(postUuid);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findComments(comments.getCommentUuid()) != null) {
                throw new PreexistingEntityException("Comments " + comments + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comments comments) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comments persistentComments = em.find(Comments.class, comments.getCommentUuid());
            Posts postUuidOld = persistentComments.getPostUuid();
            Posts postUuidNew = comments.getPostUuid();
            if (postUuidNew != null) {
                postUuidNew = em.getReference(postUuidNew.getClass(), postUuidNew.getPostUuid());
                comments.setPostUuid(postUuidNew);
            }
            comments = em.merge(comments);
            if (postUuidOld != null && !postUuidOld.equals(postUuidNew)) {
                postUuidOld.getCommentsCollection().remove(comments);
                postUuidOld = em.merge(postUuidOld);
            }
            if (postUuidNew != null && !postUuidNew.equals(postUuidOld)) {
                postUuidNew.getCommentsCollection().add(comments);
                postUuidNew = em.merge(postUuidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Object id = comments.getCommentUuid();
                if (findComments(id) == null) {
                    throw new NonexistentEntityException("The comments with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Object id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comments comments;
            try {
                comments = em.getReference(Comments.class, id);
                comments.getCommentUuid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comments with id " + id + " no longer exists.", enfe);
            }
            Posts postUuid = comments.getPostUuid();
            if (postUuid != null) {
                postUuid.getCommentsCollection().remove(comments);
                postUuid = em.merge(postUuid);
            }
            em.remove(comments);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comments> findCommentsEntities() {
        return findCommentsEntities(true, -1, -1);
    }

    public List<Comments> findCommentsEntities(int maxResults, int firstResult) {
        return findCommentsEntities(false, maxResults, firstResult);
    }

    private List<Comments> findCommentsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comments.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Comments findComments(Object id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comments.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommentsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comments> rt = cq.from(Comments.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
