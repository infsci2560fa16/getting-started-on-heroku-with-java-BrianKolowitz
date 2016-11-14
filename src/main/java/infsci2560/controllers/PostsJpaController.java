/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infsci2560.controllers;

import infsci2560.controllers.exceptions.NonexistentEntityException;
import infsci2560.controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import infsci2560.data.Comments;
import infsci2560.data.Posts;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author kolobj
 */
public class PostsJpaController implements Serializable {

    public PostsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Posts posts) throws PreexistingEntityException, Exception {
        if (posts.getCommentsCollection() == null) {
            posts.setCommentsCollection(new ArrayList<Comments>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Comments> attachedCommentsCollection = new ArrayList<Comments>();
            for (Comments commentsCollectionCommentsToAttach : posts.getCommentsCollection()) {
                commentsCollectionCommentsToAttach = em.getReference(commentsCollectionCommentsToAttach.getClass(), commentsCollectionCommentsToAttach.getCommentUuid());
                attachedCommentsCollection.add(commentsCollectionCommentsToAttach);
            }
            posts.setCommentsCollection(attachedCommentsCollection);
            em.persist(posts);
            for (Comments commentsCollectionComments : posts.getCommentsCollection()) {
                Posts oldPostUuidOfCommentsCollectionComments = commentsCollectionComments.getPostUuid();
                commentsCollectionComments.setPostUuid(posts);
                commentsCollectionComments = em.merge(commentsCollectionComments);
                if (oldPostUuidOfCommentsCollectionComments != null) {
                    oldPostUuidOfCommentsCollectionComments.getCommentsCollection().remove(commentsCollectionComments);
                    oldPostUuidOfCommentsCollectionComments = em.merge(oldPostUuidOfCommentsCollectionComments);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPosts(posts.getPostUuid()) != null) {
                throw new PreexistingEntityException("Posts " + posts + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Posts posts) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Posts persistentPosts = em.find(Posts.class, posts.getPostUuid());
            Collection<Comments> commentsCollectionOld = persistentPosts.getCommentsCollection();
            Collection<Comments> commentsCollectionNew = posts.getCommentsCollection();
            Collection<Comments> attachedCommentsCollectionNew = new ArrayList<Comments>();
            for (Comments commentsCollectionNewCommentsToAttach : commentsCollectionNew) {
                commentsCollectionNewCommentsToAttach = em.getReference(commentsCollectionNewCommentsToAttach.getClass(), commentsCollectionNewCommentsToAttach.getCommentUuid());
                attachedCommentsCollectionNew.add(commentsCollectionNewCommentsToAttach);
            }
            commentsCollectionNew = attachedCommentsCollectionNew;
            posts.setCommentsCollection(commentsCollectionNew);
            posts = em.merge(posts);
            for (Comments commentsCollectionOldComments : commentsCollectionOld) {
                if (!commentsCollectionNew.contains(commentsCollectionOldComments)) {
                    commentsCollectionOldComments.setPostUuid(null);
                    commentsCollectionOldComments = em.merge(commentsCollectionOldComments);
                }
            }
            for (Comments commentsCollectionNewComments : commentsCollectionNew) {
                if (!commentsCollectionOld.contains(commentsCollectionNewComments)) {
                    Posts oldPostUuidOfCommentsCollectionNewComments = commentsCollectionNewComments.getPostUuid();
                    commentsCollectionNewComments.setPostUuid(posts);
                    commentsCollectionNewComments = em.merge(commentsCollectionNewComments);
                    if (oldPostUuidOfCommentsCollectionNewComments != null && !oldPostUuidOfCommentsCollectionNewComments.equals(posts)) {
                        oldPostUuidOfCommentsCollectionNewComments.getCommentsCollection().remove(commentsCollectionNewComments);
                        oldPostUuidOfCommentsCollectionNewComments = em.merge(oldPostUuidOfCommentsCollectionNewComments);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Object id = posts.getPostUuid();
                if (findPosts(id) == null) {
                    throw new NonexistentEntityException("The posts with id " + id + " no longer exists.");
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
            Posts posts;
            try {
                posts = em.getReference(Posts.class, id);
                posts.getPostUuid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The posts with id " + id + " no longer exists.", enfe);
            }
            Collection<Comments> commentsCollection = posts.getCommentsCollection();
            for (Comments commentsCollectionComments : commentsCollection) {
                commentsCollectionComments.setPostUuid(null);
                commentsCollectionComments = em.merge(commentsCollectionComments);
            }
            em.remove(posts);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Posts> findPostsEntities() {
        return findPostsEntities(true, -1, -1);
    }

    public List<Posts> findPostsEntities(int maxResults, int firstResult) {
        return findPostsEntities(false, maxResults, firstResult);
    }

    private List<Posts> findPostsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Posts.class));
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

    public Posts findPosts(Object id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Posts.class, id);
        } finally {
            em.close();
        }
    }

    public int getPostsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Posts> rt = cq.from(Posts.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
