package fr.thisismac.necmergitur_back;

import java.util.List;
import org.rapidoid.web.Rapidoid;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import fr.thisismac.negmergitur_back.objects.User;
import lombok.Getter;

public class Core {
	
	public static void main(String[] args) {
		new Core(args);
	}
	
	// Data access
	@Getter static Core instance;
	@Getter static Sql2o db;
	
	// Data cache
	@Getter List<User> users;
	
	public Core(String[] args) {
		instance = this;
		
		// Init DB
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		db = new Sql2o("jdbc:mysql://craftwb.fr:3306/backdb", "sql_necmergitur", "FWAmX4DA7DbzCesr");
		
		// Query data for caching
		try (Connection con = db.open()) {
	       users = con.createQuery("SELECT * FROM users").executeAndFetch(User.class);
	    }
		Rapidoid.run(args);
	}
}
