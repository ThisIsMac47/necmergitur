package fr.thisismac.negmergitur_back.objects;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor
public class User {
	public int		id;
	public String 	name;
	public String	phone;
	public String	mail;
}
