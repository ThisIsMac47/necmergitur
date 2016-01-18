package fr.thisismac.negmergitur.objects;

import java.util.Collection;
import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor @DatabaseTable(tableName = "users")
public class User {
	@DatabaseField(columnName = "id", id = true)
	public UUID							id;
				
	@DatabaseField(columnName = "name")
	public String 						name;
	
	@DatabaseField(columnName = "age")
	public short						age;
	
	@DatabaseField(columnName = "adress")
	public String						adress;
	
	@DatabaseField(columnName = "phone")
	public String						phone;
	
	@DatabaseField(columnName = "mail")
	public String						mail;
	
	@DatabaseField(columnName = "last_geo")
	public String						last_geo;
	
	@DatabaseField(columnName = "password")
	public transient String				password;

	@DatabaseField(columnName = "available")
	public AvailableState				available;
	
	@DatabaseField(columnName = "status")
	public StatusState					status;
	
	@DatabaseField(columnName = "registerId")
	public String						registerId;
	
	@ForeignCollectionField
	private Collection<Degree> 	degrees;
	
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

