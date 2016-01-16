package fr.thisismac.necmergitur_back;

import java.util.Map;

import org.rapidoid.annotation.Controller;
import org.rapidoid.annotation.GET;
import fr.thisismac.negmergitur_back.objects.User;

@Controller
public class UsersWrapper {
	
	@GET
	public User users(Map<String, String> params) {
		for(User user : Core.getInstance().getUsers()) {
			if (params.containsKey("name") && user.getName().equals(params.get("name")))
				return user;
			else if (params.containsKey("id") && user.getId() == Integer.parseInt(params.get("id")))
				return user;
			else if (params.containsKey("phone") && user.getName().equals(params.get("phone")))
				return user;
			else if (params.containsKey("mail") && user.getName().equals(params.get("mail")))
				return user;
		}
		return null;
	}
	
}
