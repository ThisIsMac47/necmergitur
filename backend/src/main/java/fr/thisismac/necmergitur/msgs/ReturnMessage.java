package fr.thisismac.necmergitur.msgs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ReturnMessage {
	public boolean 		status;
	public Object	 	message;
}
