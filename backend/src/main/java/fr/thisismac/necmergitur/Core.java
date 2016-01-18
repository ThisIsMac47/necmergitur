package fr.thisismac.necmergitur;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import fr.thisismac.negmergitur.msgs.ReturnMessage;
import fr.thisismac.negmergitur.objects.Degree;
import fr.thisismac.negmergitur.objects.User;
import fr.thisismac.negmergitur.objects.User.AvailableState;
import fr.thisismac.negmergitur.objects.User.StatusState;

import static spark.Spark.*;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import lombok.Getter;

public class Core {

	// Entry point for this program
	public static void main(String[] args) {
		new Core(args);
	}
	
	// Final data
	public String SQL_USER = "";
	public String SQL_PASSWD = "";
	public String SQL_URL = "";
	public String GAPI_KEY = "google api key";
	public String SENDER_ID = "google project id";
	
	
	// Class access
	@Getter static Core instance;
	@Getter static ConnectionSource connectionSource;

	// ORM Interface
	@Getter Dao<User, String> usersDao;
	@Getter Dao<Degree, String> degreesDao;
	
	// Data caching
	@Getter List<Degree> degrees;
	@Getter List<User> users;

	// Gson instance
	public Gson gson = new GsonBuilder().serializeNulls().create();

	public Core(String[] args) {
		instance = this;

		// Init DB Driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			// Query data for caching
			connectionSource = new JdbcConnectionSource(SQL_URL, SQL_USER, SQL_PASSWD);
			usersDao = DaoManager.createDao(connectionSource, User.class);
			users = usersDao.queryForAll();
			degreesDao = DaoManager.createDao(connectionSource, Degree.class);
			degrees = degreesDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// dev mode
		//port(4242);
		
		// Log all request and ip
		before((request, response) -> {
			System.out.println("Requested : " + request.pathInfo() + " : " + request.ip());
		    response.header("Content-Encoding", "gzip");
			response.type("application/json");
		});
		
		
		/* Login an user from his username and password (and post registerId for push notification) (String username, String password)*/
		post("/users/login", (request, response) -> {
			if (request.queryMap().get("username").value() == null || request.queryMap().get("password").value() == null
					|| request.queryMap().get("registerId").value() == null)
				return gson.toJson(new ReturnMessage(false, "Fill all required field please"));
			for (User user : users) {
				if (user.getName().equalsIgnoreCase(request.queryMap().get("username").value())) {
					if (user.getPassword().equals(request.queryMap().get("password").value())) {
						user.setRegisterId(request.queryMap("registerId").value());
						usersDao.update(user);
						return gson.toJson(new ReturnMessage(true, user.getId().toString()));
					}
					else
						return gson.toJson(new ReturnMessage(false, "Bad credidentials"));
				}
			}
			return gson.toJson(new ReturnMessage(false, "User not found"));
		});

		/* Show all users */
		get("/users/all", (request, response) -> {
			JsonArray array = new JsonArray();
			for (User user : users) {
				JsonElement elem = gson.toJsonTree(user, User.class);
				array.add(elem);
			}
			return array;
		});
		
		/* Show one user by his name (String username)*/
		get("/users/:name", (request, response) -> {
			for (User user : users) {
				if (user.getName().equals(request.params(":name")))
					return gson.toJson(user);
			}
			return gson.toJson(new ReturnMessage(false, "User not found"));
		});
		
		/* Find user's degrees (String username)*/
		get("/users/:name/degrees", (request, response) -> {
			for (User user : users) {
				if (user.getName().equals(request.params(":name")))
					return gson.toJson(user.getDegrees());
			}
			return gson.toJson(new ReturnMessage(false, "User not found"));
		});
		
		
		/* Post update about user availability (String uuid, String location, int state)*/
		post("/available/update", (request, response) -> {
			if (request.queryMap().get("id").value() == null || request.queryMap().get("location").value() == null ||
					request.queryMap().get("state").value() == null)
				return gson.toJson(new ReturnMessage(false, "Fill all required field please"));
			for (User user : users) {
				if (user.getId().toString().equalsIgnoreCase(request.queryMap().get("id").value())) {
					user.setLast_geo(request.queryMap().get("location").value());
					user.setAvailable(AvailableState.values()[Integer.parseInt(request.queryMap().get("state").value())]);
					usersDao.update(user);
					return gson.toJson(new ReturnMessage(true, "Your status has been updated"));
				}
			}
			return gson.toJson(new ReturnMessage(false, "User not found"));
		});
		
		/* Request availability for this user (String uuid) */
		get("/available/request/:id", (request, response) -> {
			if (request.params(":id") == null)
				return gson.toJson(new ReturnMessage(false, "Fill all required field please"));
			for (User user : users) {
				if (user.getId().toString().equalsIgnoreCase(request.params(":id"))) {
					if (user.getAvailable().ordinal() == 0 && !user.getRegisterId().isEmpty()) {
						return gson.toJson(new ReturnMessage(false, "User is not available"));
					}
					send_push(user, "request", "Etes-vous disponible pour être engagé sur une mission ?");
					
					return gson.toJson(new ReturnMessage(true, "Availability Requested"));
				}
			}
			return gson.toJson(new ReturnMessage(false, "User not found"));
		});
		
		/* Request availability from all users possible (void)*/
		get("/available/request/all", (request, response) -> {
			for (User user : users) {
				if (user.getAvailable().ordinal() != 0 && !user.getRegisterId().isEmpty()) {
					send_push(user, "request", "Etes-vous disponible pour être engagé sur une mission ?");
				}
			}
			return gson.toJson(new ReturnMessage(true, "Availability Requested"));
		});
		
		/* Show all user that are actually engaged or ready in operation (void) */
		get("/engaged/show/all", (request, response) -> {
			JsonArray array = new JsonArray();
			for (User user : users) {
				if (user.getStatus() != StatusState.NONE) {
					JsonElement elem = gson.toJsonTree(user, User.class);
					array.add(elem);
				}
			}
			return array;
		});
		
		/* Show all user that are in {filtre} status(void) */
		get("/engaged/show/:filtre", (request, response) -> {
			if (request.params(":filtre") == null || StatusState.valueOf(request.params(":filtre")) == null)
				return gson.toJson(new ReturnMessage(false, "Fill all required field please"));
			JsonArray array = new JsonArray();
			for (User user : users) {
				if (user.getStatus() == StatusState.valueOf(request.params(":filtre"))) {
					array.add(gson.toJson(user));
				}
			}
			return array;
		});
		
		/* Post response for availability (String uuid, String location, int state, boolean status) */
		post("/available/response", (request, response) -> {
			if (request.queryMap("id").value() == null || request.queryMap("status").value() == null)
				return gson.toJson(new ReturnMessage(false, "Fill all required field please"));
			for (User user : users) {
				if (user.getId().toString().equalsIgnoreCase(request.queryMap("id").value())) {
					if (request.queryMap("status").booleanValue()) {
						user.setAvailable(AvailableState.NO_AVAILABLE);
						if (request.queryMap().get("has_stuff").booleanValue())
							user.setStatus(StatusState.WAITING_ORDER_STUFF);
						else
							user.setStatus(StatusState.WAITING_ORDER);
						user.setLast_geo(request.queryMap().get("location").value());
						usersDao.update(user);
						return gson.toJson(new ReturnMessage(true, "Now, just WAITING for order."));
					} else {
						user.setAvailable(AvailableState.NO_AVAILABLE);
						usersDao.update(user);
						return gson.toJson(new ReturnMessage(true, "You are not consired anymore as available."));
					}
					
				}
			}
			return gson.toJson(new ReturnMessage(false, "User not found"));
		});
	}
	
	/* Send a push notification to an user */
	public void send_push(User user, String type, String message) {
		try {
			Sender sender = new Sender(GAPI_KEY);
			Result result = sender.send(new Message.Builder()
					.collapseKey("message")
					.timeToLive(120)
	                .delayWhileIdle(true)
					.addData("message", message)
					.addData("type", type)
					.build(), user.getRegisterId(), 3);
			System.out.println("Request avaibility on " + user.getName() + " >> messageId = " + result.getMessageId() + " && errorCode = " + result.getErrorCodeName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
