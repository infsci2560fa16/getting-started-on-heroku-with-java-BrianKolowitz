/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helloworld.controllers;

import infsci2560.controllers.PostsJpaController;
import infsci2560.data.Posts;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kolobj
 */
public class PostsJpaControllerTest {
    
//    public PostsJpaControllerTest() {
//    }
//    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }
//
//    /**
//     * Test of getEntityManager method, of class PostsJpaController.
//     */
//    @Test
//    public void testGetEntityManager() {
//        System.out.println("getEntityManager");
//        PostsJpaController instance = null;
//        EntityManager expResult = null;
//        EntityManager result = instance.getEntityManager();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of create method, of class PostsJpaController.
//     */
//    @Test
//    public void testCreate() throws Exception {
//        System.out.println("create");
//        Posts posts = null;
//        PostsJpaController instance = null;
//        instance.create(posts);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of edit method, of class PostsJpaController.
//     */
//    @Test
//    public void testEdit() throws Exception {
//        System.out.println("edit");
//        Posts posts = null;
//        PostsJpaController instance = null;
//        instance.edit(posts);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of destroy method, of class PostsJpaController.
//     */
//    @Test
//    public void testDestroy() throws Exception {
//        System.out.println("destroy");
//        Object id = null;
//        PostsJpaController instance = null;
//        instance.destroy(id);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of findPostsEntities method, of class PostsJpaController.
     */
    @Test
    public void testFindPostsEntities_0args() {
        System.out.println("findPostsEntities");
        PostsJpaController instance = new PostsJpaController(
                Persistence.createEntityManagerFactory("infsci2560.data.BlogPU"));
        List<Posts> expResult = null;
        List<Posts> result = instance.findPostsEntities();
        assertNotEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

//    /**
//     * Test of findPostsEntities method, of class PostsJpaController.
//     */
//    @Test
//    public void testFindPostsEntities_int_int() {
//        System.out.println("findPostsEntities");
//        int maxResults = 0;
//        int firstResult = 0;
//        PostsJpaController instance = null;
//        List<Posts> expResult = null;
//        List<Posts> result = instance.findPostsEntities(maxResults, firstResult);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findPosts method, of class PostsJpaController.
//     */
//    @Test
//    public void testFindPosts() {
//        System.out.println("findPosts");
//        Object id = null;
//        PostsJpaController instance = null;
//        Posts expResult = null;
//        Posts result = instance.findPosts(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getPostsCount method, of class PostsJpaController.
//     */
//    @Test
//    public void testGetPostsCount() {
//        System.out.println("getPostsCount");
//        PostsJpaController instance = null;
//        int expResult = 0;
//        int result = instance.getPostsCount();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
