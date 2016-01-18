package fr.thisismac.negmergitur.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor @DatabaseTable(tableName = "degrees")
public class Degree {
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "user_id", foreignColumnName = "id")
	public transient  User				user;
	
	@DatabaseField(columnName = "name")
	public String 						name;
	
}
