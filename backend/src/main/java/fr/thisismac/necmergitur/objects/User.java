package fr.thisismac.necmergitur.objects;

import java.util.List;
import java.util.UUID;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor 
public class User {
	
	public UUID								id;
	public String 							name;
	public short							age;
	public String							adress;
	public String							phone;
	public String							mail;
	public String							last_geo;
	public String							password;
	public AvailableState					available;
	public StatusState						status;
	public String							registerId;
	public List<Degree> 					degrees;
	
	public enum AvailableState {
		NO_AVAILABLE,
		AVAILABLE,
		ONLY_EMERGENCY;
	}
	
	public enum StatusState {
		WAITING_ORDER_STUFF,
		WAITING_ORDER,
		ENGAGED,
		NONE;
	}
}

