
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kolobj
 */
public class UserService {
    ArrayList<User> db = new ArrayList<User>();
    
    public UserService() {
        User user = new User();
        user.setId("1866d959-4a52-4409-afc8-4f09896f38b2");
        user.setEmail("john");
        user.setName("john@foobar.com");
        db.add(user);

        user = new User();
        user.setId("90d965ad-5bdf-455d-9808-c38b72a5181a");
        user.setEmail("anna");
        user.setName("anna@foobar.com");
        db.add(user);

        user = new User();
        user.setId("fdfsdfsd-5bdf-455d-9808-c38b72a5181a");
        user.setEmail("brian");
        user.setName("brian@foobar.com");
        db.add(user);
    }
    
    // returns a list of all users
  public List<User> getAllUsers() { return db; }
   
  // returns a single user by id
  public User getUser(String id) { return db.stream().filter(u -> u.getId().compareTo(id) == 0).findFirst().get(); }
 
  // creates a new user
  public User createUser(String name, String email) { return new User(); }
 
  // updates an existing user
  public User updateUser(String id, String name, String email) { return new User(); }
    
}
