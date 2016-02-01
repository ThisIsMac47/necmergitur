package fr.thisismac.necmergitur.objects;

import java.util.UUID;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor 
public class Degree {
	
	public UUID				id;
	public String 			name;
}
