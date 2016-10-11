import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

import java.net.URI;
import java.net.URISyntaxException;

import static spark.Spark.*;
import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;
import static spark.Spark.get;

import com.heroku.sdk.jdbc.DatabaseUrl;
import spark.Request;
import spark.Response;
import spark.Route;

public class Main {

    private static boolean shouldReturnHtml(Request request) {
      String accept = request.headers("Accept");
      return accept != null && accept.contains("text/html");
    }
    private static boolean shouldReturnXml(Request request) {
      String accept = request.headers("Accept");
      return accept != null && accept.contains("text/xml");
    }
    
  public static void main(String[] args) {

    port(Integer.valueOf(System.getenv("PORT")));
    staticFileLocation("/public");

    get("/hello", (req, res) -> "Hello World");


    // get("/", (request, response) -> {
    //         Map<String, Object> attributes = new HashMap<>();
    //         attributes.put("message", "Hello World!");
    //         return new ModelAndView(attributes, "index.ftl");
    //     }, new FreeMarkerEngine());
    
    get("/", (request, response) -> {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("message", "Hello World!");
        
      if (shouldReturnHtml(request)) {
          response.status(200);
          response.type("text/html");
          return new FreeMarkerEngine().render(new ModelAndView(attributes, "index.ftl"));
      } else if (shouldReturnXml(request)) {
          response.status(200);
          response.type("text/xml");
          return null; // todo return xml
      } 
      else {
          response.status(200);
          response.type("application/json");
          return JsonUtil.toJson(attributes);
      }
  });

    get("/db", (req, res) -> {
      Connection connection = null;
      Map<String, Object> attributes = new HashMap<>();
      try {
        connection = DatabaseUrl.extract().getConnection();

        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

        ArrayList<String> output = new ArrayList<String>();
        while (rs.next()) {
          output.add( "Read from DB: " + rs.getTimestamp("tick"));
        }

        attributes.put("results", output);
        return new ModelAndView(attributes, "db.ftl");
      } catch (Exception e) {
        attributes.put("message", "There was an error: " + e);
        return new ModelAndView(attributes, "error.ftl");
      } finally {
        if (connection != null) try{connection.close();} catch(SQLException e){}
      }
    }, new FreeMarkerEngine());
    
    UserService userService = new UserService();
    
    get("/users", (req, res) -> {
        res.type("application/json");
        return userService.getAllUsers();
      }, JsonUtil.json());
    
    get("/users/:id", (req, res) -> {
      res.type("application/json");
        String id = req.params(":id");
        User user = userService.getUser(id);
        if (user != null) {
          return user;
        }
        res.status(400);
        return new ResponseError("No user with id '%s' found", id);
      }, JsonUtil.json());
    
    exception(IllegalArgumentException.class, (e, req, res) -> {
        res.status(400);
        res.body(JsonUtil.toJson(new ResponseError(e)));
      });

  }
  
  

}
